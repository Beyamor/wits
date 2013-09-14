(ns wits.projects.views
  (:use hiccup.element
        [hiccup.page :only [html5]]
        [hiccup.util :only [escape-html]]
        [wits.util :only [-#> -#>>]]
        [wits.web.html :only [sections]])
  (:require [wits.web.pages :as pages]
            [wits.web.html :as html]
            [wits.web.pjax :as pjax]
            [wits.projects.library :as library]
            [clojure.data.json :as json]))

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
    (link-to url
      (image showcase-image))]
   [:div.info
    [:div.title title]
    [:div.summary
     [:p short-description]]
    [:div.check-it-out (link-to url "check it out")]]])

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
          (for [[category id] [["all" "all"]
                               ["games" "games"]
                               ["procedural generation" "pcg"]]]
            [:div
             {:id (str "collection-category-" id)
              :class (str "category" (when (= "all" id) " selected"))}
             category])]

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
       [:h1 (escape-html title)]
       ; haha this is so dumb
       [:div.project-container-container
        html-representation
        (when source (link-to {:class "source" :target "_blank"} source "source"))]
       [:div.description (html/str->p description)])}))
