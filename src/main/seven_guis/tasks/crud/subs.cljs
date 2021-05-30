(ns seven-guis.tasks.crud.subs
  (:require [seven-guis.tasks.crud.db :as db]
            [clojure.string :as str]
            [re-frame.core :as re]))

(defn filter-names [names filter-prefix]
  (if (empty? filter-prefix)
    names
    (filter (fn [[_ {:keys [last-name]}]]
              (str/starts-with? (str/lower-case last-name)
                                (str/lower-case filter-prefix)))
            names)))

(defn input-valid? [{:keys [first-name last-name]}]
  (not (or (empty? (str/trim first-name))
           (empty? (str/trim last-name)))))

(defn list-names [{:keys [names filter-prefix selected-id]}]
  (let [filtered-names (filter-names names filter-prefix)
        selected-id (ffirst (filter #(= selected-id (first %)) filtered-names))
        names (sort-by first (seq filtered-names))]
    {:names names :with-selected-id? (some? selected-id)}))

(re/reg-sub ::input-valid?  #(input-valid? (::db/crud %)))
(re/reg-sub ::list-names    #(list-names (::db/crud %)))
(re/reg-sub ::first-name    #(get-in % [::db/crud :first-name]))
(re/reg-sub ::last-name     #(get-in % [::db/crud :last-name]))
(re/reg-sub ::filter-prefix #(get-in % [::db/crud :filter-prefix]))
(re/reg-sub ::selected-id   #(get-in % [::db/crud :selected-id]))
