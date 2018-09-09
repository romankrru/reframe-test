(ns cljs-todo.button.core
  (:require [reagent.core :as reagent]
            [stylefy.core :as stylefy]))

(def default-styles {:border "3px solid white"
                     :padding "10px 20px"})

(defn button [{:keys []} & children]
  [:button (stylefy/use-style default-styles)
   children])
