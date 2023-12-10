(ns mirage-maintenance 
  (:require [clojure.string :as str]))

(defn infinite-consecutive-diffs
  [coll] ;; Returns a sequence of difference on a collection by itself.
  (iterate #(map - (next %) %) coll))

(defn fetch-till-same ;; Fetch from collections till all the values are same. Includes the same value.
  [colls]
  (concat (take-while (partial apply not=) colls) ;; Fetch until the values are different. 
          [(some #(when (apply = %) %) colls)])) ;; Concat the first same value

(defn predicted-value 
  [numbers]
  (reduce + ((comp (partial map last) fetch-till-same infinite-consecutive-diffs) numbers)))

(comment
  (def values ((comp (partial mapv (comp (partial mapv parse-long) #(str/split % #" "))) str/split-lines slurp) "2023/09/input"))

  (def part1 (reduce + (map predicted-value values)))
  (def part2 (reduce + (map (comp predicted-value reverse) values))))