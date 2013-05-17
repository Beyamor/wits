(ns wits.games.views
  (:use hiccup.element
        [wits.util :only [-#> -#>>]]
        [wits.web.html :only [sections]])
  (:require [wits.web.pages :as pages]
            [wits.web.html :as html]
            [wits.web.pjax :as pjax]))

(defn preview
  "A Hiccup data structure for the preview of some game."
  [{:keys [title thumbnail short-description]}]
  [:div.game-preview
   (sections
     :thumbnail (image thumbnail)
     :info (sections
             :title title
             :description short-description))])

(defn collection
  "A preview of some collection of games."
  [games pjax?]
  (pjax/page
    pages/main pjax?
    {:title
     "Games"

     :css
     ["/css/games-collection.css"]

     :content
     (->> games
       (map preview)
       (interpose html/small-content-separator))}))
