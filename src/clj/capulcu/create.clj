(ns capulcu.create
  (:require [capulcu.db.core :refer [*db*] :as db]
            [next.jdbc :as jdbc]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [luminus-migrations.core :as migrations]
            [mount.core :as mount]
            [capulcu.config :refer [env]]
            [clojure.string :as str]))

(def country-codes ["tr"])

(defn- once []
  (mount/start
   #'capulcu.config/env
   #'capulcu.db.core/*db*))

(defn- read-resource-data [path]
  (->> path
       io/resource
       slurp
       edn/read-string))

(defn- create-country-to-db [{:keys [name iso2]}]
  (db/create-country!
    {:name name
     :code iso2}))

(defn- create-city-to-db [{:keys [city country-id]}]
  (db/create-city!
   {:name       city
    :country_id country-id}))

(defn- create-district-to-db [{:keys [city city-id]}]
 (when (and city city-id)
  (db/create-district!
   {:name    city
    :city_id city-id})))

(defn- create-country-rows [coll]
  (doseq [row coll]
    (create-country-to-db row)))

(defn- create-city-rows [country-id coll]
  (doseq [row coll]
    (create-city-to-db (conj row {:country-id country-id}))))

(defn- create-district-rows [city-id coll]
  (doseq [row coll]
    (create-district-to-db (conj row {:city-id city-id}))))

(defn- create-country []
  (some-> "data/country.edn"
          read-resource-data
          create-country-rows))

(defn- create-city [path country-id]
  (some->> path
           read-resource-data
           (filterv #(= (:city %) (:admin %)))
           (create-city-rows country-id)))

(defn- create-district [path]
  (doseq [city (db/get-all-city)]
    (some->> path
             read-resource-data
             (filterv #(and (= (:admin %) (:name city))
                            (not= (:admin %) (:city %))))
             (create-district-rows (:id city)))))

(defn- get-files [path]
  (some->> path
           io/resource
           io/file
           file-seq
           (filter #(.isFile %))
           (mapv #(.getName %))))

(defn- file-name [file]
  (some-> file (str/split #"\.") first))

(defn- country-from-db [code]
  (db/get-country {:code code}))

(defn- create-with-files []
  (doseq [file (remove #(= % "country.edn") (get-files "data/"))
          :let [path (str "data/" file)]]
    (println "File:" path)
    (create-city path
                 (some-> file file-name str/upper-case country-from-db :id))
    (create-district path)))

(defn create-datas
  "Create Datas"
  []
  (println "Starting create datas")
  (once)
  (create-country)
  (create-with-files)
  (println "Finishing create datas"))
