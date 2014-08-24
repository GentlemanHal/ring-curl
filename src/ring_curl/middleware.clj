(ns ring-curl.middleware
  (:require [clojure.tools.logging :as log]
            [ring-curl.core :refer :all]))

(defn log-as-curl
  "Middleware to log the request as curl, logs using clojure.tools.logging and logs
  as :debug by default."
  [handler & {:keys [level options]
              :or   {level   :debug
                     options default-options}}]
  (fn [request]
    (log/log level (to-curl request options))
    (handler request)))