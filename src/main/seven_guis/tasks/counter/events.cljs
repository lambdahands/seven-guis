(ns seven-guis.tasks.counter.events
  (:require [seven-guis.tasks.counter.db :as db]
            [re-frame.core :as re]))

(defn increment-counter [state]
  (update state ::db/counter inc))

(re/reg-event-db ::increment-counter increment-counter)
