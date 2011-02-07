(defproject boat "0.0.1-SNAPSHOT"
  :description "Some shit that does stuff with boats."
  :dependencies [[org.clojure/clojure "1.3.0-alpha4"]
                 ;; TODO pare down to only the libraries we need
                 [org.clojure.contrib/standalone "1.3.0-alpha4"]
                 [jexcelapi/jxl "2.4.2"]]
  :dev-dependencies [[swank-clojure "1.3.0-SNAPSHOT"]]
  :main boat.core)
