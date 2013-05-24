(ns wits.games.presentation
  (:use [hiccup.page :only [include-js]]
        [hiccup.util :only [escape-html]]
        [wits.util :only [flatten-lists]])
  (:require [wits.web.html :as html]))

(defmulti html-representation
  "Based on the type of the implementation,
   this returns the content for the game itself."
  #(-> % :implementation :type))

(defmethod html-representation :canvas
  [{{[width _ height] :dimensions} :implementation :keys [url]}]
  [:iframe
   {:src (str "/games/" url "/canvas")
    :width width :height height
    :class "game-container"
    :scrolling "no"}])

(defmethod html-representation :flash
  [{{[width _ height] :dimensions :keys [swf]} :implementation}]
  [:div.game-container
   (html/swf :width width :height height :source swf)])

(defn full-game
  "Prepares a game for viewing in full."
  [game]
  (-> game
    (assoc :html-representation (html-representation game))))

(defn canvas
  "Prepares the canvas content for a canvas-based game."
  [{:keys [js] [width _ height] :dimensions}]
  (list
    (->> js
      flatten-lists
      (map #(str % ".js"))
      (map include-js))
    [:canvas {:id "game-canvas" :width width :height height}]))
