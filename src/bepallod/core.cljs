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

(defn target-x [ball x y]
  (cond
    (and (> x (ball :cur-x)) (> y (ball :cur-y))) (+ (ball :cur-x) 50)
    (and (< x (ball :cur-x)) (< y (ball :cur-y))) (- (ball :cur-x) 50)
    :else (ball :cur-x)))

(defn target-y [ball x y]
  (cond
    (and (> x (ball :cur-x)) (< y (ball :cur-y))) (- (ball :cur-y) 50)
    (and (< x (ball :cur-x)) (> y (ball :cur-y))) (+ (ball :cur-y) 50)
    :else (ball :cur-y)))

(defn update-position [c t]
  (cond
    (> c t) (- c 5)
    (< c t) (+ c 5)
    :else c))

(defn move-balls [bs click-x click-y]
  (->> bs
    (mapv (fn [ball] (update-in ball [:target-x] #(target-x ball click-x click-y))))
    (mapv (fn [ball] (update-in ball [:target-y] #(target-y ball click-x click-y))))))

(defn update-positions [bs]
  (->> bs
    (mapv (fn [ball] (update-in ball [:cur-x] #(update-position (ball :cur-x) (ball :target-x)))))
    (mapv (fn [ball] (update-in ball [:cur-y] #(update-position (ball :cur-y) (ball :target-y)))))))


(defn do-frame []
  (.requestAnimationFrame js/window do-frame)
  (swap! ball-state update-positions)
  (.clearRect context 0 0 (. canvas -width) (. canvas -height))
  (doseq [b @ball-state] (drawBall b)))


(.addEventListener canvas "click"
  (fn [event]
    (let [x (. event -layerX)
          y (. event -layerY)
          balls-to-move (filter (fn [ball] (and (is-near? (ball :cur-x) x) (is-near? (ball :cur-y) y))) @ball-state)
          static-balls (remove (fn [ball] (and (is-near? (ball :cur-x) x) (is-near? (ball :cur-y) y))) @ball-state)]
      (when (= (count balls-to-move) 4)
        (reset! ball-state (concat static-balls (move-balls balls-to-move x y)))))
    false))

(do-frame)