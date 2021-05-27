(ns seven-guis.tasks.counter
  (:require [reagent.core :as r]))

(defonce app-state (r/atom {:counter 0}))

(defn increment-counter [state]
  (update state :counter inc))

(defn counter []
  [:div
   [:input {:disabled true :value (:counter @app-state)}]
   [:button {:on-click #(swap! app-state increment-counter)}
    "Count"]])
