(ns td-lambda.util)

(defn argmax
  "Returns the item in coll which results in the maximal value for f."
  [f coll]
  (when (seq coll)
    (let [results (zipmap coll (map f coll))
          max-value (apply max (vals results))
          max-args (map first (filter #(= max-value (second %)) results))]
      (rand-nth max-args))))

(defn rand-from-probdist
  [probdist]
  (when (seq probdist)
    (loop
      [r (rand)
       coll probdist]
      (cond
        (= 1 (count coll))
        (ffirst coll)
        (> 0 (- r (second (first coll))))
        (ffirst coll)
        :else
        (recur
          (- r (second (first coll)))
          (rest coll))))))
