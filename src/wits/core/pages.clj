(ns wits.core.pages
  (:use hiccup.element
        [hiccup.page :only [html5 include-css include-js]]))

(def base-css
  ["/css/core.css"])

(def base-js
  ["/js/lib/jquery-1.9.1.min.js"
   "/js/lib/jquery.pjax.js"])

(defn extend-base
  [& {:keys [title content css js script full-page?]}]
  (let [title (str "Words in the Sky" (if title (str " - " title)))
        css (if full-page? css (conj css "/css/half-page.css"))]
    (html5
      [:head
       [:title title]]
      [:body
       (when-not full-page? [:canvas#side-graphic])
       [:div#page
         (link-to {:id "pjax-link"} "/blog/pjax" "pjax?")
         [:div#page-content content]]
       (link-to {:id "navigation"} "/" "wits")
       (map include-css css)
       (map include-js js)
       (javascript-tag script)
       (javascript-tag
         "$(document).pjax('#pjax-link', '#page-content')")
       ])))
