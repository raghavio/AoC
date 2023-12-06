(require '[clojure.string :as str])

(def input-content (slurp "2023/05/input"))
(def seeds-str (first (str/split input-content #"\n\n")))
(def mappings-str (rest (str/split input-content #"\n\n")))

(def seeds ((comp (partial map bigint) (partial re-seq #"\d+")) seeds-str))

(def all-mappings
  (mapv (fn [s] 
         (let [m ((comp rest str/split-lines) s)]
           (mapv #(mapv bigint (str/split % #" ")) m))) mappings-str))

(defn get-mapped-number
  [n mappings]
  (or (some (fn [[destination source length]]
              (let [source-end (+ source length)]
                (when (and (>= n source) (<= n source-end))
                  (+ destination (- n source))))) mappings)
      n))

(defn seeds-to-location
  [seeds all-mappings]
  (reduce #(assoc %1 %2 (reduce get-mapped-number %2 all-mappings)) {} seeds))

(comment
  ; Part 1
  (apply min (vals (seeds-to-location seeds all-mappings))))