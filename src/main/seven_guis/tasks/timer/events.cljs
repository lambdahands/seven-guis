(ns seven-guis.tasks.timer.events
  (:require [seven-guis.tasks.timer.db :as db]
            [seven-guis.tasks.timer.subs :as s]
            [cljs.core.async :as a :refer [go-loop chan <! put! timeout]]
            [re-frame.core :as re]))

;;;; Coeffects

;; Helpers

(defn start-timer!
  "Start a new timer that returns two channels: a `timer-ch` to which the timer
   sends updates every `tick-ms` (50ms in this case), and an `interval-ch` whose
   purpose is to receive updates from outside of the timer with a number
   representing a new interval (duration) for the timer to count up to."
  [get-interval]
  (let [tick-ms 50
        timer-ch (chan)
        interval-ch (chan)]
    ; Loop with an initial elapsed time of 0
    (go-loop [i 0]
      ; If the current interval has yet to be reached
      (if (<= i (get-interval))
        ; Send a new tick to the timer-ch - if it received the tick...
        (when (put! timer-ch i)
          ; ...wait for one tick
          (<! (timeout tick-ms))
          ; ...restart the loop with an updated elapsed time
          (recur (+ i tick-ms)))
        ; Wait for a new interval from interval-ch - if the channel is open...
        (when (<! interval-ch)
          ; ...restart the loop with the current elapsed time
          (recur i))))
    [timer-ch interval-ch]))

;; Handlers

(defn reset-db-timer
  "Resets the current timer in the DB (if one exists) and dispatches a new timer
   to the DB."
  [db]
  ; Cleanup the current DB channels
  (when-let [timer-ch (:timer-ch db)]
    (a/close! timer-ch))
  (when-let [interval-ch (:interval-ch db)]
    (a/close! interval-ch))
  ; Start a new timer
  (let [interval (re/subscribe [::s/interval])
        [timer-ch interval-ch] (start-timer! #(* 1000 @interval))]
    ; Update the DB with the new channels
    (re/dispatch [::set-timer-ch timer-ch interval-ch])
    ; Read from the new timer-ch and dispatch the elapsed time to the DB
    (go-loop []
      (when-let [elapsed (<! timer-ch)]
        (re/dispatch [::set-elapsed elapsed])
        (recur)))))

(defn put-interval-ch [[interval-ch value]]
  (put! interval-ch value))

;; Registration

(re/reg-fx ::reset-db-timer reset-db-timer)
(re/reg-fx ::put-interval-ch put-interval-ch)

;;;; FX Events

;; Handlers

(defn reset-timer [cofx]
  {::reset-db-timer (:db cofx)})

(defn handle-slide
  "Update the DB and the current timer's interval-ch with the new slider value"
  [{:keys [db]} [_ interval]]
  {:db (assoc db :interval interval)
   ::put-interval-ch [(:interval-ch db) interval]})

;; Registration
; Use re-frame.core/path to assign the `db` parameter in event handlers to the
; ::db/timer key in the global app DB.

(re/reg-event-fx ::reset-timer  [(re/path ::db/timer)] reset-timer)
(re/reg-event-fx ::handle-slide [(re/path ::db/timer)] handle-slide)

;;;; DB Events

;; Handlers

(defn set-timer-ch [db [_ timer-ch interval-ch]]
  (assoc db :timer-ch timer-ch :interval-ch interval-ch))

(defn set-elapsed [db [_ elapsed]]
  (assoc db :elapsed elapsed))

;; Registration
; Use re-frame.core/path to assign the `db` parameter in event handlers to the
; ::db/timer key in the global app DB.

(re/reg-event-db ::set-timer-ch [(re/path ::db/timer)] set-timer-ch)
(re/reg-event-db ::set-elapsed  [(re/path ::db/timer)] set-elapsed)
