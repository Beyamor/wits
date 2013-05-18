(ns wits.games.presentation
  (:use [hiccup.page :only [include-js]]
        [wits.util :only [flatten-lists]]))

(defmulti html-representation
  "Based on the type of the implementation,
   this returns the content for the game itself."
  #(-> % :implementation :type))

(defmethod html-representation :canvas
  [{{[width _ height] :dimensions} :implementation :keys [url]}]
  [:iframe
   {:src (str "/games/" url "/play")
    :width width :height height
    :class "game-container"
    :scrolling "no"}])

(defn full-game
  "Prepares a game for viewing in full."
  [game]
  (assoc game :html-representation (html-representation game)))

(defmulti standalone-content
  "Based on the type of the implementation of a game,
   this returns the content to, y'know, play the damn thing."
  :type)

(defmethod standalone-content :canvas
  [{:keys [js] [width _ height] :dimensions}]
  (list
    (->> js
      flatten-lists
      (map #(str % ".js"))
      (map include-js))
    [:canvas {:id "game-canvas" :width width :height height}]))

(defn standalone
  "Creates the standalone content for playing a game.
   i.e., the game itself."
  [game]
  (standalone-content (:implementation game)))
