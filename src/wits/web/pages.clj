(ns wits.web.pages
  (:use hiccup.element
        [hiccup.core :only [html]]
        [hiccup.page :only [html5 include-css include-js]])
  (:require [wits.web.html :as html]
            [clojure.walk :as walk]))

(def core-css
  ["/css/core.css"
   "/css/lib/syntax-highlighter/shCore.css"])

(def core-js
  ["/js/lib/jquery-1.9.1.min.js"
   "/js/lib/jquery.pjax.js"
   "/js/core.js"
   "/js/art/arts.js"
   "/js/art/starry.js"
   "/js/lib/syntax-highlighter/shCore.js"])

(def page-initialization-js
  ["/js/project-collection.js"])

(defn wits-title
  [title]
  [:title (if title title "Words in the Sky")])

(defn add-pjax-links
  "Makes in-site links pjax"
  [content]
  (walk/postwalk
    (fn [form]
      (if (and (sequential? form) (html/tagged? :a form) (html/has-internal-url? form))
        (html/add-class form "pjax")
        form))
    content))

(def header
  (add-pjax-links
    [:div#header
      [:div.title
       "words in the sky"]
      [:div.navigation
       (link-to "/blog" "blog")
       (link-to "/projects" "projects")]]))

(defn as-content
  "Given a map of page attributes (contents, title, css, etc.),
   this returns the resulting HTML contents."
  [{:keys [title content js css script]}]
  (html
    (wits-title title)
    (map include-css css)
    (-> content
      add-pjax-links)
    (map include-js js)
    (when script (javascript-tag script))))

(defn main
  "Creates one of the main wits pages given the page contents.
   These page contents should include css, js, etc."
  [page-contents]
  (html5
    [:head
     (map include-css core-css)
     (map include-js
          (concat core-js page-initialization-js))]
    [:body
     [:canvas#art]
     header
     [:div#page
      page-contents]]))

(defn content-wrapper
  "Creates a cer simple page which only wraps some content in the body."
  [content]
  (html5
    [:body
     {:style "margin: 0px; padding: 0px;"}
     content]))
