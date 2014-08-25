(ns ring-curl.core
  (:require [clojure.string :refer [upper-case blank? join escape]]
            [camel-snake-kebab.core :refer [->HTTP-Header-Case]]
            [clojure.data.json :as json]))

(defn method [request]
  (let [request-method (:request-method request)]
    (str "-X " (upper-case (if (nil? request-method) "get" (name request-method))) (if (= :head request-method) " --head"))))

(defn- scheme [request]
  (let [s (:scheme request)]
    (str (if (nil? s) "http" (name s)) "://")))

(defn- server-name [request]
  (:server-name request))

(defn- port [request]
  (let [port (:server-port request)]
    (if-not (nil? port)
      (str ":" port))))

(defn- path [request]
  (:uri request))

(defn- query-string [request]
  (let [qs (:query-string request)]
    (if-not (blank? qs)
      (str "?" qs))))

(defn url [request]
  (str (scheme request) (server-name request) (port request) (path request) (query-string request)))

(defn- map-header [[k v]]
  (str "-H \"" (->HTTP-Header-Case k) (if (nil? v) ";" (str ": " v)) "\""))

(defn headers [request]
  (join " " (map map-header (:headers request))))

(defn data [request]
  (let [body (:body request)]
    (str "-d \""
         (escape
           (cond
             (string? body) (:body request)
             (map? body) (json/write-str body))
           {\" "\\\""})
         "\"")))

(defn- verbose [options]
  (if (:verbose? options) "-v"))

(defn- silent [options]
  (if (:silent? options) "-s"))

(defn- no-proxy [options]
  (if-let [hosts (not-empty (:no-proxy options))]
    (str "--noproxy \"" (join ", " hosts) "\"")))

(defn- progress-bar [options]
  (if (:progress-bar? options) "-#"))

(defn- insecure [options]
  (if (:insecure? options) "-k"))

(defn- connect-timeout [options]
  (if-let [timeout (:conn-timeout options)] (str "--connect-timeout " timeout)))

(def all-options
  [silent
   verbose
   no-proxy
   progress-bar
   insecure
   connect-timeout])

(defn- apply-options [options]
  (join " " (keep identity (map (fn [f] (f options)) all-options))))

(def default-options
  {:verbose?      true
   :silent?       false
   :no-proxy      []
   :progress-bar? false
   :insecure?     false})

(defn to-curl
  "Converts the given ring request to a cURL command"
  ([request]
   (to-curl request default-options))
  ([request options]
   (str "curl " (apply-options options) " " (method request) " " (headers request) " \"" (url request) "\"")))
