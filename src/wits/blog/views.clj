(ns wits.blog.views
  (:use hiccup.element
        [hiccup.util :only [escape-html]]
        [hiccup.page :only [html5 include-css include-js]]
        [wits.core.html :only [sections]]
        [wits.blog.core :only [load-all-blogs]])
  (:require [markdown.core :as md]
            [wits.core.pages :as core-pages]))

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
  (core-pages/extend-base
    :title
    "Blog"

    :content
    (map blog (load-all-blogs))

    :css
    (core-pages/extend-base-css
      "/css/blog.css"
      "/css/lib/syntax-highlighter/shCore.css"
      "/css/lib/syntax-highlighter/themes/witsTheme.css")

    :js
    (core-pages/extend-base-js
      "/js/lib/syntax-highlighter/shCore.js"
      "/js/lib/syntax-highlighter/brushes/shBrushClojure.js")

    :script
     "SyntaxHighlighter.all()"))
