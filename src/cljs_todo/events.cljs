(ns cljs-todo.events
  (:require [cljs-todo.db :refer [default-db todos->local-store]]
            [re-frame.core :refer [reg-event-db
                                   reg-event-fx
                                   inject-cofx
                                   path
                                   after]]
            [cljs.spec.alpha :as s]))

;INTERCEPTORS
(defn check-and-throw
  "Throws an exception if `db` does'not match to th Spec `spec`"
  [a-spec db]
  (when-not (s/valid? a-spec db)
    (throw (ex-info (str "spec check faild: " (s/explain-str a-spec db)) {}))))

(def check-spec-interceptor (after (partial check-and-throw :cljs-todo.db/db)))

(def ->local-store (after todos->local-store))

(def todo-interceptors [check-spec-interceptor
                        (path :todos)
                        ->local-store])

; Helpers
(defn allocate-next-id
  "Returns next todo id.
  Assumes todos are sorted.
  Returns one more than current largest id."
  [todos]
  ((fnil inc 0) (last (keys todos))))

;Event handlers
(reg-event-fx
 :initialize-db
 [(inject-cofx :local-store-todos)
  check-spec-interceptor]
 (fn [{:keys [db local-store-todos]} _]
   {:db (assoc default-db :todos local-store-todos)}))

(reg-event-db
 :set-showing
 [check-spec-interceptor]
 (fn [db [_ new-filter-kw]]
   (assoc db :showing new-filter-kw)))

(reg-event-db
 :add-todo
 todo-interceptors
 (fn [todos [_ text]]
   (let [id (allocate-next-id todos)]
     (assoc todos id {:id id
                      :title text
                      :done false}))))

(reg-event-db
 :toggle-done
 todo-interceptors
 (fn [todos [_ id]]
   (update-in todos [id :done] not)))

(reg-event-db
 :save
 todo-interceptors
 (fn [todos [_ id title]]
   (assoc-in todos [id :title] title)))

(reg-event-db
 :delete-todo
 todo-interceptors
 (fn [todos [_ id]]
   (dissoc todos id)))

(reg-event-db
 :clear-completed
 todo-interceptors
 (fn [todos _]
   (let [done-ids (->> (vals todos)
                       (filter :done)
                       (map :id))]
     (reduce dissoc todos done-ids))))

(reg-event-db
 :complete-all-toggle
 todo-interceptors
 (fn [todos _]
   (let [new-done (not-every? :done (vals todos))]
     (reduce #(assoc-in %1 [%2 :done] new-done)
             todos
             (keys todos)))))
