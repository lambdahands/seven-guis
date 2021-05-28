(ns seven-guis.tasks.crud
  (:require [reagent.core :as r]
            [clojure.string :as str]))

(defonce db (r/atom {:names {}
                     :selected-id nil
                     :first-name ""
                     :last-name ""
                     :filter-prefix ""
                     :filtered-names []}))

(defn clear-name-inputs [db]
  (assoc db :first-name "" :last-name ""))

(defn create-name [db]
  (let [id (count (:names db))]
    (-> db
     (update :names assoc id (select-keys db [:first-name :last-name])))))

(defn update-name [db]
  (let [new-name (select-keys db [:first-name :last-name])]
    (assoc-in db [:names (:selected-id db)] new-name)))

(defn delete-name [db]
  (-> db
    (update :names dissoc (:selected-id db))
    (assoc :selected-id nil)
    (clear-name-inputs)))

(defn select-name [db id]
  (assoc db
         :selected-id id
         :first-name (get-in db [:names id :first-name])
         :last-name (get-in db [:names id :last-name])))

(defn filter-names [names filter-prefix]
  (filter (fn [[id {:keys [last-name]}]]
            (str/starts-with? (str/lower-case last-name)
                              (str/lower-case filter-prefix)))
          names))

(defn update-filter-prefix [{:keys [selected-id names] :as db} filter-prefix]
  (let [filtered-names (filter-names names filter-prefix)
        selected-id (ffirst (filter #(= selected-id (first %)) filtered-names))
        filtered-db (assoc db
                           :filter-prefix filter-prefix
                           :filtered-names filtered-names
                           :selected-id selected-id)]
    (if (nil? selected-id)
      (clear-name-inputs filtered-db)
      filtered-db)))

;; Helpers

(defn list-names [{:keys [names filter-prefix filtered-names] :as db}]
  (sort-by first (seq (if (empty? filter-prefix) names filtered-names))))

;; View

(defn crud []
  [:div
   [:input {:type "text"
            :on-change #(swap! db assoc :first-name (-> % .-target .-value))
            :value (:first-name @db)}]
   [:input {:type "text"
            :on-change #(swap! db assoc :last-name (-> % .-target .-value))
            :value (:last-name @db)}]
   [:hr]
   [:input {:type "text"
            :on-change #(swap! db update-filter-prefix (-> % .-target .-value))
            :value (:filter-prefix @db)}]
   [:button {:on-click #(swap! db create-name)} "Create"]
   [:button {:on-click #(swap! db update-name)} "Update"]
   [:button {:on-click #(swap! db delete-name)} "Delete"]
   [:hr]

   (for [[id {:keys [first-name last-name]}] (list-names @db)]
     [:div {:key id :on-click #(swap! db select-name id)}
      (str last-name ", " first-name)])])
