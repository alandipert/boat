(ns boat.core
  (:use [clojure.java.io :only (reader)])
  (:import (org.supercsv.io CsvListReader)
           (org.supercsv.prefs CsvPreference))
  (:require [clojure.set :as set])
  (:gen-class))

(def ^{:comment "Map of roman numerals to description and afi"
       :private true}
  numeral->category
  { "I"    {:desc "Hull structure"     :afi 0}
    "II"   {:desc "Interior structure" :afi 0}
    "III"  {:desc "Mechanical"         :afi 0}
    "IV"   {:desc "Electrical"         :afi 0}
    "V"    {:desc "Piping"             :afi 0}
    "VI"   {:desc "Wiring"             :afi 0}
    "VII"  {:desc "Hull outfit"        :afi 0}
    "VIII" {:desc "Deck equipment"     :afi 0}
    "IX"   {:desc "Interior outfit"    :afi 0}
    "X"    {:desc "Payload"            :afi 0}})

(defn- keep-line?
  "Given a line map, returns whether or not it's a map we'd want to
  retain."
  [{:keys [name]}]
  ((set (keys numeral->category)) (first (.split name " ")))  )

(def ^{:comment "Map of column indices to names"
       :private true}
  line-kmap
  {0 :name
   3 :wt
   4 :lcg
   5 :tcg
   6 :vcg})

(defn csv-lines
  [f]
  (let [csv-reader (CsvListReader. (reader f)
                                   CsvPreference/STANDARD_PREFERENCE)]
    (map vec (take-while (partial not= nil)
                         (repeatedly #(.read csv-reader))))))

(defn convert-line [kmap line]
  (let [line-map (zipmap (range) line)]
    (select-keys (set/rename-keys line-map kmap)
                 (vals kmap))))

(defn extract-lines [f]
  (let [lines (csv-lines f)
        structured-lines (map (partial convert-line line-kmap) lines)]
    (filter keep-line? structured-lines)))

(defn -main [& args]
  (println "Yo, boat here!"))
