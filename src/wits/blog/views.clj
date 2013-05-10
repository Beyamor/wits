(ns wits.blog.views
  (:use hiccup.element
        [hiccup.util :only [escape-html]]
        [hiccup.page :only [html5 include-css include-js]]
        [wits.blog.core :only [load-all-blogs]])
  (:require [markdown.core :as md]))

(defn sections
  "Takes section-name/content pairs
   and returns a list of divs
   each of which has the provided section name and content."
  [& args]
  (for [[section-name content] (partition 2 args)]
    [:div {:class section-name} content]))

(defn prepare-blog-content
  [content]
  (-> content
    md/md-to-html-string))

(defn blog
  "Creates a view of a blog whose contents are truncated."
  [{:keys [title date content]}]
  (sections
    :blog (sections
            :title title
            :date date
            :content (prepare-blog-content content))))

(defn blog-roll
  []
  (html5
    [:head
     [:title "Words in the Sky - Blog"]]
    [:body
     [:canvas {:id "side-graphic"}]
     (link-to {:id "navigation"} "/" "wits")
     [:div {:id "page-content"}
      (map blog (load-all-blogs))]
     (map include-css
          ["/css/blog.css"
           "/css/lib/syntax-highlighter/shCore.css"
           "/css/lib/syntax-highlighter/themes/witsTheme.css"])
     (map include-js
          ["/js/lib/syntax-highlighter/shCore.js"
           "/js/lib/syntax-highlighter/brushes/shBrushClojure.js"])
     (javascript-tag
       "SyntaxHighlighter.all()")]))
