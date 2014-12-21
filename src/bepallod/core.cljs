(ns bepallod.core)

(enable-console-print!)

(def canvas (.getElementById js/document "canvas"))
(def context (.getContext canvas "2d"))

(def colors ["#ffff00" "#ff00ff" "#00ffff" "#ff7f7f" "#7fff7f" "#7f7fff" "#aaaaaa"])

(def balls
  (->>
    (for [x (range 25 425 50)
          y (range 25 425 50)]
      [x y x y 25 (rand-nth colors)])
    (mapv #(zipmap [:cur-x :cur-y :target-x :target-y :dimension :color] %))))

(def ball-state (atom balls))

(defn drawBall [ball]
  (.save context)
  (.translate context (ball :cur-x) (ball :cur-y))
  (set! (. context -lineWidth) 2)
  (let [gradient (.createLinearGradient context 0 0 (ball :dimension) (ball :dimension))]
    (.addColorStop gradient 0 (ball :color))
    (.addColorStop gradient 1 "#000000")
    (set! (. context -fillStyle) gradient))
  (.beginPath context)
  (.arc context 0 0 (ball :dimension) 0 (* Math/PI 2) true)
  (.closePath context)
  (.fill context)
  (.stroke context)
  (.restore context))

(defn is-near? [a b]
  (< (Math/abs (- a b)) 40))

(.addEventListener canvas "click"
  (fn [event]
    (let [x (. event -layerX)
          y (. event -layerY)
          fb (filter (fn [ball] (and (is-near? (ball :cur-x) x) (is-near? (ball :cur-y) y))) @ball-state)]
      (when (= (count fb) 4)
        (println "Hit!")))
    false))

(doseq [b @ball-state] (drawBall b))
