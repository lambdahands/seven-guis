(ns seven-guis.tasks.counter.events
  (:require [seven-guis.tasks.counter.db :as db]
            [re-frame.core :as re]))

;;;; DB Events

;; Handlers

(defn increment-counter [state]
  (update state ::db/counter inc))

;; Registration

(re/reg-event-db ::increment-counter increment-counter)
