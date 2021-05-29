(ns seven-guis.tasks.counter.subs
  (:require [seven-guis.tasks.counter.db :as db]
            [re-frame.core :as re]))

(re/reg-sub ::counter #(::db/counter %))
