(ns seven-guis.tasks.temperature-converter.views
  (:require [seven-guis.tasks.temperature-converter.events :as e]
            [seven-guis.tasks.temperature-converter.subs :as s]
            [re-frame.core :refer [dispatch subscribe]]))

; Description text from https://eugenkiss.github.io/7guis/tasks#temp
; HTML converted with http://html2hiccup.buttercloud.com/
(defn temperature-converter-description []
  [:p "The task is to build a frame containing two textfields "
   [:em "T" [:sub "C"]] " and "
   [:em "T" [:sub "F"]] " representing the temperature in Celsius and Fahrenheit, respectively. Initially, both "
   [:em "T" [:sub "C"]] " and "
   [:em "T" [:sub "F"]] " are empty. When the user enters a numerical value into "
   [:em "T" [:sub "C"]] " the corresponding value in "
   [:em "T" [:sub "F"]] " is automatically updated and vice versa. When the user enters a non-numerical string into "
   [:em "T" [:sub "C"]] " the value in "
   [:em "T" [:sub "F"]] " is "
   [:em "not"] " updated and vice versa. The formula for converting a temperature "
   [:em "C"] " in Celsius into a temperature "
   [:em "F"] " in Fahrenheit is "
   [:em "C = (F - 32) * (5/9)"] " and the dual direction is "
   [:em "F = C * (9/5) + 32"] "."])

;; Helpers

(defn handle-change [e from to]
  (dispatch [::e/handle-conversion (-> e .-target .-value) from to]))

;; Main view

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
