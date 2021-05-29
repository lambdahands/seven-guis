(ns seven-guis.tasks.timer.db
  (:require [cljs.core.async :refer [chan]]))

(def initial-state
  {::timer {:interval 0
            :elapsed 0
            :timer-ch nil}})
