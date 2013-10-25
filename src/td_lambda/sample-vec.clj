(ns td-lambda.sample-vec
  "A toy problem for testing the vectorized version
   of TD(lambda)."
  (:require [td-lambda.core :as tdl]))

(def goal [1 0 1 0 1 0])

(defn- flip [x]
  (if (= 1 x) 0 1))

(defn reward
  )

(defn m
  [s a]
  (assoc s a (flip (get s a))))

(defn sp
  [s]
  (if (= goal s) [] (range 0 6)))

(defn create-policy
  [lambda alpha y n max-steps]
  )
