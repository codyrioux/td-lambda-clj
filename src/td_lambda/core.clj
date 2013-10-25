(ns td-lambda.core
  "A vectorized implementation of TD(lambda) reinforcement learning algorithm."
  (:require [td-lambda.util :as util]
            [incanter.core :refer [matrix mult mmult plus trans]]))

(defn- q
  [reward m y theta fe s a]
  (+ (reward (m s a))
     (* y (first (mmult (trans theta) (matrix (fe (m s a))))))))

(defn- policy
  "Applies the policy that would be defined by v to select an action.
   Currently implements Softmax Action Selection over a Boltzmann
   distribution. As temperature r is reduced the policy becomes
   greedier."
  [theta reward y m r fe sp s]
  (let
    [actions (sp s)
     q (partial q reward m y theta fe s)]
    (cond
      (empty? actions) nil
      (= 0 r)
      (let [action (util/argmax q actions)] action)
      :else
      (let
        [actions (sp s)
         boltzmann-sum (reduce + 0 (map #(java.lang.StrictMath/exp
                                           (/ (q %) r)) actions))
         boltzmann-probs (zipmap actions (map #(/ (java.lang.StrictMath/exp
                                                    (/ (q %) r)) boltzmann-sum) actions))]
        (util/rand-from-probdist boltzmann-probs)))))

(defn- episode
  [m reward fe si sp lambda y terminal? tk alphak theta]
  (loop
    [s si
     theta theta
     e (matrix (repeat (count (fe si)) 0))]
    (let [s' (m s (policy theta reward y m tk fe sp s))
          r (reward s')
          fe-s (matrix (fe s))
          delta (- (+ r (* y (first (mmult (trans theta) (matrix (fe s'))))))
                   (first (mmult (trans theta) fe-s)))
          e (plus (mult (* y lambda) e) fe-s)
          theta (plus theta (mult (* alphak delta) e))]
      (if (terminal? s') theta (recur s' theta e)))))

(defn learn
  "Implements TD(lambda) for summarization using reinforcement learning.

   m: Generative model in which (m s a) => s'
   reward: Reward function (reward s) => double
   fe: feature extraction function for states.
   initial-state: The initial state for each episode
   sp: Source of actions for a state where (sp s) => [a1, a2, an]
   lambda: The trace decay parameter.
   alpha: The learning rate, a small positive number.
   y: Discount rate for future states.
   nmax: Number of episodes for learning.
   terminal?: A function that determines if a state s is a terminal state.
   reset: Currently does nothing.

   Returns a policy function p in which (p s) => a."
  [m reward fe initial-state sp lambda y alpha nmax terminal? & {:keys [reset] :or {reset true}}]
  (partial policy
           (nth (iterate (partial episode m reward fe initial-state sp lambda y terminal? 0.987 alpha)
                         (matrix (repeat (count (fe initial-state)) 0)))
                (dec nmax))
           reward y m 0 fe sp))
