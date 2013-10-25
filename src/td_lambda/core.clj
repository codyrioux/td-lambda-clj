(ns td-lambda.core
  "A vectorized implementation of TD(lambda) reinforcement learning algorithm."
  (:require [td-lambda.util :as util]))

(defn theta-t-phi
  [theta phi]
  (reduce + (map * theta phi)))

(defn scalar-op-vec
  [s op v]
  (map (partial op s) v))

(defn vec-op-vec
  [x op y]
  (map op x y))

(defn v
  [theta fe-s]
  (theta-t-phi theta fe-s))

(defn q
  [reward m y theta fe s a]
  (+ 
    (reward (m s a))
    (* y (v theta (fe (m s a))))))

(defn- policy
  "Applies the policy that would be defined by v to select an action.
   Randomly selects between equivalent actions.
   Assumes an initial value of 0.01 for each state, uses zero
   when set to greedy (param r = 0)"
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
     e (repeat (count (fe si)) 0)]
    (let [s' (m s (policy theta reward y m tk fe sp s))
          r (reward s')
          fe-s (fe s)
          delta (- (+ r (* y (theta-t-phi theta (fe s')))) (theta-t-phi theta fe-s))
          fe-s (fe s)
          e (vec-op-vec (scalar-op-vec (* y lambda) * e) + fe-s)
          theta (vec-op-vec theta + (scalar-op-vec (* alphak delta) * e))]
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
   reset: A boolean determining if the eligibility should reset at each episode.

   Returns a policy function p in which (p s) => a."
  [m reward fe initial-state sp lambda y alpha nmax terminal? & {:keys [reset]
                                                                 :or {reset true}}]
  (partial policy
           (nth (iterate (partial episode m reward fe initial-state sp lambda y terminal? 0.987 alpha)
                         (repeat (count (fe initial-state)) 0))
                (dec nmax))
           reward y m 0 fe sp))

