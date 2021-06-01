(ns seven-guis.tasks.temperature-converter.subs
  (:require [seven-guis.tasks.temperature-converter.db :as db]
            [re-frame.core :as re]))

;;;; DB Subscriptions

;; Registration

(re/reg-sub ::fahrenheit #(get-in % [::db/temperature-converter :fahrenheit]))
(re/reg-sub ::celcius    #(get-in % [::db/temperature-converter :celcius]))
