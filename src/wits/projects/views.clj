(ns wits.projects.views
  (:use hiccup.element
        [hiccup.page :only [html5 include-css include-js]]
        [hiccup.util :only [escape-html]]
        [wits.util :only [-#> -#>>]]
        [wits.web.html :only [sections]])
  (:require [wits.web.pages :as pages]
            [wits.web.html :as html]
            [wits.web.pjax :as pjax]
            [clojure.data.json :as json]))

(def categories
  [["all" "all"]
   ["games" "game"]
   ["procedural generation" "pcg"]
   ["art" "art"]])

(defn preview
  "A Hiccup data structure for the preview of some project."
  [{:keys [url title thumbnail short-description description]}]
  [:div
   (link-to (str "/projects/" url)
            [:div.project-preview
             (sections
               :thumbnail (image thumbnail)
               :info (sections
                       :title title
                       :description (or short-description description)))])])

(defn filter-by-category
  "Creates a filter function which
  returns only the projects in the given category"
  [category projects]
  (filter #(= category (:category %)) projects))

(defn showcase
  [{:keys [title short-description url] showcase-image :showcase}]
  [:div.showcase
   [:div.screenshot
    (link-to {:class "project-link"} url
             (image showcase-image))]
   [:div.info
    [:div.title title]
    [:div.summary
     short-description]
    [:div.check-it-out (link-to {:class "project-link"} url "check it out")]]])

(defn collection
  "A preview of some collection of projects."
  [projects pjax?]
  (pjax/page
    pages/main pjax?
    {:title
     "Projects"

     :css
     ["/css/project-collection.css"]

     :script
     (str
       "projectCollection="
       (json/write-str projects)
       ";")

     :content
     (list
       [:div (showcase (first projects))]

       [:div
        [:div.collection
         [:div.categories
          (for [[label category] categories]
            [:div
             {:class (str "category"
                          (when (= "all" category) " selected"))
              :data-category category}
             label])]

         [:div.previews
          (for [i (range (count projects))
                :let [{:keys [url thumbnail title category]} (nth projects i)]]
            [:div.preview
             {:data-category category
              :data-id i}
             [:div.preview-image
              (link-to {:class "not-pjax"} url
                       (image {:title title} thumbnail))]])]]])}))

(defn full-project
  "Returns a view for playing a project."
  [{:keys [title description html-representation source]} pjax?]
  (pjax/page
    pages/main pjax?
    {:title
     (escape-html title)

     :css
     ["/css/project.css"]

     :content
     (list
       [:div.title (escape-html title)]
       ; haha this is so dumb
       [:div#app.project-container-container
        html-representation
        (when source (link-to {:class "source" :target "_blank"} source "source"))]
       [:div.description
        description])}))

(defn full-page
  "A full-page full-project view.
  Note that this shouldn't be used with PJAX
  since we don't want it fitting within another page's content."
  [project]
  (html5
    [:head
     (include-css "/css/full-page.css")
     (include-js "/js/lib/jquery-1.9.1.min.js")
     (include-js "/js/full-page-project.js")]
    [:body
     [:iframe
       {:src (-> project :implementation :url)}]]))
