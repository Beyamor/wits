(ns wits.blog.views
  (:use hiccup.element
        [hiccup.util :only [escape-html]]
        [hiccup.page :only [html5 include-css include-js]]
        [wits.core.html :only [sections html->hiccup html->enlive]]
        [wits.blog.core :only [blog->url]]
        [markdown.core :only [md-to-html-string]]
        [wits.util :only [-#> -#>>]])
  (:require [markdown.core :as md]
            [wits.core.pages :as core-pages]
            [wits.core.html :as html]))

(defn add-code-tags
  "The stuff generated by markdown doesn't explicitly label the code sections.
   Thus, it needs to be added manually so we can style it."
  [content]
  (map
    (fn [el]
      (if (html/tagged? :pre el)
        [:div.code-block el]
        el))
    content))

(defn prepare-blog-content
  [content]
  (-> content
    md-to-html-string
    html->hiccup
    add-code-tags))

(defn blog-with-content
  "Creates a view of a blog, given the blog and a function for processing its content."
  [{:keys [title date content] :as blog} process-content]
  [:div.blog
   (sections
     :title (link-to
              (str "/blog/entries/" (blog->url blog))
              title)
     :date date
     :content (process-content content))])

(defn full-blog
  "Creates a view of a full blog."
  [blog]
  (blog-with-content blog prepare-blog-content))

(defn truncate-by-paragraphs
  "Returns the blog contents truncated to some number of paragraphs"
  [number-of-paragraphs blog-content]
  (->>
    blog-content
    (filter (-#> html/tag (= :p)))
    (take number-of-paragraphs)))

(defn truncated-blog
  "Creates a view of a blog whose contents are truncated."
  [blog]
  (blog-with-content
    blog
    (-#>>
       prepare-blog-content
       (truncate-by-paragraphs 3))))

(def blog-css
  (concat core-pages/base-css
          ["/css/blog.css"
           "/css/lib/syntax-highlighter/shCore.css"
           "/css/lib/syntax-highlighter/themes/witsTheme.css"]))

(def blog-js
  (concat core-pages/base-js
          ["/js/lib/syntax-highlighter/shCore.js"
           "/js/lib/syntax-highlighter/brushes/shBrushClojure.js"]))

(defn blog-page
  [& {:keys [title content full-page?]}]
  (core-pages/regular-page
    :title
    title

    :content
    content

    :css
    blog-css

    :js
    blog-js
    
    :script
    "SyntaxHighlighter.all()"

    :full-page?
    full-page?))

(defn blog-roll
  [blogs]
  (blog-page
    :title
    "Blog"

    :content
    (map truncated-blog blogs)))

(defn blog-entry
  [blog]
  (blog-page
    :full-page?
    true

    :title
    (:title blog)

    :content
    (full-blog blog)))
