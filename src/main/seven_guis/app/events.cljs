(ns seven-guis.app.events
  (:require [seven-guis.app.db :as app-db]
            [seven-guis.tasks.counter.db :as counter-db]
            [seven-guis.tasks.flight-booker.db :as flight-booker-db]
            [seven-guis.tasks.temperature-converter.db :as temperature-converter-db]
            [seven-guis.tasks.timer.db :as timer-db]
            [seven-guis.tasks.crud.db :as crud-db]
            [seven-guis.tasks.timer.events :as timer-events]
            [re-frame.core :as re]))

(def initial-task-states
  {:app                   app-db/initial-state
   :counter               counter-db/initial-state
   :temperature-converter temperature-converter-db/initial-state
   :flight-booker         flight-booker-db/initial-state
   :timer                 timer-db/initial-state
   :crud                  crud-db/initial-state})

(defn initialize-state []
  (apply merge (vals initial-task-states)))

(defn select-task [db [_ task]]
  (assoc db :selected-task task))

(defn reset-task-state [{:keys [db]} [_ task]]
  (let [state (get initial-task-states task)
        cofx {:db (merge db state)}]
    (condp = task
      :timer (assoc cofx ::timer-events/reset-db-timer state)
      cofx)))

(re/reg-event-db ::initialize-state initialize-state)
(re/reg-event-db ::select-task [(re/path ::app-db/app)] select-task)
(re/reg-event-fx ::reset-task-state reset-task-state)
