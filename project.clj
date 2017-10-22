(defproject ring-curl "0.3.1"
            :description "Converts ring requests to cURL commands"
            :url "https://github.com/GentlemanHal/ring-curl"
            :license {:name "Eclipse Public License"
                      :url  "http://www.eclipse.org/legal/epl-v10.html"}
            :dependencies [[org.clojure/tools.logging "0.3.1"]
                           [org.clojure/data.json "0.2.6"]]
            :profiles {:dev {:plugins      [[lein-midje "3.1.3"]
                                            [lein-ancient "0.6.7"]]
                             :dependencies [[org.clojure/clojure "1.6.0"]
                                            [midje "1.7.0"]
                                            [clj-http "2.0.0"]]}})
