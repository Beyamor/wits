(ns wits.core.pages
  (:use hiccup.element
        [hiccup.core :only [html]]
        [hiccup.page :only [html5 include-css include-js]])
  (:require [wits.core.pjax :as pjax]))

(def base-css
  ["/css/core.css"])

(def base-js
  ["/js/lib/jquery-1.9.1.min.js"])

(defn wits-title
  [title]
  (str "Words in the Sky" (if title (str " - " title))))

(defn navigation
  ([]
   (navigation nil))
  ([additional-classes]
    (link-to
      (merge {:id "navigation"} (when additional-classes {:class additional-classes}))
      "/" "wits")))

(defn pjax-page
  [& {:keys [title content css js script full-page? pjax?]}]
  (let [title (str "Words in the Sky" (if title (str " - " title)))
        css (if (and (not full-page?) (not pjax?))
              (conj css "/css/half-page.css") css)
        js (concat js ["/js/lib/jquery.pjax.js" "/js/page-content.js"])
        page (html
               (when pjax? "<!-- Loaded with PJAX -->")
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
         [:canvas#side-graphic]
         [:div#page page]
         (navigation "page-content")]))))

(defn regular-page
  [& {:keys [title content css js script full-page?]}]
  (let [css (if-not full-page? (conj css "/css/half-page.css") css)]
    (html
      [:head
       [:title (wits-title title)]]
      [:body
       (map include-css css)
       (when-not full-page? [:canvas#side-graphic "Nope."])
       [:div#page
        [:div#page-content content]]
       (navigation)
       (map include-js js)
       (if script (javascript-tag script))])))
