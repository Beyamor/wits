(ns wits.games.presentation
  (:use [hiccup.page :only [include-js]]
        [wits.util :only [flatten-lists]]))

(defmulti html-representation #(-> % :implementation :type))

(defmethod html-representation :canvas
  [{{[width _ height] :dimensions} :implementation :keys [url]}]
  [:iframe
   {:src (str "/games/" url "/play")
    :width width :height height
    :class "game-container"
    :scrolling "no"}])

(defn full-game
  [game]
  (assoc game :html-representation (html-representation game)))

(defmulti playable-representation :type)

(defmethod playable-representation :canvas
  [{:keys [js] [width _ height] :dimensions}]
  (list
    (->> js
      flatten-lists
      (map #(str % ".js"))
      (map include-js))
    [:canvas {:id "game-canvas" :width width :height height}]))

(defn playable
  [game]
  (playable-representation (:implementation game)))
