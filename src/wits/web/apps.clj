(ns wits.web.apps
  (:use [wits.util :only [-#> flatten-lists]]
        [hiccup.page :only [include-js]])
  (:require [wits.web.html :as html]))

(defn embed-canvas
  "Returns the HTML in which a canvas is embedded."
  [& {:keys [width height url container-class]
      :or {container-class "project-container"}}]
  [:iframe
   {:src url
    :width width :height height
    :class container-class
    :scrolling "no"}])

(defn embed-swf
  "Returns the HTML for putting a swf on the page."
  [& {:keys [width height swf container-class]
      :or {container-class "project-container"}}]
  [:div {:class container-class}
   (html/swf :width width :height height :source swf)])

(defn canvas
  "Returns the HTML representation of a game."
  [& {:keys [width height js canvas-id]
      :or {canvas-id "game-canvas"}}]
  (list
    (->> js
      flatten-lists
      (map (-#> (str ".js") include-js)))
    [:canvas {:id canvas-id :width width :height height}]))
