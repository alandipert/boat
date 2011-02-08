(ns boat.core
  (:use [clojure.java.io :only (file)])
  (:import (jxl Workbook Sheet Cell))
  (:gen-class))

(defn ^Sheet sheet-from-file
  "Returns the nth sheet from File or filename f.
   When provided f only, return the first (0th) sheet."
  ([f n]
     (.getSheet (Workbook/getWorkbook (file f)) n))
  ([f]
     (grab-sheet f 0)))

(defn get-row
  "Given sheet, row number n, and the column to start at,
   returns a sequence of non-empty cell values.

   Given only sheet and row number n, returns the rows starting at
   column 0. "
  ([^Sheet sheet n start]
     (take-while
      (partial not= "")
      (map #(.. sheet
                (getCell % n)
                getContents)
           (iterate inc start))))
  ([sheet n]
     (get-row sheet n 0)))

(defn -main [& args]
  (println "Yo, boat here!"))


