(ns boat.core
  (:use [clojure.java.io :only (reader)])
  (:import (org.supercsv.io CsvListReader)
           (org.supercsv.prefs CsvPreference))
  (:gen-class))

(def numeral->category
  { "I"  {:desc "Hull structure"      :afi 0}
    "II" {:desc "Interior structure"  :afi 0}
    "III" {:desc "Mechanical"         :afi 0}
    "IV" {:desc "Electrical"          :afi 0}
    "V"  {:desc "Piping"              :afi 0}
    "VI" {:desc "Wiring"              :afi 0}
    "VII" {:desc "Hull outfit"        :afi 0}
    "VIII" {:desc "Deck equipment"    :afi 0}
    "IX" {:desc "Interior outfit"     :afi 0}
    "X"  {:desc "Payload"             :afi 0}})

(defn csv-lines
  [f]
  (let [csv-reader (CsvListReader. (reader f)
                                   CsvPreference/STANDARD_PREFERENCE)]
    (map vec (take-while (partial not= nil)
                         (repeatedly #(.read csv-reader))))))

(defn convert-line [line]
  (let [indices-keys {0 :name
                      3 :wt
                      4 :lcg
                      5 :tcg
                      6 :vcg}
        columns (select-keys line (keys indices-keys))]
    (into {} (map (fn [[k v]] [(get indices-keys k) v])
                  (into [] columns)))))

(defn extract-lines [f]
  (let [lines (csv-lines f)
        structured-lines (map convert-line lines)]
    (filter (fn [{:keys [name]}]
              ((set (keys numeral->category)) (first (.split name " "))))
            structured-lines)))

(defn -main [& args]
  (println "Yo, boat here!"))
