(ns td-lambda.core-test
  "A test case that runs the sample problem located in td-lambda.sample.
   This is the best I can currently do for a learning algorithm. If anyone
   has advice on unit testing machine learning algorithms please open
   a github ticket on the subject. I should very much like to discuss it."
  (:use clojure.test
        td-lambda.core
        td-lambda.sample))

(deftest ensure-learning
  (testing "Ensuring learning took place for sample problem."
    (let
      [pol (create-policy 0.5 0.001 0.1 100)]
      (is (= 4 (pol 0)))
      (is (= 4 (pol 4)))
      (is (= 2 (pol 8)))
      (is (= nil (pol 10)))
      (is (= 3 (pol 7))))))
