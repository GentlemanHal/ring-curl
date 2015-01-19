(ns ring-curl.core
  (:require [clojure.string :refer [upper-case blank? join escape replace]]
            [clojure.data.json :as json]
            [clojure.xml :as xml])
  (:refer-clojure :exclude [replace]))

(def ^:dynamic write-json (fn [body] (json/write-str body :escape-slash false)))
(def ^:dynamic write-xml (fn [body] (with-out-str (xml/emit-element body))))

(def escaped-quote "\\\"")

(defn- quoted [& s]
  (str "\"" (escape (join s) {\" escaped-quote}) "\""))

(defn- sp [s]
  (if-not (blank? s) (str " " s)))

(defn method [request]
  (let [request-method (or (:request-method request) :get)]
    (str "-X " (upper-case (name request-method)) (if (= :head request-method) " --head"))))

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
  (str "-H " (quoted (name k) (if (nil? v) ";" (str ": " v)))))

(defn headers [request]
  (if-let [headers (:headers request)]
    (join " " (map map-header headers))))

(defn- header [request header-key]
  (if-let [headers (get request :headers)]
    (or (get headers header-key) (get headers (name header-key)))))

(defn- xml? [request]
  (if-let [content-type (header request :content-type)]
    (or (re-matches #"application/.*?xml" content-type)
        (= "text/xml" content-type))))

(defn- form? [request]
  (if-let [content-type (header request :content-type)]
    (.startsWith content-type "application/x-www-form-urlencoded")))

(defn- write-form-entry [[key val]]
  (str "--data-urlencode " (quoted (name key) "=" val)))

(defn- write-form [request]
  (join " " (map write-form-entry (:form-params request))))

(defn- data-binary [data]
  (if-not (nil? data)
    (str "--data-binary " (quoted data))))

(defn- missing? [body]
  (or (nil? body)
      (and (seq? body) (empty? body))))

(defn form [request]
  (form? request) (write-form request))

(defn data [request]
  (let [body (:body request)]
    (if-not (missing? body)
      (data-binary
        (cond
          (string? body) body
          (xml? request) (write-xml body)
          :else (write-json body))))))

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
    (str "-o " (quoted file) " --create-dirs")))

(defn- retry [options]
  (if-let [val (:retry options)]
    (str "--retry " val)))

(defn- dump-headers [options]
  (if-let [file (:dump-headers options)]
    (str "-D " (quoted file))))

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
   retry
   dump-headers])

(defn- apply-options [options]
  (join " " (keep identity (map #(% options) all-options))))

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
         (sp (form request))
         (sp (quoted (url request))))))
