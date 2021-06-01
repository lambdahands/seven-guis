(ns seven-guis.core
  (:require [seven-guis.app.events :as app-events]
            [seven-guis.app.views :as app-views]
            [re-frame.core :refer [dispatch-sync]]
            [reagent.dom :as rdom]))

;; Initialize our app state on page load. Wrapping this expression in
;; `defonce` will ensure that hot code reloading will preserve the app state.
(defonce initialize-state
  (dispatch-sync [::app-events/initialize-state]))

(defn init []
  (let [app-element (js/document.getElementById "app")]
    (rdom/render [app-views/app] app-element)))

;; Defining this function with the :dev/after-load metadata tells shadow-cljs to
;; run this function after a hot code reload. This function re-renders the
;; component tree, which will apply to any changes to child components.
(defn ^:dev/after-load re-render []
  (init))
