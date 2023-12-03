(require '[clojure.string :as str])

(def schematic ((comp str/split-lines slurp) "day_3_input"))

(defn symbol?
  "Checks if the given string has any character other than words, numbers or ."
  [s]
  (boolean (re-find #"[^\w\d.]" s)))

(defn str-number?
  "Checks if the given string is a number or not"
  [c]
  (re-find #"\d" (str c)))

(defn number-adjacent?
  [line-index from-index number]
  (let [lines-to-scan (cond-> [(get schematic line-index)]
                        (> line-index 0) (conj (get schematic (dec line-index)))
                        (< line-index (dec (count schematic))) (conj (get schematic (inc line-index))))
        from-index*   (if (zero? from-index) from-index (dec from-index))
        to-index      (+ (count number) from-index)]
    (or (some (fn [s] 
                (when (symbol? (subs s from-index* (if (= to-index (count s))
                                                     to-index
                                                     (inc to-index))))
                  (Integer/parseInt number))) 
              lines-to-scan) 0)))

(reduce + (map-indexed (fn [line-index line]
               (loop [line          line
                      current-index 0
                      line-sum      0
                      last-number   ""]
                 (let [c          (first line)
                       remaining  (rest line)
                       new-index  (inc current-index)
                       from-index (- current-index (count last-number))]
                   (cond 
                     (str-number? c) (recur remaining new-index line-sum (str last-number c))
                     (seq last-number) (recur remaining new-index (+ line-sum (number-adjacent? line-index from-index last-number)) "")
                     (not c) line-sum
                     :else (recur remaining new-index line-sum last-number))))) schematic))
                  