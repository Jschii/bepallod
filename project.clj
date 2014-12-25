(defproject bepallod "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2496"]]

  :plugins [[lein-cljsbuild "1.0.4-SNAPSHOT"]]

  :source-paths ["src"]

  :hooks [leiningen.cljsbuild]

  :cljsbuild {
               :builds [{:id "dev"
                         :source-paths ["src"]
                         :compiler {
                                     :output-to "target/bepallod.js"
                                     :output-dir "target"
                                     :optimizations :none
                                     :source-map true}}
                        {:id "test"
                         :source-paths ["src" "test"]
                         :notify-command ["phantomjs" "phantom/unit-test.js" "phantom/unit-test.html"]
                         :compiler {:optimizations :whitespace
                                    :pretty-print true
                                    :output-to "target/testable.js"}}]})