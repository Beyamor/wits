(ns wits.games.views
  (:use hiccup.element
        [hiccup.page :only [html5]]
        [hiccup.util :only [escape-html]]
        [wits.util :only [-#> -#>>]]
        [wits.web.html :only [sections]])
  (:require [wits.web.pages :as pages]
            [wits.web.html :as html]
            [wits.web.pjax :as pjax]))

(defn preview
  "A Hiccup data structure for the preview of some game."
  [{:keys [url title thumbnail short-description description]}]
  [:div
   (html/pjax-link (str "/games/" url)
                   [:div.game-preview
                    (sections
                      :thumbnail (image thumbnail)
                      :info (sections
                              :title title
                              :description (or short-description description)))])])

(defn collection
  "A preview of some collection of games."
  [games pjax?]
  (pjax/page
    pages/main pjax?
    {:title
     "Games"

     :url
     "candy"

     :css
     ["/css/games-collection.css"]

     :content
     (->> games
       (map preview)
       (interpose html/small-content-separator))}))

(defn full-game
  "Returns a view for playing a game."
  [{:keys [title description html-representation source]} pjax?]
  (pjax/page
    pages/main pjax?
    {:title
     (:title (escape-html title))

     :css
     ["/css/game.css"]

     :content
     (list
       [:h1 (escape-html title)]
       ; haha this is so dumb
       [:div.game-container-container
        html-representation
        (when source (link-to {:class "source" :target "_blank"} source "source"))]
       [:div.description description])}))
