(ns ring-curl.core-test
  (:require [midje.sweet :refer :all]
            [ring-curl.core :as subject]))

;The standard keys are:
;
;:remote-addr The IP address of the client or the last proxy that sent the request.
;:content-type The MIME type of the request body, if known.
;:content-length The number of bytes in the request body, if known.
;:character-encoding The name of the character encoding used in the request body, if known.
;:body An InputStream for the request body, if present.

(facts "adds the request method"
       (fact "converted to uppercase"
             (subject/method {:request-method :get}) => "-X GET"
             (subject/method {:request-method :post}) => "-X POST"
             (subject/method {:request-method :put}) => "-X PUT"
             (subject/method {:request-method :options}) => "-X OPTIONS"
             (subject/method {:request-method :delete}) => "-X DELETE")

       (fact "additional --head argument gets added"
             (subject/method {:request-method :head}) => "-X HEAD --head")

       (fact "defaults to GET if missing"
             (subject/method {}) => "-X GET"))

(facts "adds the url"
       (facts "adds the scheme"
              (fact "with the correct :// seperator"
                    (subject/url {:scheme :http}) => (has-prefix "http://")
                    (subject/url {:scheme :https}) => (has-prefix "https://"))

              (fact "defaults to http if missing"
                    (subject/url {}) => (has-prefix "http://")))

       (facts "adds the host"
              (subject/url {:server-name "some-server"}) => (contains "some-server"))

       (facts "adds the port"
              (fact "with the correct : seperator"
                    (subject/url {:server-port 11090}) => (contains ":11090"))

              (fact "doesn't add the : seperator if missing"
                    (subject/url {:server-name "server-name" :server-port nil}) =not=> (contains "server-name:")
                    (subject/url {:server-name "server-name"}) =not=> (contains "server-name:")))

       (fact "adds the path"
             (subject/url {:uri "/some/path/to/a/resource"}) => (contains "/some/path/to/a/resource"))

       (facts "adds the query string"
              (fact "with the correct ? seperator"
                    (subject/url {:query-string "a=1&b=2"}) => (contains "?a=1&b=2"))

              (fact "doesn't add the ? seperator if missing"
                    (subject/url {:query-string nil}) =not=> (contains "?")
                    (subject/url {}) =not=> (contains "?")))

       (fact "complete example"
             (subject/url {:scheme       :https
                           :server-name  "github.com"
                           :server-port  80
                           :uri          "/GentlemanHal/ring-curl"
                           :query-string "page=2&per_page=100"}) => "https://github.com:80/GentlemanHal/ring-curl?page=2&per_page=100"))

(facts "adds headers"
       (fact "single header"
             (subject/headers {:headers {"date" "Tue, 15 Nov 1994 08:12:31 GMT"}}) => "-H \"Date: Tue, 15 Nov 1994 08:12:31 GMT\"")

       (fact "multiple headers"
             (subject/headers {:headers {"accept-charset"  "utf-8"
                                         "accept-language" "en-GB"}}) => "-H \"Accept-Charset: utf-8\" -H \"Accept-Language: en-GB\"")

       (fact "headers with no value are added with a semi-colon"
             (subject/headers {:headers {"x-custom-header" nil}}) => "-H \"X-Custom-Header;\""))

(facts "adds data"
       (fact "strings get added as is"
             (subject/data {:body "a string body"}) => "-d \"a string body\"")

       (fact "maps get converted to json"
             (subject/data {:body {:foo "bar"}}) => "-d \"{\\\"foo\\\":\\\"bar\\\"}\""))

(facts "curl"
       (fact "simple example with default options"
             (subject/to-curl {:request-method :get
                               :scheme         :http
                               :server-name    "server.com"
                               :server-port    1234
                               :uri            "/some/path"
                               :query-string   "a=1&b=2"
                               :headers        {"foo" "bar"
                                                "bas" "baz"}}) => "curl -v -X GET -H \"Foo: bar\" -H \"Bas: baz\" \"http://server.com:1234/some/path?a=1&b=2\"")

       (facts "options"
              (fact "verbose"
                    (subject/to-curl {} {:verbose? true}) => (contains " -v "))

              (fact "silent"
                    (subject/to-curl {} {:silent? true}) => (contains " -s "))

              (fact "no proxy"
                    (subject/to-curl {} {:no-proxy ["a" "b" "c"]}) => (contains " --noproxy \"a, b, c\" "))

              (fact "progress bar"
                    (subject/to-curl {} {:progress-bar? true}) => (contains " -# "))

              (fact "insecure"
                    (subject/to-curl {} {:insecure? true}) => (contains " -k "))

              (fact "connection timeout"
                    (subject/to-curl {} {:connect-timeout 60}) => (contains " --connect-timeout 60 "))

              (fact "max time"
                    (subject/to-curl {} {:max-time 120}) => (contains " -m 120 "))))