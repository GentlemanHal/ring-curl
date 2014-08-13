(defproject ring-curl "0.1.0-SNAPSHOT"
  :description "Converts ring requests to cURL commands"
  :url "https://github.com/GentlemanHal/ring-curl"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [camel-snake-kebab "0.2.2"]
                 [midje "1.6.3"]]
  :profiles {:dev {:plugins [[lein-midje "3.1.3"]]}})
