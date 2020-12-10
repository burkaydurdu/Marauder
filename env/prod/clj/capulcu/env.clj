(ns capulcu.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[capulcu started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[capulcu has shut down successfully]=-"))
   :middleware identity})
