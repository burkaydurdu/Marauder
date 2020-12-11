(ns capulcu.db.core-test
  (:require
   [capulcu.db.core :refer [*db*] :as db]
   [java-time.pre-java8]
   [luminus-migrations.core :as migrations]
   [clojure.test :refer :all]
   [next.jdbc :as jdbc]
   [capulcu.config :refer [env]]
   [mount.core :as mount]))

(use-fixtures
  :once
  (fn [f]
    (mount/start
     #'capulcu.config/env
     #'capulcu.db.core/*db*)
    (migrations/migrate ["migrate"] (select-keys env [:database-url]))
    (f)))

(deftest test-users
  (jdbc/with-transaction [t-conn *db* {:rollback-only true}]
    (is (= 1 (db/create-country!
              t-conn
              {:name "Türkiye"
               :code "TR"}
              {})))
    (is (=  {:name "Türkiye" :code "TR"}
            (-> (db/get-country t-conn {:code "TR"} {})
                (select-keys [:name :code]))))))
