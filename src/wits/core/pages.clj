(ns wits.core.pages
  (:use hiccup.element
        [hiccup.core :only [html]]
        [hiccup.page :only [html5 include-css include-js]]))

(def base-css
  ["/css/core.css"])

(def base-js
  ["/js/lib/jquery-1.9.1.min.js"
   "/js/lib/jquery.pjax.js"])

(defn pjax-page
  [& {:keys [title content css js script full-page? pjax?]}]
  (let [title (str "Words in the Sky" (if title (str " - " title)))
        css (if full-page? css (conj css "/css/half-page.css"))
        page (html
               [:div#page-content content]
               (map include-css css)
               (map include-js js)
               (javascript-tag script))]
    (if pjax?
      page
      (html5
        [:head
         [:title title]]
        [:body
         (when-not full-page? [:canvas#side-graphic])
         [:div#page page]
         (link-to {:id "navigation" :class ".page-content"} "/" "wits")
         (javascript-tag
           "$(document).pjax('.page-content', '#page')")]))))
