(ns ring-curl.clj-http)

(defn- header [request key]
  (if-let [value (get request key)]
    (merge-with merge request {:headers {(name key) value}})
    request))

(defn- method [request]
  (if-let [value (:method request)]
    (merge request {:request-method value})
    request))

(defn convert [request]
  (-> request
      (header :content-type)
      (header :content-length)
      (header :accept-encoding)
      (header :accept)
      method))
