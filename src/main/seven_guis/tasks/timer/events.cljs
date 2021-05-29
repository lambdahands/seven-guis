(ns seven-guis.tasks.timer.events
  (:require [seven-guis.tasks.timer.db :as db]
            [seven-guis.tasks.timer.subs :as s]
            [cljs.core.async :as a :refer [go go-loop chan <! >! put! timeout]]
            [re-frame.core :as re]))

(def interval-ch (chan))

(defn start-timer! [get-interval interval-ch]
  (let [tick-ms 100
        timer-ch (chan)]
    (go-loop [i 0]
      (if (and (>= (get-interval) i)
               (put! timer-ch i))
        (do (<! (timeout tick-ms))
            (recur (+ i tick-ms)))
        (do (<! interval-ch)
            (recur i))))
    timer-ch))

(defn start-db-timer! []
  (let [interval (re/subscribe [::s/interval])]
    (start-timer! #(* 1000 @interval) interval-ch)))

(defn reset-db-timer [db]
  (when-let [prev-timer (:timer-ch db)]
    (a/close! prev-timer))
  (let [timer-ch (start-db-timer!)]
    (re/dispatch [::set-timer-ch  timer-ch])
    (go-loop []
      (when-let [elapsed (<! timer-ch)]
        (re/dispatch [::set-elapsed elapsed])
        (recur)))))

(re/reg-fx ::reset-db-timer reset-db-timer)

(defn put-interval-ch [v]
  (put! interval-ch v))

(re/reg-fx ::put-interval-ch put-interval-ch)

(defn set-timer-ch [db [_ timer-ch]]
  (assoc db :timer-ch timer-ch))

(defn set-elapsed [db [_ timer-ch]]
  (assoc db :elapsed timer-ch))

(re/reg-event-db ::set-timer-ch [(re/path ::db/timer)] set-timer-ch)
(re/reg-event-db ::set-elapsed  [(re/path ::db/timer)] set-elapsed)

(re/reg-event-fx ::reset-timer [(re/path ::db/timer)]
                 (fn [cofx]
                   {::reset-db-timer (:db cofx)}))

(re/reg-event-fx ::handle-slide [(re/path ::db/timer)]
                 (fn [cofx [_ interval]]
                   {:db (assoc (:db cofx) :interval interval)
                    ::put-interval-ch interval}))
