(require '[clojure.string :as str])

(def games ((comp str/split-lines slurp) "2023/02/day_2_input"))

(defn largest-x-cube-in-game
  "Fetches the largest cube count of the provided color in a game."
  [color game-str]
  (let [color-count-re (re-pattern (format "(\\d+) %s" color))
        all-counts (map #(Integer/parseInt (second %)) (re-seq color-count-re game-str))]
    (last (sort all-counts))))

(defn game-id
  "Fetches the game id from the game string"
  [game-str]
  (Integer/parseInt (second (re-find #"Game (\d+)" game-str))))

(defn vaild-games
  [game-config]
  (filter (fn [game-str]
            (every? #(<= (largest-x-cube-in-game % game-str) (get game-config %))
                    ["red" "green" "blue"])) 
          games))

(defn valid-game-ids
  [game-config]
  (map game-id (vaild-games game-config)))

(reduce + (valid-game-ids {"red" 12 "green" 13 "blue" 14})) ;; Sum of all valid game ids

(defn power-of-set
  "Multiplication of the max count of each color"
  [game-str]
  (reduce * (map #(largest-x-cube-in-game % game-str) ["red" "green" "blue"])))

(reduce + (map power-of-set games)) ;; Sum of all the powers