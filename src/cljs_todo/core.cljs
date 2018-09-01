(ns cljs-todo.core
  (:require-macros [secretary.core :refer [defroute]])
  (:require
   [goog.events :as events]
   [cljs-todo.views]
   [reagent.core :as reagent]
   [re-frame.core :refer [dispatch dispatch-sync]]
   [secretary.core :as secretary]
   [goog.events :as events]
   [cljs-todo.events]
   [cljs-todo.subs]
   [cljs-todo.views]
   [devtools.core :as devtools])
  (:import [goog History]
           [goog.history EventType]))

(devtools/install!)
(enable-console-print!)

(dispatch-sync [:initialize-db])

(defroute "/" [] (dispatch [:set-showing :all]))
(defroute "/:filter" [filter] (dispatch [:set-showing (keyword filter)]))

(def history
  (doto (History.)
    (events/listen EventType.NAVIGATE
                   (fn [event] (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

(defn ^:export run
  []
  (reagent/render [cljs-todo.views/todo-app]
                  (.getElementById js/document "app")))
