(ns seven-guis.tasks.flight-booker
  (:require [reagent.core :as r]))

;;

(defn format-date [date]
  (let [opts #js {:day "2-digit" :month "2-digit" :year "numeric"}]
    (.toLocaleDateString date "en-US" opts)))

(defn reset-day! [date]
  (.setHours date 0 0 0 0)
  date)

(defn add-days! [date days]
  (.setDate date (+ days (.getDate date)))
  date)

(defn days-from-today [days]
  (-> (new js/Date)
    reset-day!
    (add-days! days)))

(defonce db
  (r/atom {:one-way (format-date (days-from-today 7))
           :roundtrip (format-date (days-from-today 14))
           :trip-type :one-way
           :flight-booked? false}))

(defn validate-date-string [string]
  (re-find #"^(\d{2})/(\d{2})/(\d{4})$" string))

(defn validate-inputs [{:keys [one-way roundtrip] :as db}]
  (let [first- (fnil first nil)]
   {:one-way (first- (validate-date-string one-way))
    :roundtrip (first- (validate-date-string roundtrip))}))

(defn one-way-date-valid? [one-way]
  (<= (reset-day! (new js/Date)) (new js/Date one-way)))

(defn roundtrip-date-valid? [one-way roundtrip]
  (<= (reset-day! (new js/Date one-way)) (new js/Date roundtrip)))

(defn dates-valid? [{:keys [one-way roundtrip] :as validated-inputs}]
  (and one-way
       roundtrip
       (one-way-date-valid? one-way)
       (roundtrip-date-valid? one-way roundtrip)))

(defn flight-booker []
  (let [validated-inputs (validate-inputs @db)]
    [:div.flight-booker
     [:select {:on-change
               #(swap! db assoc :trip-type (keyword (-> % .-target .-value)))}
      [:option {:value "one-way"} "One Way Flight"]
      [:option {:value "roundtrip"} "Roundtrip Flight"]]
     [:input {:type "text"
              :class (when-not (:one-way validated-inputs) "invalid")
              :on-change #(swap! db assoc :one-way (-> % .-target .-value))
              :value (:one-way @db)}]
     [:input {:type "text"
              :class (when (and
                            (not= :roundtrip (:trip-type @db))
                            (not (:roundtrip validated-inputs)))
                       "invalid")
              :disabled (not= :roundtrip (:trip-type @db))
              :on-change #(swap! db assoc :roundtrip (-> % .-target .-value))
              :value (:roundtrip @db)}]
     [:button {:disabled (not (dates-valid? validated-inputs))
               :on-click #(swap! db assoc :flight-booked? true)}
      "Book Flight"]
     (when (:flight-booked? @db)
       [:p (condp = (:trip-type @db)
             :one-way
             (str "You've booked a one-way flight on " (:one-way @db))
             :roundtrip
             (str "You've booked a roundtrip flight from "
                  (:one-way @db)
                  " to "
                  (:roundtrip @db)))])]))
