(ns seven-guis.app.views
  (:require [seven-guis.app.subs :as s]
            [seven-guis.app.events :as e]
            [seven-guis.tasks.counter.views :refer [counter counter-description]]
            [seven-guis.tasks.flight-booker.views
             :refer [flight-booker flight-booker-description]]
            [seven-guis.tasks.temperature-converter.views
             :refer [temperature-converter temperature-converter-description]]
            [seven-guis.tasks.timer.views :refer [timer timer-description]]
            [seven-guis.tasks.crud.views :refer [crud crud-description]]
            [re-frame.core :refer [dispatch subscribe]]))

(def tasks
  {:counter               {:title "Counter"
                           :component counter
                           :description counter-description}
   :temperature-converter {:title "Temperature Converter"
                           :component temperature-converter
                           :description temperature-converter-description}
   :flight-booker         {:title "Flight Booker"
                           :component flight-booker
                           :description flight-booker-description}
   :timer                 {:title "Timer"
                           :component timer
                           :description timer-description}
   :crud                  {:title "CRUD"
                           :component crud
                           :description crud-description}})

(defn app []
  (let [task @(subscribe [::s/task])]
    [:<>
     [:div.header
      [:div.header__links
       (for [[task-id {:keys [title]}] tasks]
         [:a.header__link
          {:key (name task-id)
           :href "#"
           :class (when (= task-id task) "header__link--selected")
           :on-click #(dispatch [::e/select-task task-id])}
          title])]
      [:h2.header__title "Seven GUIs"]]
     (let [{:keys [title component description]} (get tasks task)]
       [:div.content-wrapper
        [:div.description
         [:h1.description__header title]
         [:div.description__content [description]]]
        [:div.content
         [:div.content__task
          [component]]
         [:div.reset-state
          [:button.button.reset-state__button
           {:on-click #(dispatch [::e/reset-task-state task])}
           "Reset State"]]]])]))
