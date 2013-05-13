(ns wits.web.pages
  (:use hiccup.element
        [hiccup.core :only [html]]
        [hiccup.page :only [html5 include-css include-js]]))

(def base-css
  ["/css/core.css"])

(def base-js
  ["/js/lib/jquery-1.9.1.min.js"
   "/js/lib/jquery.pjax.js"
   "/js/page-content.js"])

(defn wits-title
  [title]
  [:title (str "Words in the Sky" (if title (str " - " title)))])

(def header
  [:div#header
    [:div.title
     (link-to {:class "page-content"} "/" "words in the sky")]
    [:div.navigation
     (link-to {:class "page-content"} "/blog" "blog")
     (link-to {:class "page-content"} "/" "code")
     (link-to {:class "page-content"} "/" "me")]])

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
    [:head]
    [:body
     [:canvas#art]
     [:div#page
      header
      [:div#page-content
       page-contents]]]))
