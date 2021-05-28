(ns seven-guis.core
  (:require [seven-guis.tasks.counter :refer [counter]]
            [seven-guis.tasks.temperature-converter :refer [temperature-converter]]
            [seven-guis.tasks.flight-booker :refer [flight-booker]]
            [seven-guis.tasks.timer :refer [timer]]
            [reagent.dom :as rdom]))

(defn main []
  [:div
   [counter]
   [temperature-converter]
   [flight-booker]
   [timer]])

(defn init []
  (let [app-element (js/document.getElementById "app")]
    (rdom/render [main] app-element)))

(defn ^:dev/after-load re-render []
  (init))
