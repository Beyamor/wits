(ns wits.blog.views
  (:use hiccup.element
        [hiccup.util :only [escape-html]]
        [hiccup.page :only [html5 include-css include-js]]
        [wits.core.html :only [sections html->hiccup html->enlive]]
        [wits.blog.core :only [load-all-blogs]]
        [markdown.core :only [md-to-html-string]]
        [wits.util :only [-#> -#>>]])
  (:require [markdown.core :as md]
            [wits.core.pages :as core-pages]
            [wits.core.html :as html]))

(defn prepare-blog-content
  [content]
  (-> content
    md-to-html-string
    html->hiccup))

(defn blog-with-content
  "Creates a view of a blog, given the blog and a function for processing its content."
  [{:keys [title date content]} process-content]
  [:div.blog
   (sections
     :title title
     :date date
     :content (process-content content))])

(defn full-blog
  "Creates a view of a full blog."
  [blog]
  (blog-with-content blog prepare-blog-content))

(defn truncate-by-paragraphs
  "Returns the blog contents truncated to some number of paragraphs"
  [number-of-paragraphs blog-content]
  (let [els-with-words (filter (-#> html/tag #{:p :h1 :h2}) blog-content)]
    (loop [collected-paragraphs 0 remaining-els els-with-words truncated-content []]
      (if (and (< collected-paragraphs number-of-paragraphs)
               (seq remaining-els))
        (let [[el & more-els] remaining-els]
          (recur
            (+ collected-paragraphs (if (= :p (html/tag el)) 1 0))
            more-els
            (conj truncated-content el)))
        (list* truncated-content)))))

(defn truncated-blog
  "Creates a view of a blog whose contents are truncated."
  [blog]
  (blog-with-content
    blog
    (-#>>
       prepare-blog-content
       (truncate-by-paragraphs 3))))

(defn blog-roll
  []
  (core-pages/extend-base
    :title
    "Blog"

    :content
    (map truncated-blog (load-all-blogs))

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
