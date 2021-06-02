(ns seven-guis.tasks.flight-booker.events
  (:require [seven-guis.tasks.flight-booker.db :as db]
            [re-frame.core :as re]))

;;;; DB Events

;; Handlers

(defn update-trip-type [db [_ trip-type]]
  (assoc db :trip-type (keyword trip-type) :flight-booked? false))

(defn update-one-way [db [_ value]]
  (assoc db :one-way value :flight-booked? false))

(defn update-roundtrip [db [_ value]]
  (assoc db :roundtrip value :flight-booked? false))

(defn book-flight [db]
  (assoc db :flight-booked? true))

(defn confirm-booking []
  (::db/flight-booker db/initial-state))

;; Registration
; Use re-frame.core/path to assign the `db` parameter in event handlers to the
; ::db/flight-booker key in the global app DB.

(re/reg-event-db ::update-trip-type [(re/path ::db/flight-booker)] update-trip-type)
(re/reg-event-db ::update-one-way   [(re/path ::db/flight-booker)] update-one-way)
(re/reg-event-db ::update-roundtrip [(re/path ::db/flight-booker)] update-roundtrip)
(re/reg-event-db ::book-flight      [(re/path ::db/flight-booker)] book-flight)
(re/reg-event-db ::confirm-booking  [(re/path ::db/flight-booker)] confirm-booking)
