(ns seven-guis.tasks.temperature-converter.views
  (:require [seven-guis.tasks.temperature-converter.events :as e]
            [seven-guis.tasks.temperature-converter.subs :as s]
            [re-frame.core :refer [dispatch subscribe]]))

(defn handle-change [e from to]
  (dispatch [::e/handle-conversion (-> e .-target .-value) from to]))

(defn temperature-converter []
  (let [fahrenheit @(subscribe [::s/fahrenheit])
        celcius    @(subscribe [::s/celcius])]
    [:div
     [:input {:on-change #(handle-change % :fahrenheit :celcius)
              :value fahrenheit}]
     [:input {:on-change #(handle-change % :celcius :fahrenheit)
              :value celcius}]]))

