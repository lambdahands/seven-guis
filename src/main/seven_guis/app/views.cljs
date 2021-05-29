(ns seven-guis.app.views
  (:require [seven-guis.app.subs :as s]
            [seven-guis.app.events :as e]
            [seven-guis.tasks.counter.views :refer [counter]]
            [seven-guis.tasks.flight-booker.views :refer [flight-booker]]
            [seven-guis.tasks.temperature-converter.views :refer [temperature-converter]]
            [seven-guis.tasks.timer.views :refer [timer]]
            [seven-guis.tasks.crud.views :refer [crud]]
            [re-frame.core :refer [dispatch subscribe]]))

(def tabs
  [[1 :counter "Counter"]
   [2 :temperature-converter "Temperature Converter"]
   [3 :flight-booker "Flight Booker"]
   [4 :timer "Timer"]
   [5 :crud "CRUD"]])

(defn app []
  (let [task @(subscribe [::s/task])]
    [:div
     [:div
      (for [[id task title] tabs]
        [:a {:key id :on-click #(dispatch [::e/select-task task])}
         title])]
     (condp = task
       :counter               [counter]
       :flight-booker         [flight-booker]
       :temperature-converter [temperature-converter]
       :timer                 [timer]
       :crud                  [crud])]))
