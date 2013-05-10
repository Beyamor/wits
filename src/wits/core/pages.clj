(ns wits.core.pages
  (:use hiccup.element
        [hiccup.page :only [html5 include-css include-js]]))

(def base-css [])

(defn extend-base-css
  [& more-csses]
  (concat base-css more-csses))

(def base-js [])

(defn extend-base-js
  [& more-jses]
  (concat base-js more-jses))

(defn extend-base
  [& {:keys [title content css js script]}]
  (let [title (str "Words in the Sky" (if title (str " - " title)))]
    (html5
      [:head
       [:title title]]
      [:body
       [:canvas#side-graphic]
       (link-to {:id "navigation"} "/" "wits")
       [:div#page-content content]
       (map include-css css)
       (map include-js js)
       (javascript-tag script)])))
