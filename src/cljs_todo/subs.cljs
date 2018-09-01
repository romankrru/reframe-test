(ns cljs-todo.subs
  (:require [re-frame.core :refer [reg-sub subscribe]]))

(reg-sub
 :showing
 (fn [db _]
   (:showing db)))

(reg-sub
 :sorted-todos
 (fn [db _]
   (:todos db)))

(reg-sub
 :todos
 (fn [query-v _]
   (subscribe [:sorted-todos]))
 (fn [sorted-todos query-v _]
   (vals sorted-todos)))

(reg-sub
 :visible-todos
 (fn [query-v _]
   [(subscribe [:todos])
    (subscribe [:showing])])
 (fn [[todos showing] _]
   (let [filter-fn (case showing
                     :active (complement :don)
                     :done :done
                     :all identity)]
     (filter filter-fn todos))))

#_(reg-sub
 :visible-todos
 :<- [:todos]
 :<- [:showing]
 (fn [[todos showing] _]
   (let [filter-fn (case showing
                     :active (complement :don)
                     :done :done
                     :all identity)]
     (filter filter-fn todos))))

(reg-sub
 :all-complete?
 :<- [:todos]
 (fn [todos _]
   (every? :done todos)))

(reg-sub
 :completed-count
 :<- [:todos]
 (fn [todos _]
   (count (filter :done todos))))

(reg-sub
 :footer-counts
 :<- [:todos]
 :<- [:completed-count]
 (fn [[todos completed] _]
   [(- (count todos) completed) completed]))
