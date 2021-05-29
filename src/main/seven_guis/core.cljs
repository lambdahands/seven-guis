(ns seven-guis.core
  (:require [seven-guis.app.events :as app-events]
            [seven-guis.app.views :as app-views]
            [re-frame.core :refer [dispatch-sync]]
            [reagent.dom :as rdom]))

(defonce initialize-state
 (dispatch-sync [::app-events/initialize-state]))


(defn init []
  (let [app-element (js/document.getElementById "app")]
    (rdom/render [app-views/app] app-element)))

(defn ^:dev/after-load re-render []
  (init))
