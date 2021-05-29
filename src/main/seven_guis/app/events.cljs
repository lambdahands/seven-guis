(ns seven-guis.app.events
  (:require [seven-guis.app.db :as app-db]
            [seven-guis.tasks.counter.db :as counter-db]
            [seven-guis.tasks.flight-booker.db :as flight-booker-db]
            [seven-guis.tasks.temperature-converter.db :as temperature-converter-db]
            [seven-guis.tasks.timer.db :as timer-db]
            [seven-guis.tasks.crud.db :as crud-db]
            [re-frame.core :as re]))

(defn initialize-state []
  (merge app-db/initial-state
         counter-db/initial-state
         temperature-converter-db/initial-state
         flight-booker-db/initial-state
         timer-db/initial-state
         crud-db/initial-state))

(defn select-task [db [_ task]]
  (assoc db :selected-task task))

(re/reg-event-db ::initialize-state initialize-state)
(re/reg-event-db ::select-task [(re/path ::app-db/app)] select-task)
