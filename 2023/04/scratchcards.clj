(require '[clojure.string :as str]
         '[clojure.set :as set])

(def scratchcards ((comp str/split-lines slurp) "2023/04/input"))

(defn split-by-space 
  [s]
  (str/split s #" "))

(defn extract-numbers
  [card]
  (let [[_ game-id winning-numbers numbers] (re-find #"Card\s+(\d+): (.+?) \| (.+)" card)
        create-set (comp set (partial remove empty?) split-by-space)]
    {:game (Integer/parseInt game-id)
     :winning (create-set winning-numbers)
     :numbers (create-set numbers)}))

(defn add-matching-count
  [{:keys [winning numbers] :as data}]
  (assoc data :matching-count ((comp count set/intersection) winning numbers)))

(defn cards-&-amount
  "Returns a map of card number and its amount."
  [data]
  (reduce (fn [acc {:keys [game matching-count]}]
            (let [acc* (update acc game (fnil inc 0))
                  won (range (inc game) (+ matching-count (inc game))) ;; Copies won per card. 
                  copies (reduce #(assoc %1 %2 (get acc* game)) {} won)]
              (merge-with + copies acc*))) {} data))

(defn points
  [match-count]
  (int (Math/pow 2 (dec match-count))))

(comment
  ;; Part 1
  (apply + (map (comp points :matching-count add-matching-count extract-numbers) scratchcards))
  
  ;; Part 2
  (apply + (->> scratchcards
                (map (comp add-matching-count extract-numbers))
                cards-&-amount
                vals))
  )