(ns ring-curl.encoding-test
  (:use midje.sweet)
  (:require [ring-curl.encoding :as subject]))

(fact "encodes server names"
      (subject/server-name "server-name") => "server-name"
      (subject/server-name "server name") => "server%20name")