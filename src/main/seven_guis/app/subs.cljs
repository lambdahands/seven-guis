(ns seven-guis.app.subs
  (:require [seven-guis.app.db :as db]
            [re-frame.core :as re]))

(re/reg-sub ::task #(get-in % [::db/app :selected-task]))
