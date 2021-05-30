(ns seven-guis.tasks.counter.views
  (:require [seven-guis.tasks.counter.events :as e]
            [seven-guis.tasks.counter.subs :as s]
            [re-frame.core :refer [dispatch subscribe]]))

(defn counter-description []
  [:p "The task is to build a frame containing a label or read-only textfield "
    [:em "T"] " and a button "
    [:em "B"] ". Initially, the value in "
    [:em "T"] " is “0” and each click of "
    [:em "B"] " increases the value in "
    [:em "T"] " by one."])

(defn counter []
  (let [counter @(subscribe [::s/counter])]
    [:div.counter
     [:input.input.counter__input {:disabled true :value counter}]
     [:button.button.button--confirm.counter__button.
      {:on-click #(dispatch [::e/increment-counter])}
      "Count"]]))
