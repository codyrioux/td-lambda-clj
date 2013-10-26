(ns td-lambda.util-test
  (:use clojure.test
        td-lambda.util))

(deftest test-argmax
  (testing "argmax selects maximum value."
    (is (= 6 (argmax identity [0 1 2 6 4 1 2 3 1 2]))))
  (testing "argmax handles empty list gracefully."
    (is (= nil (argmax identity []) ))))

(deftest test-rand-from-probdist
  (testing "rand-from-probdist handles empty distribution gracefully."
    (is (= nil (rand-from-probdist {}))))
  (testing "rand-from-probdist selects a val with 1"
    (is (= :a (rand-from-probdist {:a 1.0}))))
  (testing "rand-from-probdist works with zero prob elements"
    (is (= :a (rand-from-probdist {:b 0.0 :c 0.0 :a 1.0})))
    (is (= :c (rand-from-probdist {:b 0.0 :c 1.0 :a 0.0}))))
  )
