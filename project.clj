(defproject ring-curl "0.1.0"
            :description "Converts ring requests to cURL commands"
            :url "https://github.com/GentlemanHal/ring-curl"
            :license {:name "Eclipse Public License"
                      :url  "http://www.eclipse.org/legal/epl-v10.html"}
            :dependencies [[camel-snake-kebab "0.2.2" :exclusions [org.clojure/clojure]]
                           [org.clojure/tools.logging "0.3.0"]]
            :profiles {:dev {:plugins      [[lein-midje "3.1.3"]
                                            [lein-ancient "0.5.5"]]
                             :dependencies [[org.clojure/clojure "1.6.0"]
                                            [midje "1.6.3"]
                                            [clj-http "1.0.0"]]}})
