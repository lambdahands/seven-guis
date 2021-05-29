(ns seven-guis.tasks.temperature-converter
  (:require [reagent.core :as r]))

;; State

(def initial-state {:fahrenheit "32" :celcius "0"})

(defonce db (r/atom initial-state))

;; Conversion Functions

(defn celcius->fahrenheit [num]
  (+ 32 (* num (/ 9 5))))

(defn fahrenheit->celcius [num]
  (* (- num 32) (/ 9 5)))

(def conversion-fns
  {[:fahrenheit :celcius] fahrenheit->celcius
   [:celcius :fahrenheit] celcius->fahrenheit})

;; Validators

(defn input-edge-case? [input]
  (or (= "-" input)
      (re-find #".*e$" input)
      (re-find #".*e\+$" input)
      (re-find #"^e.*" input)
      (re-find #"^e\+.*" input)))

;; Helpers

(defn str->number [string]
  (let [num (js/Number string)]
    (when (and (not (js/Number.isNaN num))
               (js/Number.isFinite num)
               (number? num))
      num)))

(defn set-conversion [db input [from to]]
  (let [num (str->number input)]
    (cond num (-> db
                (assoc from input)
                (assoc to (str ((get conversion-fns [from to]) num))))
          (input-edge-case? input) (assoc db from input)
          :else db)))

(defn handle-input! [e db [from to]]
  (swap! db set-conversion (-> e .-target .-value) [from to]))

;; View

(defn temperature-converter []
  [:div
   [:input {:on-change #(handle-input! % db [:fahrenheit :celcius])
            :value (:fahrenheit @db)}]
   [:input {:on-change #(handle-input! % db [:celcius :fahrenheit])
            :value (:celcius @db)}]])
