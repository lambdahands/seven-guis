(ns seven-guis.tasks.timer.views
  (:require [seven-guis.tasks.timer.subs :as s]
            [seven-guis.tasks.timer.events :as e]
            [re-frame.core :refer [dispatch subscribe]]))

(defn progress-bar-width [elapsed interval-ms]
  (str (if-not (or (= 0 interval-ms) (>= elapsed interval-ms))
         (* 100 (/ elapsed interval-ms))
         100)
       "%"))

(defn timer- []
  (let [elapsed @(subscribe [::s/elapsed])
        interval @(subscribe [::s/interval])
        interval-ms (* 1000 interval)
        done (<= interval-ms elapsed)]
   [:div.timer
    [:div.timer__progress-bar-wrapper
     (when-not (= 0 elapsed)
      [:div.timer__progress-bar
       {:class (when done "timer__progress-bar--done")
        :style {:width (progress-bar-width elapsed interval-ms)}}])]
    [:div.timer__elapsed
     [:p.timer__elapsed-title "Time elapsed:"]
     [:h1.timer__elapsed-value
      {:class (when done "timer__elapsed-value--done")}
      (str (.toFixed (/ elapsed 1000) 1) "s")]]
    [:div.timer__controls
     [:p.timer__controls-duration (str "Duration: " interval "s")]
     [:div.timer__controls-slider
      [:input
       {:type "range" :min 0 :max 60 :step 1
        :value interval
        :on-change #(let [value (-> % .-target .-value)]
                      (dispatch [::e/handle-slide value]))}]]
     [:button.button {:on-click #(dispatch [::e/reset-timer])}
      "Reset"]]]))

(def timer
  (with-meta
   timer-
   {:component-did-mount #(when-not @(subscribe [::s/timer-ch])
                            (dispatch [::e/reset-timer]))}))

