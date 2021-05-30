(ns seven-guis.tasks.crud.views
  (:require [seven-guis.tasks.crud.events :as e]
            [seven-guis.tasks.crud.subs :as s]
            [re-frame.core :refer [dispatch subscribe]]))

(defn crud-description []
  [:p "The task is to build a frame containing the following elements: a textfield "
   [:em "T" [:sub "prefix"]] ", a pair of textfields "
   [:em "T" [:sub "name"]] " and "
   [:em "T" [:sub "surname"]] ", a listbox "
   [:em "L"] ", buttons "
   [:em "B" [:sub "C"]] ", "
   [:em "B" [:sub "U"]] " and "
   [:em "B" [:sub "D"]] " and the three labels as seen in the screenshot. "
   [:em "L"] " presents a view of the data in the database that consists of a list of names. At most one entry can be selected in "
   [:em "L"] " at a time. By entering a string into "
   [:em "T" [:sub "prefix"]] " the user can filter the names whose surname start with the entered prefix—this should happen immediately without having to submit the prefix with enter. Clicking "
   [:em "B" [:sub "C"]] " will append the resulting name from concatenating the strings in "
   [:em "T" [:sub "name"]] " and "
   [:em "T" [:sub "surname"]] " to "
   [:em "L"] ". "
   [:em "B" [:sub "U"]] " and "
   [:em "B" [:sub "D"]] " are enabled iff an entry in "
   [:em "L"] " is selected. In contrast to "
   [:em "B" [:sub "C"]] ", "
   [:em "B" [:sub "U"]] " will not append the resulting name but instead replace the selected entry with the new name. "
   [:em "B" [:sub "D"]] " will remove the selected entry. The layout is to be done like suggested in the screenshot. In particular, "
   [:em "L"] " must occupy all the remaining space."])

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
