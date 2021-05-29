(ns seven-guis.tasks.crud.views
  (:require [seven-guis.tasks.crud.events :as e]
            [seven-guis.tasks.crud.subs :as s]
            [re-frame.core :refer [dispatch subscribe]]))

(defn crud []
  (let [first-name    @(subscribe [::s/first-name])
        last-name     @(subscribe [::s/last-name])
        filter-prefix @(subscribe [::s/filter-prefix])
        list-names    @(subscribe [::s/list-names])]
    [:div
     [:input {:type "text"
              :on-change #(dispatch [::e/update-first-name (-> % .-target .-value)])
              :value first-name}]
     [:input {:type "text"
              :on-change #(dispatch [::e/update-last-name (-> % .-target .-value)])
              :value last-name}]
     [:hr]
     [:input {:type "text"
              :on-change #(dispatch [::e/update-filter-prefix (-> % .-target .-value)])
              :value filter-prefix}]
     [:button {:on-click #(dispatch [::e/create-name])} "Create"]
     [:button {:disabled (not (:with-selected-id? list-names))
               :on-click #(dispatch [::e/update-name])}
      "Update"]
     [:button {:on-click #(dispatch [::e/delete-name])} "Delete"]
     [:hr]
     (for [[id {:keys [first-name last-name]}] (:names list-names)]
       [:div {:key id :on-click #(dispatch [::e/select-name id])}
        (str last-name ", " first-name)])]))
