(ns seven-guis.tasks.timer.db)

(def initial-state
  {::timer {:interval 0
            :elapsed 0
            :timer-ch nil}})
