(ns seven-guis.tasks.temperature-converter.events
  (:require [seven-guis.tasks.temperature-converter.db :as db]
            [re-frame.core :as re]))

;;;; DB Events

;; Helpers

(defn celcius->fahrenheit [num]
  (+ 32 (* num (/ 9 5))))

(defn fahrenheit->celcius [num]
  (* (- num 32) (/ 5 9)))

(def conversion-fns
  {[:fahrenheit :celcius] fahrenheit->celcius
   [:celcius :fahrenheit] celcius->fahrenheit})

(defn number-edge-case? [value]
  (or (= "" value)
      (= "-" value)
      (re-find #".*e$" value)
      (re-find #".*e\+$" value)
      (re-find #"^e.*" value)
      (re-find #"^e\+.*" value)))

(defn str->number [string]
  (let [num (js/Number string)]
    (when (and (not (js/Number.isNaN num))
               (js/Number.isFinite num)
               (number? num))
      num)))

;; Handlers

(defn handle-conversion [db [_ value from to]]
  (let [num (str->number value)]
    (cond (number-edge-case? value) (assoc db from value)
          num (-> db
                (assoc from value)
                (assoc to (str ((get conversion-fns [from to]) num))))
          :else db)))

;; Registration
; Use re-frame.core/path to assign the `db` parameter in event handlers to the
; ::db/temperature-converter key in the global app DB.

(re/reg-event-db ::handle-conversion
                 [(re/path ::db/temperature-converter)]
                 handle-conversion)
