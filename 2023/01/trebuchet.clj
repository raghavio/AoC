(require '[clojure.string :as str])

(def name-to-number
  {"one"   1
   "two"   2
   "three" 3
   "four"  4
   "five"  5
   "six"   6
   "seven" 7
   "eight" 8
   "nine"  9})

(defn name-to-number-fn [name]
  (get name-to-number name name))

(defn get
      []
      nil)

(defn calibration-value
  [text]
  (let [numbers-name-piped (str/join  "|" (keys name-to-number)) ;; Names of all numbers separated by | for regex
        first-digit-re (re-pattern  (format "\\d|%s" numbers-name-piped))
        first-digit (re-find first-digit-re text)
        last-digit-re  (re-pattern (format ".*(\\d|%s).*" numbers-name-piped))
        last-digit (second (re-find last-digit-re text))]
    (Integer/parseInt (str (name-to-number-fn first-digit) 
                           (name-to-number-fn last-digit)))))


(def file-content-str (slurp "./day_1_input"))

(def texts (str/split  file-content-str #"\n"))

;; Sum of all calibration values
(reduce + (map calibration-value texts))