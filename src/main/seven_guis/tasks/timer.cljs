(ns seven-guis.tasks.timer
  (:require [cljs.core.async :as a :refer [go go-loop chan <! >! put! timeout]]
            [reagent.core :as r]))

(defonce db (r/atom {:interval 0
                     :elapsed 0
                     :interval-ch (chan)
                     :timer-ch nil}))

(defn start-timer! [get-interval interval-ch]
  (let [tick-ms 100
        timer-ch (chan)]
    (go-loop [i 0]
      (if (and (>= (get-interval) i)
               (put! timer-ch i))
        (do (<! (timeout tick-ms))
            (recur (+ i tick-ms)))
        (do (<! interval-ch)
            (recur i))))
    timer-ch))

(defn start-db-timer! [db]
  (let [get-interval #(* 1000 (:interval @db))
        interval-ch (:interval-ch @db)]
    (start-timer! get-interval interval-ch)))

(defn reset-db-timer! [db]
  (when-let [prev-timer (:timer-ch @db)]
    (a/close! prev-timer))
  (let [timer-ch (start-db-timer! db)]
    (swap! db assoc :timer-ch timer-ch)
    (go-loop []
      (when-let [elapsed (<! timer-ch)]
        (swap! db assoc :elapsed elapsed)
        (recur)))))

(defn on-slide! [e db]
  (let [interval (-> e .-target .-valueAsNumber)]
    (swap! db assoc :interval interval)
    (put! (:interval-ch @db) interval)))

(defn progress-bar-width [db max-w]
  (let [elapsed (:elapsed @db)
        interval (* 1000 (:interval @db))]
    (str (if-not (or (= 0 interval) (>= elapsed interval))
           (* max-w (/ elapsed interval))
           max-w)
         "px")))

(defn timer- []
  [:div
   [:div {:style {:width "500px" :height "10px" :background "#eee"}}
     [:div {:style {:width (progress-bar-width db 500)
                    :height "10px"
                    :background "blue"
                    :transition "all 100ms linear"}}]]
   [:p (str "Elapsed time " (.toFixed (/ (:elapsed @db) 1000) 1))]
   [:p (str "Duration " (:interval @db) "s")]
   [:input {:type "range" :min 0 :max 60 :step 1
            :value (:interval @db)
            :on-change #(on-slide! % db)}]
   [:button {:on-click #(reset-db-timer! db)} "Reset"]])

(def timer
  (with-meta timer- {:component-did-mount #(reset-db-timer! db)}))
