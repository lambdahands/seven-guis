(ns seven-guis.tasks.timer.views
  (:require [seven-guis.tasks.timer.subs :as s]
            [seven-guis.tasks.timer.events :as e]
            [re-frame.core :refer [dispatch subscribe]]))

(defn progress-bar-width [elapsed interval max-w]
  (let [interval (* 1000 interval)]
    (str (if-not (or (= 0 interval) (>= elapsed interval))
           (* max-w (/ elapsed interval))
           max-w)
         "px")))

(defn timer- []
  (let [elapsed @(subscribe [::s/elapsed])
        interval @(subscribe [::s/interval])]
   [:div
    [:div {:style {:width "500px" :height "10px" :background "#eee"}}
      [:div {:style {:width (progress-bar-width elapsed interval 500)
                     :height "10px"
                     :background "blue"
                     :transition "all 100ms linear"}}]]
    [:p (str "Elapsed time " (.toFixed (/ elapsed 1000) 1))]
    [:p (str "Duration " interval "s")]
    [:input {:type "range" :min 0 :max 60 :step 1
             :value interval
             :on-change #(dispatch [::e/handle-slide (-> % .-target .-value)])}]
    [:button {:on-click #(dispatch [::e/reset-timer])} "Reset"]]))

(def timer
  (with-meta timer- {:component-did-mount #(dispatch [::e/reset-timer])}))

