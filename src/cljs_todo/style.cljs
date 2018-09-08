(ns cljs-todo.style
  (:require [stylefy.core :as stylefy]))

(stylefy/tag "*" {:box-sizing "border-box"})

(stylefy/tag "body" {:font-family "sans-serif"
                     :margin 0
                     :padding 0})

(def btn {:color "blue"
          :background-color "black"})
