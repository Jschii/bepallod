(ns test-runner
  (:require
   [cljs.test :refer-macros [run-tests]]
   [bepallod.core-test]))

(enable-console-print!)

(defn runner []
  (if (cljs.test/successful?
        (run-tests
          'bepallod.core-test))
    0
    1))