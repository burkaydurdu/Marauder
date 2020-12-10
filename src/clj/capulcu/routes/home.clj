(ns capulcu.routes.home
  (:require
   [capulcu.layout :as layout]
   [capulcu.db.core :as db]
   [clojure.java.io :as io]
   [capulcu.middleware :as middleware]
   [ring.util.response]
   [ring.util.http-response :as response]))

(defn- string->uuid [string-uuid]
  (try
    (java.util.UUID/fromString string-uuid)
    (catch Exception e
      nil)))

(defn- countries [request]
  (let [opt (select-keys (:params request) [:code])]
    (layout/render-success (if (empty? opt)
                             (db/get-all-country)
                             (db/get-country {:code "TR"})))))

(defn- cities [request]
  (let [opt (select-keys (:params request) [:country_id])]
    (if (contains? opt :country_id)
      (-> {:country_id (string->uuid (:country_id opt))}
          db/get-city
          layout/render-success)
      (layout/render-error {:body   "Missing Parameter"
                            :status 400}))))

(defn- districts [request]
  (let [opt (select-keys (:params request) [:city_id])]
    (if (contains? opt :city_id)
      (-> {:city_id (string->uuid (:city_id opt))}
          db/get-district
          layout/render-success)
      (layout/render-error {:body   "Missing Parameter"
                            :status 400}))))

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/countries" {:get countries}]
   ["/cities"    {:get cities}]
   ["/districts" {:get districts}]])
