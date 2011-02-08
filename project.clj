(defproject boat "0.0.1-SNAPSHOT"
  :description "Some shit that does stuff with boats."
  :repositories {"intalio" "http://www.intalio.org/public/maven2/"}
  :dependencies [[org.clojure/clojure "1.3.0-alpha4"]
                 [org.clojure.contrib/standalone "1.3.0-alpha4"]
                 [org.supercsv/SuperCSV "1.52"]
                 [org.antlr/stringtemplate "3.2"]]
  :dev-dependencies [[swank-clojure "1.3.0-SNAPSHOT"]]
  :main boat.core)
