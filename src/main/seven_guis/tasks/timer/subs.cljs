(ns seven-guis.tasks.timer.subs
  (:require [seven-guis.tasks.timer.db :as db]
            [re-frame.core :as re]))

(re/reg-sub ::interval #(get-in % [::db/timer :interval]))
(re/reg-sub ::elapsed  #(get-in % [::db/timer :elapsed]))
