(ns wits.games.presentation
  (:use [hiccup.page :only [include-js]]
        [hiccup.util :only [escape-html]]
        [wits.util :only [flatten-lists]])
  (:require [wits.web.html :as html]
            [wits.web.apps :as apps]))

(defmulti html-representation
  "Based on the type of the implementation,
   this returns the content for the game itself."
  #(-> % :implementation :type))

(defmethod html-representation :canvas
  [{{[width _ height] :dimensions} :implementation :keys [url]}]
  (apps/embed-canvas
    :url (str "/games/" url "/canvas")
    :width width
    :height height))

(defmethod html-representation :flash
  [{{[width _ height] :dimensions :keys [swf]} :implementation}]
  (apps/embed-swf
    :swf swf
    :width width
    :height height))

(defn full-game
  "Prepares a game for viewing in full."
  [game]
  (-> game
    (assoc :html-representation (html-representation game))))

(defn for-canvas
  "Prepares a game for, y'know, a canvas."
  [{{:keys [js] [width _ height] :dimensions} :implementation}]
  (println width "," height)
  (apps/canvas
    :js js
    :width width
    :height height))
