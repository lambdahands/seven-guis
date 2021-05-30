(ns seven-guis.tasks.counter.views
  (:require [seven-guis.tasks.counter.events :as e]
            [seven-guis.tasks.counter.subs :as s]
            [re-frame.core :refer [dispatch subscribe]]))

(defn counter []
  (let [counter @(subscribe [::s/counter])]
    [:div.counter
     [:input.input.counter__input {:disabled true :value counter}]
     [:button.button.button--confirm.counter__button.
      {:on-click #(dispatch [::e/increment-counter])}
      "Count"]]))
