(ns ring-curl.core
  (:require [clojure.string :refer [upper-case blank? join]]
            [camel-snake-kebab.core :refer [->HTTP-Header-Case]]))

(defn method [request]
  (let [request-method (:request-method request)]
    (str "-X " (upper-case (if (nil? request-method) "get" (name request-method))) (if (= :head request-method) " --head"))))

(defn- scheme [request]
  (let [s (:scheme request)]
    (str (if (nil? s) "http" (name s)) "://")))

(defn- server-name [request]
  (:server-name request))

(defn- port [request]
  (str ":" (:server-port request)))

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

(defn- verbose [options]
  (if (:verbose options) "-v"))

(defn- silent [options]
  (if (:silent options) "-s"))

(def all-options
  [silent
   verbose])

(defn- apply-options [options]
  (join " " (keep identity (map (fn [f] (f options)) all-options))))

(defn to-curl
  "Converts the given ring request to a cURL command"
  ([request]
   (to-curl request {:verbose true
                     :silent  false}))
  ([request options]
   (str "curl " (apply-options options) " " (method request) " " (headers request) " \"" (url request) "\"")))
