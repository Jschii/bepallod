(ns bepallod.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
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

(deftest find-matches-test
  (testing "find-matches returns the indices of the elements that are three or more in a row"

    (is (= (b/find-matches [[{:index 0 :color 1} {:index 1 :color 1} {:index 2 :color 1} {:index 3 :color 2} {:index 4 :color 2} {:index 5 :color 3} {:index 6 :color 3} {:index 7 :color 4}]])
          [0 1 2]))
    (is (= (b/find-matches [[{:index 0 :color 1} {:index 1 :color 2} {:index 2 :color 2} {:index 3 :color 2} {:index 4 :color 3} {:index 5 :color 3} {:index 6 :color 4} {:index 7 :color 4}]])
          [1 2 3]))
    (is (= (b/find-matches [[{:index 0 :color 1} {:index 1 :color 1} {:index 2 :color 2} {:index 3 :color 2} {:index 4 :color 2} {:index 5 :color 3} {:index 6 :color 3} {:index 7 :color 4}]])
          [2 3 4]))
    (is (= (b/find-matches [[{:index 0 :color 1} {:index 1 :color 1} {:index 2 :color 2} {:index 3 :color 3} {:index 4 :color 3} {:index 5 :color 3} {:index 6 :color 4} {:index 7 :color 4}]])
          [3 4 5]))
    (is (= (b/find-matches [[{:index 0 :color 1} {:index 1 :color 1} {:index 2 :color 2} {:index 3 :color 2} {:index 4 :color 3} {:index 5 :color 3} {:index 6 :color 3} {:index 7 :color 4}]])
          [4 5 6]))
    (is (= (b/find-matches [[{:index 0 :color 1} {:index 1 :color 1} {:index 2 :color 2} {:index 3 :color 2} {:index 4 :color 3} {:index 5 :color 4} {:index 6 :color 4} {:index 7 :color 4}]])
          [5 6 7]))
    (is (= (b/find-matches [[{:index 0 :color 1} {:index 1 :color 2} {:index 2 :color 2} {:index 3 :color 2} {:index 4 :color 2} {:index 5 :color 1} {:index 6 :color 2} {:index 7 :color 2}]])
          [1 2 3 4]))
    (is (= (b/find-matches [[{:index 0 :color 1} {:index 1 :color 1} {:index 2 :color 2} {:index 3 :color 2} {:index 4 :color 3} {:index 5 :color 3} {:index 6 :color 4} {:index 7 :color 4}]])
          []))
    (is (= (b/find-matches [[{:index 0 :color 1} {:index 1 :color 1} {:index 2 :color 2} {:index 3 :color 3} {:index 4 :color 3} {:index 5 :color 2} {:index 6 :color 1} {:index 7 :color 1}]])
          []))
    (is (= (b/find-matches [[{:index 0 :color 1} {:index 1 :color 1} {:index 2 :color 2} {:index 3 :color 2} {:index 4 :color 3} {:index 5 :color 3} {:index 6 :color 4} {:index 7 :color 4}]
                            [{:index 8 :color 1} {:index 9 :color 1} {:index 10 :color 1} {:index 11 :color 2} {:index 12 :color 2} {:index 13 :color 3} {:index 14 :color 3} {:index 15 :color 4}]])
          [8 9 10]))
    (is (= (b/find-matches [[{:index 0 :color 1} {:index 1 :color 1} {:index 2 :color 2} {:index 3 :color 2} {:index 4 :color 3} {:index 5 :color 4} {:index 6 :color 4} {:index 7 :color 4}]
                            [{:index 8 :color 1} {:index 9 :color 1} {:index 10 :color 1} {:index 11 :color 2} {:index 12 :color 2} {:index 13 :color 3} {:index 14 :color 3} {:index 15 :color 4}]])
          [5 6 7 8 9 10]))))
