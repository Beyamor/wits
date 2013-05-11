(ns wits.core.pages
  (:use hiccup.element
        [hiccup.page :only [html5 include-css include-js]]))

(def base-css
  ["/css/core.css"])

(def base-js [])

(defn extend-base
  [& {:keys [title content css js script]}]
  (let [title (str "Words in the Sky" (if title (str " - " title)))]
    (html5
      [:head
       [:title title]]
      [:body
       [:canvas#side-graphic]
       [:div#page
         [:div#page-content content]]
       (link-to {:id "navigation"} "/" "wits")
       (map include-css css)
       (map include-js js)
       (javascript-tag script)])))
