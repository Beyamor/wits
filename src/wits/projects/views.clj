(ns wits.projects.views
  (:use hiccup.element
        [hiccup.page :only [html5]]
        [hiccup.util :only [escape-html]]
        [wits.util :only [-#> -#>>]]
        [wits.web.html :only [sections]])
  (:require [wits.web.pages :as pages]
            [wits.web.html :as html]
            [wits.web.pjax :as pjax]
            [wits.projects.library :as library]))

(defn preview
  "A Hiccup data structure for the preview of some project."
  [{:keys [url title thumbnail short-description description]}]
  [:div
   (html/pjax-link (str "/projects/" url)
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

(defn collection-categories-json
  "Spits out the JSON categorizing the given projects."
  [projects]
  (json/write-str
    {:all projects
     :games (filter-by-category :game projects)
     :pcg (filter-by-category :pcg projects)}))

(defn collection
  "A preview of some collection of projects."
  [projects pjax?]
  (pjax/page
    pages/main pjax?
    {:title
     "Games"

     :css
     ["/css/project-collection.css"]

     :js
     ["/js/project-collection.js"]

     :content
     (list
       (let [{:keys [title short-description url]} (first projects)]
         [:div.showcase
          [:div.screenshot
           (image "/images/projects/candy-showcase.png")]
          [:div.info
             [:div.title title]
             [:div.summary
              [:p short-description]]
             (link-to {:class "check-it-out"}
                      (str "/projects/" url)
                      "check it out")]])

       [:div.collection
        [:div.categories
         (for [[category id] [["all" "all"]
                               ["games" "games"]
                               ["procedural generation" "pcg"]]]
           [:div.category
            {:id (str "collection-category-" id)}
            category])]

        [:div.previews
         (for [{:keys [thumbnail title category]} library/all]
           [:div.preview
            {:data-category category}
            [:div.preview-image
              (image {:title title} thumbnail)]])]])}))

(defn full-project
  "Returns a view for playing a project."
  [{:keys [title description html-representation source]} pjax?]
  (pjax/page
    pages/main pjax?
    {:title
     (:title (escape-html title))

     :css
     ["/css/project.css"]

     :content
     (list
       [:h1 (escape-html title)]
       ; haha this is so dumb
       [:div.project-container-container
        html-representation
        (when source (link-to {:class "source" :target "_blank"} source "source"))]
       [:div.description description])}))
