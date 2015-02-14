(ns bepallod.core)

(enable-console-print!)

(def canvas (.getElementById js/document "canvas"))
(def context (.getContext canvas "2d"))

(def colors ["#ffff00" "#ff00ff" "#00ffff" "#ff7f7f" "#7fff7f" "#7f7fff" "#aaaaaa"])

(def ball-diameter 50)
(def ball-radius 25)

(def balls
  (->>
    (for [x (range ball-radius 425 ball-diameter)
          y (range ball-radius 425 ball-diameter)]
      [x y x y 25 (rand-nth colors)])
    (mapv #(zipmap [:cur-x :cur-y :target-x :target-y :radius :color] %))
    (map-indexed #(into %2 {:index %1}))))

(def ball-state (atom balls))

(def knobs
  (for [x (range ball-diameter 400 ball-diameter)
        y (range ball-diameter 400 ball-diameter)]
    {:cur-x x :cur-y y :radius 7 :color "#2a2a2a"}))

(defn gradient-value []
  (-> (-(.getTime (js/Date.)))
    (mod 4000)
    (/ 2000)
    (- 1)
    (Math/abs)))

(defn drawBall [ball]
  (let [radius (ball :radius)]
    (.save context)
    (.translate context (ball :cur-x) (ball :cur-y))
    (set! (.-lineWidth context) 2)
    (when (= radius ball-radius)
      (let [gradient (.createLinearGradient context (- radius) (- radius) radius radius)]
        (.addColorStop gradient 0 (ball :color))
        (.addColorStop gradient (gradient-value) "#d5d5d5")
        (.addColorStop gradient 1 (ball :color))
        (set! (.-fillStyle context) gradient)))
    (.beginPath context)
    (.arc context 0 0 radius 0 (* Math/PI 2) true)
    (.closePath context)
    (.fill context)
    (.stroke context)
    (.restore context)))

(defn is-near? [a b]
  (< (Math/abs (- a b)) 40))

(defn target-x [ball x y]
  (cond
    (and (> x (ball :cur-x)) (> y (ball :cur-y))) (+ (ball :cur-x) ball-diameter)
    (and (< x (ball :cur-x)) (< y (ball :cur-y))) (- (ball :cur-x) ball-diameter)
    :else (ball :cur-x)))

(defn target-y [ball x y]
  (cond
    (and (> x (ball :cur-x)) (< y (ball :cur-y))) (- (ball :cur-y) ball-diameter)
    (and (< x (ball :cur-x)) (> y (ball :cur-y))) (+ (ball :cur-y) ball-diameter)
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
    (mapv (fn [ball] (update-in ball [:cur-y] #(update-position (ball :cur-y) (ball :target-y)))))
    (sort-by (juxt :cur-y :cur-x))))

(defn all-same-color? [s]
  (let [clrs (map :color s)]
    (if (= (count (distinct clrs)) 1)
      (map :index s)
      ())))

(defn find-matches [rows]
  (->> rows
    (mapv #(partition 3 1 %))
    (mapv #(mapv all-same-color? %))
    (flatten)
    (distinct)))

(defn match-index [ball matches]
  (some #(= % (ball :index)) matches))

(defn is-above [ball match-ball]
  (and
    (= (match-ball :cur-x) (ball :cur-x))
    (> (match-ball :cur-y) (ball :cur-y))))

(defn number-of-matches [ball matches]
  (->> @ball-state
    (filter #(match-index % matches))
    (filter #(is-above ball %))
    (count)))

(defn new-position [ball matches]
  (* (number-of-matches ball matches) ball-diameter))

(defn reset-matching-ball [ball matches]
  (if (match-index ball matches)
    (-> ball
      (assoc-in [:cur-y] (- ball-radius))
      (assoc-in [:target-y] (+ ball-radius (new-position ball matches)))
      (assoc-in [:color] (rand-nth colors)))
    ball))

(defn move-ball-above [ball matches]
  (assoc-in ball [:target-y] (+ (ball :target-y) (new-position ball matches))))

(defn check-board [bs]
  (let [clrs (map #(select-keys % [:color :index]) bs)
        rows (partition 8 clrs)
        cols (map #(take-nth 8 (drop % clrs)) (range 8))
        matches (find-matches (concat rows cols))]
    (->> bs
      (mapv #(move-ball-above % matches))
      (mapv #(reset-matching-ball % matches)))))

(defn do-frame []
  (.requestAnimationFrame js/window do-frame)
  (swap! ball-state update-positions)
  (.clearRect context 0 0 (.-width canvas) (.-height canvas))
  (when (every? #(and
                   (= (% :cur-y) (% :target-y))
                   (= (% :cur-x) (% :target-x))) @ball-state)
    (swap! ball-state check-board))
  (doseq [b @ball-state] (drawBall b))
  (doseq [k knobs] (drawBall k)))

(defn ^:export main []
  (.addEventListener canvas "click"
    (fn [event]
      (let [x (.-layerX event)
            y (.-layerY event)
            to-move? (fn [ball] (and (is-near? (ball :cur-x) x) (is-near? (ball :cur-y) y)))
            balls-to-move (filter to-move? @ball-state)
            static-balls  (remove to-move? @ball-state)]
        (when (= (count balls-to-move) 4)
          (reset! ball-state (concat static-balls (move-balls balls-to-move x y)))))
      false))

  (do-frame))