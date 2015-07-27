(ns ring-curl.middleware
  (:require [clojure.tools.logging :as log]
            [ring-curl.core :refer :all]))

(defn wrap-curl-logging
  "Middleware to log the request as curl, logs using clojure.tools.logging and logs
  as :debug by default.

  Accepts the following options:

  :level          - the logging level to use
  :options        - the options to pass to pass to the to-curl function"
  [handler & [{:keys [level options]
               :or   {level   :debug
                      options default-options}}]]
  (fn [request]
    (log/log level (to-curl request options))
    (handler request)))