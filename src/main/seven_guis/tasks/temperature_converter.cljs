(ns seven-guis.tasks.temperature-converter
  (:require [reagent.core :as r]
            [cljs.tools.reader :refer [read-string]]))

(defonce state (r/atom {:temperature-converter {:farenheit "32"
                                                :celcius "0"}}))
(defn str->number [string]
  (let [num (js/Number string)]
    (when (and (not (js/Number.isNaN num))
               (js/Number.isFinite num)
               (number? num))
      num)))

(defn input-edge-case? [input]
     (or (= "-" input)
         (re-find #".*e$" input)
         (re-find #".*e\+$" input)))

(defn celcius->farenheit [num]
  (+ 32 (* num (/ 9 5))))

(defn farenheit->celcius [num]
  (* (- num 32) (/ 9 5)))

(defn set-farenheit [db input]
  (let [num (str->number input)]
    (cond
     num (-> db
           (assoc-in [:temperature-converter :farenheit] input)
           (assoc-in [:temperature-converter :celcius]
                     (str (farenheit->celcius num))))
     (input-edge-case? input)
     (assoc-in db [:temperature-converter :farenheit] input)
     :else db)))

(defn set-celcius [db input]
  (let [num (str->number input)]
    (cond
     num (-> db
           (assoc-in [:temperature-converter :farenheit]
                     (str (celcius->farenheit num)))
           (assoc-in [:temperature-converter :celcius] input))
     (input-edge-case? input)
     (assoc-in db [:temperature-converter :celcius] input)
     :else db)))

(defn temperature-converter []
  [:div
   [:input {:on-change #(swap! state set-farenheit (-> % .-target .-value))
            :value (get-in @state [:temperature-converter :farenheit])}]
   [:input {:on-change #(swap! state set-celcius (-> % .-target .-value))
            :value (get-in @state [:temperature-converter :celcius])}]])
