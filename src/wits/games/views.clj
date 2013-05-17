(ns wits.games.views
  (:use hiccup.element
        [hiccup.util :only [escape-html]]
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

(defn play
  "Returns a view for playing a game."
  [{:keys [title description code]} pjax?]
  (pjax/page
    pages/main pjax?
    {:title
     (:title (escape-html title))

     :content
     (list
       [:h1 (escape-html title)]
       code
       [:div.description description])}))
