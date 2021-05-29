(ns seven-guis.tasks.crud.db)

(def initial-state
  {::crud {:names {}
           :selected-id nil
           :first-name ""
           :last-name ""
           :filter-prefix ""
           :filtered-names []}})

