(ns seven-guis.tasks.flight-booker.helpers)

(defn format-date [date]
  (let [opts #js {:day "2-digit" :month "2-digit" :year "numeric"}]
    (.toLocaleDateString date "en-US" opts)))

(defn reset-day! [date]
  (.setHours date 0 0 0 0)
  date)

(defn add-days! [date days]
  (.setDate date (+ days (.getDate date)))
  date)

(defn days-from-today [days]
  (-> (new js/Date)
    reset-day!
    (add-days! days)))

