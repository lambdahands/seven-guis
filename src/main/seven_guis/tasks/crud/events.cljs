(ns seven-guis.tasks.crud.events
  (:require [seven-guis.tasks.crud.db :as db]
            [re-frame.core :as re]))

;;;; DB Events

;; Helpers

(defn clear-name-inputs [db]
  (assoc db :first-name "" :last-name ""))

;; Handlers

(defn create-name [db]
  (let [id (count (:names db))]
    (-> db
     (update :names assoc id (select-keys db [:first-name :last-name]))
     ; Clear names on create
     (clear-name-inputs))))

(defn update-name [db]
  (let [new-name (select-keys db [:first-name :last-name])]
    (assoc-in db [:names (:selected-id db)] new-name)))

(defn delete-name [db]
  (-> db
    (update :names dissoc (:selected-id db))
    (assoc :selected-id nil)
    (assoc :first-name "" :last-name "")))

(defn select-name [db [_ id]]
  (if-not (= id (:selected-id db))
    (assoc db
           :selected-id id
           :first-name (get-in db [:names id :first-name])
           :last-name (get-in db [:names id :last-name]))
    (-> db
     (assoc :selected-id nil)
     ; Clear name inputs when a row is deselected
     (clear-name-inputs))))

(defn update-first-name [db [_ value]]
  (assoc db :first-name value))

(defn update-last-name [db [_ value]]
  (assoc db :last-name value))

(defn update-filter-prefix [db [_ filter-prefix]]
  (assoc db :filter-prefix filter-prefix))

;; Registration
; Use re-frame.core/path to assign the `db` parameter in event handlers to the
; ::db/crud key in the global app DB.

(re/reg-event-db ::create-name          [(re/path ::db/crud)] create-name)
(re/reg-event-db ::update-name          [(re/path ::db/crud)] update-name)
(re/reg-event-db ::delete-name          [(re/path ::db/crud)] delete-name)
(re/reg-event-db ::select-name          [(re/path ::db/crud)] select-name)
(re/reg-event-db ::update-first-name    [(re/path ::db/crud)] update-first-name)
(re/reg-event-db ::update-last-name     [(re/path ::db/crud)] update-last-name)
(re/reg-event-db ::update-filter-prefix [(re/path ::db/crud)] update-filter-prefix)
