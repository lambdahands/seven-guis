(ns seven-guis.tasks.timer.events
  (:require [seven-guis.tasks.timer.db :as db]
            [seven-guis.tasks.timer.subs :as s]
            [cljs.core.async :as a :refer [go-loop chan <! put! timeout]]
            [re-frame.core :as re]))

;; Coeffects

(defn start-timer! [get-interval]
  (let [tick-ms 50
        timer-ch (chan)
        interval-ch (chan)]
    (go-loop [i 0]
      (if (<= i (get-interval))
        (do (put! timer-ch i)
            (<! (timeout tick-ms))
            (recur (+ i tick-ms)))
        (do (<! interval-ch)
            (recur i))))
    [timer-ch interval-ch]))

(defn start-db-timer! []
  (let [interval (re/subscribe [::s/interval])]
    (start-timer! #(* 1000 @interval))))

(defn reset-db-timer [db]
  (when-let [prev-timer (:timer-ch db)]
    (a/close! prev-timer))
  (let [[timer-ch interval-ch] (start-db-timer!)]
    (re/dispatch [::set-timer-ch timer-ch interval-ch])
    (go-loop []
      (when-let [elapsed (<! timer-ch)]
        (re/dispatch [::set-elapsed elapsed])
        (recur)))))

(defn put-interval-ch [[interval-ch value]]
  (put! interval-ch value))

(re/reg-fx ::reset-db-timer reset-db-timer)
(re/reg-fx ::put-interval-ch put-interval-ch)

;; FX Events

(defn reset-timer [cofx]
  {::reset-db-timer (:db cofx)})

(defn handle-slide [{:keys [db]} [_ interval]]
  {:db (assoc db :interval interval)
   ::put-interval-ch [(:interval-ch db) interval]})

(re/reg-event-fx ::reset-timer  [(re/path ::db/timer)] reset-timer)
(re/reg-event-fx ::handle-slide [(re/path ::db/timer)] handle-slide)

;; DB Events

(defn set-timer-ch [db [_ timer-ch interval-ch]]
  (assoc db :timer-ch timer-ch :interval-ch interval-ch))

(defn set-elapsed [db [_ elapsed]]
  (assoc db :elapsed elapsed))

(re/reg-event-db ::set-timer-ch [(re/path ::db/timer)] set-timer-ch)
(re/reg-event-db ::set-elapsed  [(re/path ::db/timer)] set-elapsed)
