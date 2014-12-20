(ns bepallod.core)

(enable-console-print!)

(def canvas (.getElementById js/document "canvas"))
(def context (.getContext canvas "2d"))

(def colors ["#ffff00" "#ff00ff" "#00ffff" "#ff7f7f" "#7fff7f" "#7f7fff" "#aaaaaa"])

(defn balls []
  (for [x (range 25 425 50)
        y (range 25 425 50)]
    [x y 25 (rand-nth colors)]))

(defn drawBall [x y d c]
  (.save context)
  (.translate context x y)
  (set! (. context -lineWidth) 2)
  (let [gradient (.createLinearGradient context 0 0 d d)]
    (.addColorStop gradient 0 c)
    (.addColorStop gradient 1 "#000000")
    (set! (. context -fillStyle) gradient))
  (.beginPath context)
  (.arc context 0 0 d 0 (* Math/PI 2) true)
  (.closePath context)
  (.fill context)
  (.stroke context)
  (.restore context))

(doseq [[x y d c] (balls)] (drawBall x y d c))
