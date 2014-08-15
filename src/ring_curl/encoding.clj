(ns ring-curl.encoding
  (:require [clojure.string :refer [escape]]))

(defn server-name [server-name]
  (escape server-name {\space "%20"}))
