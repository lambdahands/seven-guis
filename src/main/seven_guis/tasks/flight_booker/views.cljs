(ns seven-guis.tasks.flight-booker.views
  (:require [seven-guis.tasks.flight-booker.events :as e]
            [seven-guis.tasks.flight-booker.subs :as s]
            [re-frame.core :refer [dispatch subscribe]]))

(defn flight-booker-description []
  [:p "The task is to build a frame containing a combobox "
   [:em "C"] " with the two options “one-way flight” and “return flight”, two textfields "
   [:em "T" [:sub "1"]] " and "
   [:em "T" [:sub "2"]] " representing the start and return date, respectively, and a button "
   [:em "B"] " for submitting the selected flight. "
   [:em "T" [:sub "2"]] " is enabled iff "
   [:em "C"] "’s value is “return flight”. When "
   [:em "C"] " has the value “return flight” and "
   [:em "T" [:sub "2"]] "’s date is strictly before "
   [:em "T" [:sub "1"]] "’s then "
   [:em "B"] " is disabled. When a non-disabled textfield "
   [:em "T"] " has an ill-formatted date then "
   [:em "T"] " is colored red and "
   [:em "B"] " is disabled. When clicking "
   [:em "B"] " a message is displayed informing the user of his selection (e.g. “You have booked a one-way flight on 04.04.2014.”). Initially, "
   [:em "C"] " has the value “one-way flight” and "
   [:em "T" [:sub "1"]] " as well as "
   [:em "T" [:sub "2"]] " have the same (arbitrary) date (it is implied that "
   [:em "T" [:sub "2"]] " is disabled)."])

(defn flight-booker []
  (let [trip-type      @(subscribe [::s/trip-type])
        one-way        @(subscribe [::s/one-way])
        roundtrip      @(subscribe [::s/roundtrip])
        flight-booked? @(subscribe [::s/flight-booked?])
        valid-inputs   @(subscribe [::s/valid-inputs])
        dates-valid?   @(subscribe [::s/dates-valid? valid-inputs])]
    [:div.flight-booker
     [:div.flight-booker__input-group
      [:label.label "Trip Type"]
      [:div.select-wrapper
       [:select.select
        {:on-change #(dispatch [::e/update-trip-type (-> % .-target .-value)])}
        [:option {:value "one-way"} "One Way Flight"]
        [:option {:value "roundtrip"} "Roundtrip Flight"]]]]
     [:div.flight-booker__input-group
      [:label.label "Departure Date (MM/DD/YYYY)"]
      [:input.input.flight-booker__input
       {:type "text"
        :class (when-not (:one-way valid-inputs) "invalid")
        :on-change #(dispatch [::e/update-one-way (-> % .-target .-value)])
        :value one-way}]]
     [:div.flight-booker__input-group
      [:label.label "Return Date (MM/DD/YYYY)"]
      [:input.input.flight-booker__input
       {:type "text"
        :class (when (and
                      (not= :roundtrip trip-type)
                      (not (:roundtrip valid-inputs)))
                 "invalid")
        :disabled (not= :roundtrip trip-type)
        :on-change #(dispatch [::e/update-roundtrip (-> % .-target .-value)])
        :value roundtrip}]]
     [:button.button.button--confirm.flight-booker__button
      {:disabled (not dates-valid?)
       :on-click #(dispatch [::e/book-flight])}
      "Book Flight"]
     (when flight-booked?
       [:p (condp = trip-type
             :one-way
             (str "You've booked a one-way flight on " one-way)
             :roundtrip
             (str "You've booked a roundtrip flight from "
                  one-way " to " roundtrip))])]))
