(ns wits.web.pages
  (:use hiccup.element
        [hiccup.core :only [html]]
        [hiccup.page :only [html5 include-css include-js]])
  (:require [wits.web.html :as html]))

(def core-css
  ["/css/core.css"
   "/css/lib/syntax-highlighter/shCore.css"])

(def core-js
  ["/js/lib/jquery-1.9.1.min.js"
   "/js/lib/jquery.pjax.js"
   "/js/art/image.js"
   "/js/core.js"
   "/js/lib/syntax-highlighter/shCore.js"])

(defn wits-title
  [title]
  [:title (str "Words in the Sky" (if title (str " - " title)))])

(def header
  [:div#header
    [:div.title
     (html/pjax-link "/" "words in the sky")]
    [:div.navigation
     (html/pjax-link "/blog" "blog")
     (html/pjax-link "/code" "code")
     (html/pjax-link "/" "me")]])

(defn as-content
  "Given a map of page attributes (contents, title, css, etc.),
   this returns the resulting HTML contents."
  [{:keys [title content js css script]}]
  (html
    (wits-title title)
    (map include-css css)
    content
    (map include-js js)
    (when script (javascript-tag script))))

(defn main
  "Creates one of the main wits pages given the page contents.
   These page contents should include css, js, etc."
  [page-contents]
  (html5
    [:head
     (map include-css core-css)
     (map include-js core-js)]
    [:body
     [:canvas#art]
     header
     [:div#page
      [:div#page-content
       page-contents]]]))
