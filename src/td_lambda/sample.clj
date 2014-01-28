(ns td-lambda.sample
  "A toy problem testing the TD lambda implementation.
   The learner stats at 0 and attempts to learn the fastest
   route to 10 by adding the numbers 0 to 4.
   Displays that learning did in fact take place."
  (:require [td-lambda.core :as tdl]))

(def goal 10)

;; Experiment Functions

(defn reward
  "Reward is 1 / (|(goal - (s))| + 1)"
  [s] 
  (/ 1 (inc (Math/abs (- goal s)))))

(defn m
  "States and actions are added."
  [s a]
  (cond
    (nil? a) s
    :else (+ s a)))

(defn sp
  "Can add or subtract 1 to 5."
  [s]
  (cond
    (= goal s) []
    :else (range 5)))

(defn features
  "Features are the value of the state and the difference from goal"
  [s]
  [s
   (- goal s)
   (if (pos? s) 1 0)
   (if (> goal s) 1 0)])

;; Utility Functions

(defn create-policy
  "Creates a policy for going from 0 to 10.
   Try calling with 0.5 0.001 0.1 100"
  [lambda alpha gamma n]
  (tdl/learn m reward features 0 sp lambda gamma alpha 0.987 n #(<= goal %)))
