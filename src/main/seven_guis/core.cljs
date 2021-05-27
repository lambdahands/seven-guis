(ns seven-guis.core
  (:require [seven-guis.tasks.counter :refer [counter]]
            [seven-guis.tasks.temperature-converter :refer [temperature-converter]]
            [reagent.dom :as rdom]))

(defn main []
  [:div
   [counter]
   [temperature-converter]])

(defn init []
  (let [app-element (js/document.getElementById "app")]
    (rdom/render [main] app-element)))

(defn ^:dev/after-load re-render []
  (init))
