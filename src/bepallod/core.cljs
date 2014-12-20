(ns bepallod.core)

(enable-console-print!)

(def canvas (.getElementById js/document "canvas"))
(def context (.getContext canvas "2d"))

(defn balls []
  (for [x (range 25 425 50)
        y (range 25 425 50)]
    [x y]))

(defn drawBall [x y]
  (.save context)
  (.translate context x y)
  (set! (. context -lineWidth) 2)
  (let [gradient (.createLinearGradient context 0 0 25 25)]
    (.addColorStop gradient 0 "#ff0000")
    (.addColorStop gradient 1 "#000000")
    (set! (. context -fillStyle) gradient))
  (.beginPath context)
  (.arc context 0 0 25 0 (* Math/PI 2) true)
  (.closePath context)
  (.fill context)
  (.stroke context)
  (.restore context))

(doseq [[x y] (balls)] (drawBall x y))
