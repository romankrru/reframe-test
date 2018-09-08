(ns cljs-todo.checkbox.core
  (:require [reagent.core :as reagent]
            [cljs-todo.utils :refer [deep-merge]]
            [stylefy.core :as stylefy]))

(def bg-color "#59CAFF")
(def checkbox-color "white")
(def checkbox-check-color bg-color)

(def input-sub-styles {:display "none"})

(def
  label-sub-styles
  {:position "relative"
   :display "block"
   :width "30px"
   :height "30px"
   :overflow "hidden"
   :border (str "2px solid " checkbox-color)
   :transition (str "transform .5s ease-in-out, "
                    "background .125s ease-in-out")
   :text-align "left"
   :user-select "none"
   ::stylefy/mode {:hover {:background "rgba(255,255,255, .25)"
                           :cursor "pointer"}
                   :before {:content "\"\""
                            :position "absolute"
                            :border-left (str "3px solid " checkbox-check-color)
                            :transition "all .25s ease-in-out"
                            :transform-origin "center center"
                            :width "0"
                            :border-radius "3px"
                            :transform (str "rotate(-30deg) translatex(0) "
                                            "translatey(-10px)")
                            :height "10px"}
                   :after {:content "\"\""
                           :position "absolute"
                           :border-left (str "3px solid " checkbox-check-color)
                           :transition "all .25s ease-in-out"
                           :transform-origin "center center"
                           :width "0"
                           :border-radius "3px"
                           :transform (str "rotate(30deg) translatex(0) "
                                           "translatey(-20px)")
                           :height "20px"
                           :right "0"}}})

(def
  label-checked-sub-styles
  (deep-merge
   label-sub-styles
   {:background checkbox-color
    ::stylefy/mode {:hover {:background checkbox-color}
                    :before {:transform (str "rotate(-45deg) "
                                             "translatex(-4px) "
                                             "translatey(12px)")
                             :-webkit-backface-visibility "hidden"
                             :backface-visibility "hidden"}
                    :after {:transform (str "rotate(45deg) "
                                            "translatex(-4px) "
                                            "translatey(8px)")
                            :-webkit-backface-visibility "hidden"
                            :backface-visibility "hidden"}}}))

(def
  sub-styles
  {::stylefy/sub-styles {:input input-sub-styles
                         :label label-sub-styles
                         :label-checked label-checked-sub-styles}})

(def checkbox-styles {:width "34px"
                      :display "inline-block"})

(def styles (merge checkbox-styles sub-styles))

(defn checkbox [{:keys [checked on-change id]}]
  [:div (stylefy/use-style styles)
   [:input (stylefy/use-sub-style styles :input
                                  {:type "checkbox"
                                   :checked checked
                                   :on-change on-change
                                   :id id})]
   [:label (stylefy/use-sub-style styles
                                  (if checked :label-checked :label)
                                  {:for id})]])
