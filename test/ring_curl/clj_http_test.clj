(ns ring-curl.clj-http-test
  (:require [midje.sweet :refer :all]
            [ring-curl.clj-http :as subject]))

(facts "converting to a ring style request"
       (fact ":content-type"
             (subject/convert {:content-type "application/json"}) => (contains {:headers {"content-type" "application/json"}}))

       (fact ":accept-encoding"
             (subject/convert {:accept-encoding [:gzip :deflate]}) => (contains {:headers {"accept-encoding" "gzip, deflate"}}))

       (fact ":accept"
             (subject/convert {:accept "text/plain"}) => (contains {:headers {"accept" "text/plain"}}))

       (fact ":method"
             (subject/convert {:method :get}) => (contains {:request-method :get}))

       (fact ":url"
             (subject/convert {:url "https://server-name:8080/some-path/here?query=param"}) => (contains {:scheme      :https
                                                                                                          :server-name "server-name"
                                                                                                          :server-port 8080
                                                                                                          :uri         "/some-path/here"}))

       (fact ":query-params"
             (subject/convert {:url "https://server-name" :query-params {:foo "bar"}}) => (contains {:query-string "foo=bar"}))

       (fact ":form-params"
             (subject/convert {:request-method :post :form-params {:foo "bar"}}) => (contains {:body "foo=bar"}))

       (fact ":basic-auth"
             (subject/convert {:basic-auth ["user" "pass"]}) => (contains {:headers {"authorization" "Basic dXNlcjpwYXNz"}})))
