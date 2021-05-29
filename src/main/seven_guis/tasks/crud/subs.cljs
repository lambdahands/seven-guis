(ns seven-guis.tasks.crud.subs
  (:require [seven-guis.tasks.crud.db :as db]
            [re-frame.core :as re]))

(defn list-names [{:keys [names filter-prefix filtered-names] :as db}]
  (sort-by first (seq (if (empty? filter-prefix) names filtered-names))))

(re/reg-sub ::first-name #(get-in % [::db/crud :first-name]))
(re/reg-sub ::last-name #(get-in % [::db/crud :last-name]))
(re/reg-sub ::filter-prefix #(get-in % [::db/crud :filter-prefix]))
(re/reg-sub ::list-names #(list-names (::db/crud %)))
