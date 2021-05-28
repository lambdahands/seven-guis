(ns seven-guis.core
  (:require [seven-guis.tasks.counter :refer [counter]]
            [seven-guis.tasks.temperature-converter :refer [temperature-converter]]
            [seven-guis.tasks.flight-booker :refer [flight-booker]]
            [reagent.dom :as rdom]))

(defn main []
  [:div
   [counter]
   [temperature-converter]
   [flight-booker]])

(defn init []
  (let [app-element (js/document.getElementById "app")]
    (rdom/render [main] app-element)))

(defn ^:dev/after-load re-render []
  (init))
