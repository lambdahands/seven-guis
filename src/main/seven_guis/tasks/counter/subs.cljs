(ns seven-guis.tasks.counter.subs
  (:require [seven-guis.tasks.counter.db :as db]
            [re-frame.core :as re]))

;; Subscriptions

(re/reg-sub ::counter ::db/counter)
