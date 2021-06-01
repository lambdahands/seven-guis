(ns seven-guis.tasks.timer.db)

;; State declaration

(def initial-state
  {::timer {:interval 0
            :elapsed 0
            ; Define two placeholder keys for the channels that are initialized
            ; by the timer component on mount.
            :timer-ch nil
            :interval-ch nil}})
