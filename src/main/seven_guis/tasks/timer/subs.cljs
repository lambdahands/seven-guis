(ns seven-guis.tasks.timer.subs
  (:require [seven-guis.tasks.timer.db :as db]
            [re-frame.core :as re]))

;;;; DB Subscriptions

;; Registration

(re/reg-sub ::interval #(get-in % [::db/timer :interval]))
(re/reg-sub ::elapsed  #(get-in % [::db/timer :elapsed]))
(re/reg-sub ::timer-ch  #(get-in % [::db/timer :timer-ch]))
