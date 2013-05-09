(ns wits.blog.pages
  (:use hiccup.element
        [hiccup.page :only [html5 include-css]]))

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

(def blogs
  [{:title "here's a blog"
   :date "12-04-2013"
   :content "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque ultrices feugiat felis at ornare. Maecenas sagittis sem vel elit tempus sagittis. Proin nibh sapien, elementum ultrices laoreet eget, pretium id sapien. Nullam adipiscing, dui eget semper faucibus, leo magna scelerisque ipsum, id rutrum dui nisi vitae tellus. Morbi nec justo et turpis lacinia bibendum nec ornare arcu. Proin quis eros odio, at consectetur mi. Sed congue congue lorem, ut pharetra urna vulputate eget. Donec in lorem id tortor consequat imperdiet. Quisque vulputate magna quis dui hendrerit suscipit. Quisque molestie leo sit amet massa ultrices vitae ultricies massa viverra.</p>

                                           <p>Integer ac odio sed augue pulvinar blandit non vitae felis. In feugiat dolor at turpis pharetra ut iaculis nisi laoreet. Proin sit amet nisi vitae dolor varius condimentum. Integer in posuere eros. Suspendisse placerat mattis enim non consequat. Duis massa arcu, rutrum ac blandit ac, cursus quis eros. Integer nec urna eget nibh pellentesque faucibus...</p>"}
  {:title "here's another blog"
   :date "06-10-2012"
   :content "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque ultrices feugiat felis at ornare. Maecenas sagittis sem vel elit tempus sagittis. Proin nibh sapien, elementum ultrices laoreet eget, pretium id sapien. Nullam adipiscing, dui eget semper faucibus, leo magna scelerisque ipsum, id rutrum dui nisi vitae tellus. Morbi nec justo et turpis lacinia bibendum nec ornare arcu. Proin quis eros odio, at consectetur mi. Sed congue congue lorem, ut pharetra urna vulputate eget. Donec in lorem id tortor consequat imperdiet. Quisque vulputate magna quis dui hendrerit suscipit. Quisque molestie leo sit amet massa ultrices vitae ultricies massa viverra.</p>

                                                    <p>Integer ac odio sed augue pulvinar blandit non vitae felis. In feugiat dolor at turpis pharetra ut iaculis nisi laoreet. Proin sit amet nisi vitae dolor varius condimentum. Integer in posuere eros. Suspendisse placerat mattis enim non consequat. Duis massa arcu, rutrum ac blandit ac, cursus quis eros. Integer nec urna eget nibh pellentesque faucibus..."}])

(defn blog-roll
  []
  (html5
    [:head
     [:title "Words in the Sky - Blog"]]
    [:body
     [:canvas {:id "side-graphic"}]
     (link-to {:id "navigation"} "/" "wits")
     [:div {:id "page-content"}
      (map truncated-blog blogs)]
     (include-css "/css/blog.css")]))
