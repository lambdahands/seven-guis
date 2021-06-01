(ns seven-guis.tasks.flight-booker.helpers)

(defn format-date
  "Format a date object as a MM/DD/YYYY string"
  [date]
  (let [opts #js {:day "2-digit" :month "2-digit" :year "numeric"}]
    (.toLocaleDateString date "en-US" opts)))

(defn reset-day!
  "Reset the date object's time to the beginning of its day (midnight), and
   return the date"
  [date]
  (.setHours date 0 0 0 0)
  date)

(defn add-days!
  "Add n days to the date object, and return the date"
  [date days]
  (.setDate date (+ days (.getDate date)))
  date)

(defn days-from-today
  "Initialize a new date, n days from today at midnight"
  [days]
  (-> (new js/Date)
    reset-day!
    (add-days! days)))

