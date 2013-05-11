(ns wits.core.pages
  (:use hiccup.element
        [hiccup.core :only [html]]
        [hiccup.page :only [html5 include-css include-js]])
  (:require [wits.core.pjax :as pjax]))

(def base-css
  ["/css/core.css"])

(def base-js
  ["/js/lib/jquery-1.9.1.min.js"
   "/js/lib/jquery.pjax.js"])

(defn pjax-page
  [& {:keys [title content css js script full-page? pjax?]}]
  (let [title (str "Words in the Sky" (if title (str " - " title)))
        css (if (and (not full-page?) (not pjax?))
              (conj css "/css/half-page.css")
              css)
        page (html
               (when pjax? "<!-- Loaded with PJAX -->")
               [:div#page-content content]
               (map include-css css)
               (map include-js js)
               (javascript-tag script)
               pjax/page-script)]
    (if pjax?
      page
      (html5
        [:head
         [:title title]]
        [:body
         [:canvas#side-graphic]
         [:div#page page]
         (link-to {:id "navigation" :class "page-content"} "/" "wits")]))))
