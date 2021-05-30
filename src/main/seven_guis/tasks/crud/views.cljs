(ns seven-guis.tasks.crud.views
  (:require [seven-guis.tasks.crud.events :as e]
            [seven-guis.tasks.crud.subs :as s]
            [re-frame.core :refer [dispatch subscribe]]))

(defn crud []
  (let [first-name    @(subscribe [::s/first-name])
        last-name     @(subscribe [::s/last-name])
        input-valid?  @(subscribe [::s/input-valid?])
        filter-prefix @(subscribe [::s/filter-prefix])
        list-names    @(subscribe [::s/list-names])
        selected-id   @(subscribe [::s/selected-id])]
    [:div.crud
      [:div.crud__name-inputs
       [:div.crud__input-group
        [:label.label "First Name"]
        [:input.input.crud__input
         {:type "text"
          :on-change #(dispatch [::e/update-first-name (-> % .-target .-value)])
          :value first-name}]]
       [:div.crud__input-group
        [:label.label "Last Name"]
        [:input.input.crud__input
         {:type "text"
          :on-change #(dispatch [::e/update-last-name (-> % .-target .-value)])
          :value last-name}]]]
      [:div.crud__actions
       [:button.button.button--confirm.crud__button
        {:disabled (not input-valid?)
         :on-click #(dispatch [::e/create-name])} "Create"]
       [:button.button.crud__button
        {:disabled (not (:with-selected-id? list-names))
         :on-click #(dispatch [::e/update-name])}
        "Update"]
       [:button.button--cancel.button.crud__button
        {:disabled (not (:with-selected-id? list-names))
         :on-click #(dispatch [::e/delete-name])}
        "Delete"]]
     [:input.input.crud__input.crud__search
      {:type "text"
       :on-change #(dispatch [::e/update-filter-prefix (-> % .-target .-value)])
       :placeholder "Search by last name"
       :value filter-prefix}]
     (for [[id {:keys [first-name last-name]}] (:names list-names)]
       [:div.crud__entry
        {:class (when (= id selected-id) "crud__entry--selected")
         :key id
         :on-click #(dispatch [::e/select-name id])}
        (str last-name ", " first-name)])]))
