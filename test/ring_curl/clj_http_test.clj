(ns ring-curl.clj-http-test
  (:require [midje.sweet :refer :all]
            [ring-curl.clj-http :as subject]))

(facts "converting to a ring style request"
       (fact ":content-type"
             (subject/convert {:content-type "application/json"}) => (contains {:headers {"content-type" "application/json"}}))

       (fact ":content-length"
             (subject/convert {:content-length 1337}) => (contains {:headers {"content-length" 1337}}))

       (fact ":accept-encoding"
             (subject/convert {:accept-encoding "gzip, deflate"}) => (contains {:headers {"accept-encoding" "gzip, deflate"}}))

       (fact ":accept"
             (subject/convert {:accept "text/plain"}) => (contains {:headers {"accept" "text/plain"}}))

       (fact ":method"
             (subject/convert {:method :get}) => (contains {:request-method :get})))