(ns bepallod.core-test
  (:require
    [cljs.test :refer-macros [deftest testing is]]
    [bepallod.core :as b]))

(deftest is-near-test
  (testing "is-near returns true if two values are closer than 40"

    (is (b/is-near? 0 39))
    (is (b/is-near? 39 0))
    (is (not (b/is-near? 0 40)))
    (is (not (b/is-near? 40 0)))
    (is (b/is-near? 0 (- 39)))
    (is (b/is-near? (- 39) 0))
    (is (not (b/is-near? 0 (- 40))))
    (is (not (b/is-near? (- 40) 0)))
    (is (b/is-near? (- 40) (- 79)))
    (is (b/is-near? (- 79) (- 40)))
    (is (not (b/is-near? (- 40) (- 80))))
    (is (not (b/is-near? (- 80) (- 40))))))
