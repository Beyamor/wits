(ns wits.blog.pages
  (:use hiccup.element
        [hiccup.page :only [html5 include-css]]
        [wits.blog.core :only [load-all-blogs]]))

(defn sections
  "Takes section-name/content pairs
   and returns a list of divs
   each of which has the provided section name and content."
  [& args]
  (for [[section-name content] (partition 2 args)]
    [:div {:class section-name} content]))

(defn truncated-blog
  "Creates a view of a blog whose contents are truncated."
  [{:keys [title date content]}]
  (sections
    :blog (sections
            :title title
            :date date
            :content content)))

(defn blog-roll
  []
  (html5
    [:head
     [:title "Words in the Sky - Blog"]]
    [:body
     [:canvas {:id "side-graphic"}]
     (link-to {:id "navigation"} "/" "wits")
     [:div {:id "page-content"}
      (map truncated-blog (load-all-blogs))]
     (include-css "/css/blog.css")]))
