(ns wits.code.views
  (:use hiccup.element
        [hiccup.util :only [escape-html]]
        [hiccup.page :only [html5 include-css include-js]])
  (:require [wits.web.pages :as pages]
            [wits.web.html :as html]
            [wits.web.pjax :as pjax]))

(defn project-category
  [name header]
  [:div.project-category
   [:h1 name]
   (image header)])

(defn projects-page
  [pjax?]
  (pjax/page
    pages/main pjax?
    {:title
     "Code"
     
     :content
     [:div.project-categories
      (interpose html/content-separator
                 [(project-category "Games" "/images/games-header.png")
                  (project-category "Procedural Generation" "/images/pcg-header.png")
                  (project-category "Miscellanea" "/images/misc-header.png")])]

     :css
     ["/css/code.css"]}))
