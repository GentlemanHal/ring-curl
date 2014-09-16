(ns ring-curl.clj-http
  (:require [clj-http.client :refer :all])
  (:refer-clojure :exclude [get]))

(def middleware
  [wrap-query-params
   wrap-basic-auth
   wrap-oauth
   wrap-user-info
   wrap-url
   wrap-accept
   wrap-accept-encoding
   wrap-content-type
   wrap-form-params
   wrap-nested-params
   wrap-method])

(defn convert
  "Converts the given clj-http request to a ring request by calling some of the default clj-http middleware."
  [request]
  (reduce (fn [request middleware-fn]
            ((middleware-fn identity) request))
          request
          middleware))
