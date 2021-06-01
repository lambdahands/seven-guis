(ns seven-guis.app.events
  (:require [seven-guis.app.db :as app-db]
            [seven-guis.tasks.counter.db :as counter-db]
            [seven-guis.tasks.flight-booker.db :as flight-booker-db]
            [seven-guis.tasks.temperature-converter.db :as temperature-converter-db]
            [seven-guis.tasks.timer.db :as timer-db]
            [seven-guis.tasks.crud.db :as crud-db]
            [seven-guis.tasks.timer.events :as timer-events]
            [re-frame.core :as re]))

; Mapping of task states in order to merge the values into one to construct our
; application DB on start. The keys allow us to reference each state by a
; non-namespaced keyword. Each DB contains a namespaced keyword at its root in
; order to explicitly distinguish between tasks.
(def initial-task-states
  {:app                   app-db/initial-state
   :counter               counter-db/initial-state
   :temperature-converter temperature-converter-db/initial-state
   :flight-booker         flight-booker-db/initial-state
   :timer                 timer-db/initial-state
   :crud                  crud-db/initial-state})

;;;; DB Events

;; Handlers

; Use the values from our initial task state to build a single initial state.
(defn initialize-state []
  (apply merge (vals initial-task-states)))

(defn select-task [db [_ task]]
  (assoc db :selected-task task :menu-open? false))

(defn open-menu [db]
  (assoc db :menu-open? true))

(defn close-menu [db]
  (assoc db :menu-open? false))

;; Registration

(re/reg-event-db ::initialize-state initialize-state)
(re/reg-event-db ::select-task [(re/path ::app-db/app)] select-task)
(re/reg-event-db ::open-menu   [(re/path ::app-db/app)] open-menu)
(re/reg-event-db ::close-menu  [(re/path ::app-db/app)] close-menu)

;; FX Events

;; Handlers

(defn reset-task-state
  "Reset the state of the given task. The `:timer` task must also re-initialize
   an extra piece of state for its behavior to be fully reset properly. This isa?
   done by dispatching through cofx, keeping this FX Event pure."
  [{:keys [db]} [_ task]]
  (let [state (get initial-task-states task)
        cofx {:db (merge db state)}]
    (condp = task
      :timer (assoc cofx ::timer-events/reset-db-timer state)
      cofx)))

;; Registration

(re/reg-event-fx ::reset-task-state reset-task-state)
