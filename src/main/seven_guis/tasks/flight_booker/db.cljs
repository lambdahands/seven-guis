(ns seven-guis.tasks.flight-booker.db
  (:require [seven-guis.tasks.flight-booker.helpers
             :refer [format-date days-from-today]]))

(def initial-state
  {::flight-booker {:one-way (format-date (days-from-today 7))
                    :roundtrip (format-date (days-from-today 14))
                    :trip-type :one-way
                    :flight-booked? false}})

