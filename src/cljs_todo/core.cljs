(ns cljs-todo.core
  (:require
   [cljs-todo.views]
   [reagent.core :as reagent]
   [re-frame.core :refer [dispatch dispatch-sync]]
   [secretary.core :as secretary]
   [goog.events :as events]
   [devtools.core :as devtools]))

(devtools/install!)
(enable-console-print!)

(defn ^:export run
  []
  (reagent/render [cljs-todo.views/todo-app]
                  (.getElementById js/document "app")))
