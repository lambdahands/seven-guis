(ns seven-guis.tasks.flight-booker.subs
  (:require [seven-guis.tasks.flight-booker.db :as db]
            [seven-guis.tasks.flight-booker.helpers :refer [reset-day!]]
            [re-frame.core :as re]))

;;;; DB Subscriptions

;; Helpers

(defn validate-date-string [string]
  (re-find #"^(\d{2})/(\d{2})/(\d{4})$" string))

(defn one-way-date-valid? [one-way]
  (<= (reset-day! (new js/Date)) (new js/Date one-way)))

(defn roundtrip-date-valid? [one-way roundtrip]
  (<= (reset-day! (new js/Date one-way)) (new js/Date roundtrip)))

;; Handlers

(defn validate-inputs [{:keys [one-way roundtrip]}]
  (let [first- (fnil first nil)]
   {:one-way (first- (validate-date-string one-way))
    :roundtrip (first- (validate-date-string roundtrip))}))

(defn dates-valid? [db [_ {:keys [one-way roundtrip]}]]
  (and one-way
       roundtrip
       (one-way-date-valid? one-way)
       (or (= (:trip-type db) :one-way)
           (roundtrip-date-valid? one-way roundtrip))))

;; Registration


(re/reg-sub ::valid-inputs   #(validate-inputs (::db/flight-booker %)))
(re/reg-sub ::dates-valid?   #(dates-valid? (::db/flight-booker %1) %2))
(re/reg-sub ::trip-type      #(get-in % [::db/flight-booker :trip-type]))
(re/reg-sub ::one-way        #(get-in % [::db/flight-booker :one-way]))
(re/reg-sub ::roundtrip      #(get-in % [::db/flight-booker :roundtrip]))
(re/reg-sub ::flight-booked? #(get-in % [::db/flight-booker :flight-booked?]))
