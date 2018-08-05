(defproject ring-curl "1.0.0"
  :description "Converts ring requests to cURL commands"
  :url "https://github.com/GentlemanHal/ring-curl"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/tools.logging "0.4.1"]
                 [org.clojure/data.json "0.2.6"]]
  :javac-options ["-Dclojure.compiler.direct-linking=true"]
  :aliases {"test" ["midje"]
            "lint" ["eastwood"]}
  :repositories [["releases" {:url      "https://clojars.org/repo"
                              :username :env/clojars_username
                              :password :env/clojars_password}]]
  :profiles {:dev {:plugins      [[lein-midje "3.2.1"]
                                  [lein-ancient "0.6.15"]
                                  [jonase/eastwood "0.2.8"]]
                   :dependencies [[org.clojure/clojure "1.8.0"]
                                  [midje "1.9.2"]
                                  [clj-http "3.9.1"]]}})
