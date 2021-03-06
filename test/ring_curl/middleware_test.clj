(ns ring-curl.middleware-test
  (:require [midje.sweet :refer :all]
            [ring-curl.middleware :as subject]
            [clojure.tools.logging :as log]
            [clojure.tools.logging.impl :as log-impl]
            [ring-curl.core :as core]))

(background
  (core/to-curl anything anything) => ..curl..
  (log-impl/enabled? anything anything) => true)

(fact "calls log with :debug by default"
      ((subject/wrap-curl-logging identity) ..request..) => irrelevant
      (provided
        (log/log* anything :debug nil ..curl..) => irrelevant))

(facts "calls log with the given level"
       (fact "info"
             ((subject/wrap-curl-logging irrelevant {:level :info}) ..request..) => irrelevant
             (provided
               (log/log* anything :info nil ..curl..) => irrelevant))

       (fact "warn"
             ((subject/wrap-curl-logging irrelevant {:level :warn}) ..request..) => irrelevant
             (provided
               (log/log* anything :warn nil ..curl..) => irrelevant))

       (fact "error"
             ((subject/wrap-curl-logging irrelevant {:level :error}) ..request..) => irrelevant
             (provided
               (log/log* anything :error nil ..curl..) => irrelevant))

       (fact "trace"
             ((subject/wrap-curl-logging irrelevant {:level :trace}) ..request..) => irrelevant
             (provided
               (log/log* anything :trace nil ..curl..) => irrelevant))

       (fact "fatal"
             ((subject/wrap-curl-logging irrelevant {:level :fatal}) ..request..) => irrelevant
             (provided
               (log/log* anything :fatal nil ..curl..) => irrelevant)))

(fact "calls to-curl with the given options"
      ((subject/wrap-curl-logging irrelevant {:options ..options..}) ..request..) => irrelevant
      (provided
        (core/to-curl ..request.. ..options..) => ..curl..))