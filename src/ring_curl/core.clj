(ns ring-curl.core
  (:require [clojure.string :refer [upper-case blank? join escape replace]]
            [camel-snake-kebab.core :refer [->HTTP-Header-Case]]
            [clojure.data.json :as json]
            [clojure.xml :as xml])
  (:refer-clojure :exclude [replace]))

(def ^:dynamic write-json json/write-str)
(def ^:dynamic write-xml (fn [body] (with-out-str (xml/emit-element body))))

(def escaped-quote "\\\"")

(defn- quoted [& s]
  (str "\"" (join s) "\""))

(defn- sp [s]
  (if-not (blank? s) (str " " s)))

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
  (str "-H " (quoted (->HTTP-Header-Case k) (if (nil? v) ";" (str ": " v)))))

(defn headers [request]
  (if-let [headers (:headers request)]
    (join " " (map map-header headers))))

(defn- xml? [request]
  (if-let [content-type (get-in request [:headers "content-type"])]
    (or (re-matches #"application/.*?xml" content-type)
        (= "text/xml" content-type))))

(defn data [request]
  (let [body (:body request)]
    (if-not (nil? body)
      (str "--data-binary "
           (quoted
             (escape
               (cond
                 (string? body) body
                 (xml? request) (write-xml body)
                 :else (write-json body))
               {\" escaped-quote})
             )))))

(defn- verbose [options]
  (if (:verbose? options) "-v"))

(defn- very-silent [options]
  (if (:very-silent? options) "-s"))

(defn- silent [options]
  (if (:silent? options) "-s -S"))

(defn- no-proxy [options]
  (if-let [hosts (not-empty (:no-proxy options))]
    (str "--noproxy " (quoted (join ", " hosts)))))

(defn- progress-bar [options]
  (if (:progress-bar? options) "-#"))

(defn- insecure [options]
  (if (:insecure? options) "-k"))

(defn- connect-timeout [options]
  (if-let [timeout (:connect-timeout options)]
    (str "--connect-timeout " timeout)))

(defn- max-time [options]
  (if-let [timeout (:max-time options)]
    (str "-m " timeout)))

(defn- no-buffer [options]
  (if (:no-buffer? options) "-N"))

(defn- output [options]
  (if-let [file (:output options)]
    (str "-o " (quoted file))))

(defn- retry [options]
  (if-let [val (:retry options)]
    (str "--retry " val)))

(def all-options
  [very-silent
   silent
   verbose
   no-proxy
   progress-bar
   insecure
   connect-timeout
   max-time
   no-buffer
   output
   retry])

(defn- apply-options [options]
  (join " " (keep identity (map (fn [f] (f options)) all-options))))

(def default-options
  {:verbose? true})

(defn to-curl
  "Converts the given ring request to a cURL command"
  ([request]
   (to-curl request default-options))
  ([request options]
   (str "curl"
        (sp (apply-options options))
        (sp (method request))
        (sp (headers request))
        (sp (data request))
        (sp (quoted (url request))))))
