(ns seven-guis.tasks.temperature-converter.views
  (:require [seven-guis.tasks.temperature-converter.events :as e]
            [seven-guis.tasks.temperature-converter.subs :as s]
            [re-frame.core :refer [dispatch subscribe]]))

(defn handle-change [e from to]
  (dispatch [::e/handle-conversion (-> e .-target .-value) from to]))

(defn temperature-converter []
  (let [fahrenheit @(subscribe [::s/fahrenheit])
        celcius    @(subscribe [::s/celcius])]
    [:div.temperature-converter
     [:div.temperature-converter__input-group
      [:label.label "Fahrenheit"]
      [:input.input
       {:on-change #(handle-change % :fahrenheit :celcius)
        :value fahrenheit}]]
     [:h2.temperature-converter__equals "="]
     [:div.temperature-converter__input-group
      [:label.label "Celcius"]
      [:input.input
       {:on-change #(handle-change % :celcius :fahrenheit)
        :value celcius}]]]))

