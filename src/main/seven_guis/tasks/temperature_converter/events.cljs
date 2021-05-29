(ns seven-guis.tasks.temperature-converter.events
  (:require [seven-guis.tasks.temperature-converter.db :as db]
            [re-frame.core :as re]))

(defn celcius->fahrenheit [num]
  (+ 32 (* num (/ 9 5))))

(defn fahrenheit->celcius [num]
  (* (- num 32) (/ 9 5)))

(def conversion-fns
  {[:fahrenheit :celcius] fahrenheit->celcius
   [:celcius :fahrenheit] celcius->fahrenheit})

;; Validators

(defn number-edge-case? [value]
  (or (= "-" value)
      (re-find #".*e$" value)
      (re-find #".*e\+$" value)
      (re-find #"^e.*" value)
      (re-find #"^e\+.*" value)))

;; Helpers

(defn str->number [string]
  (let [num (js/Number string)]
    (when (and (not (js/Number.isNaN num))
               (js/Number.isFinite num)
               (number? num))
      num)))

(defn handle-conversion [db [_ from to value]]
  (let [num (str->number value)]
    (cond num (-> db
                (assoc from value)
                (assoc to (str ((get conversion-fns [from to]) num))))
          (number-edge-case? value) (assoc db from value)
          :else db)))

(re/reg-event-db ::handle-conversion
                 [(re/path ::db/temperature-converter)]
                 handle-conversion)


