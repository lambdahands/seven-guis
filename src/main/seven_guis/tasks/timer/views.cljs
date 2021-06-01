(ns seven-guis.tasks.timer.views
  (:require [seven-guis.tasks.timer.subs :as s]
            [seven-guis.tasks.timer.events :as e]
            [re-frame.core :refer [dispatch subscribe]]))

; Description text from https://eugenkiss.github.io/7guis/tasks#timer
; HTML converted with http://html2hiccup.buttercloud.com/
(defn timer-description []
  [:p "The task is to build a frame containing a gauge "
   [:em "G"] " for the elapsed time "
   [:em "e"] ", a label which shows the elapsed time as a numerical value, a slider "
   [:em "S"] " by which the duration "
   [:em "d"] " of the timer can be adjusted while the timer is running and a reset button "
   [:em "R"] ". Adjusting "
   [:em "S"] " must immediately reflect on "
   [:em "d"] " and not only when "
   [:em "S"] " is released. It follows that while moving "
   [:em "S"] " the filled amount of "
   [:em "G"] " will (usually) change immediately. When "
   [:em "e ≥ d"] " is true then the timer stops (and "
   [:em "G"] " will be full). If, thereafter, "
   [:em "d"] " is increased such that "
   [:em "d > e"] " will be true then the timer restarts to tick until "
   [:em "e ≥ d"] " is true again. Clicking "
   [:em "R"] " will reset "
   [:em "e"] " to zero."])

;; Helpers

(defn progress-bar-width [elapsed interval-ms]
  (str (if-not (or (= 0 interval-ms) (>= elapsed interval-ms))
         (* 100 (/ elapsed interval-ms))
         100)
       "%"))

;; Main view

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
     [:button.button.button--confirm
      {:on-click #(dispatch [::e/reset-timer])}
      "Reset"]]]))

(def timer
  (let [timer-ch (subscribe [::s/timer-ch])]
    (with-meta
     timer-
     {:component-did-mount #(when-not @timer-ch
                              (dispatch [::e/reset-timer]))})))

