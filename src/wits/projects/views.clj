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

(defn collection
  "A preview of some collection of projects."
  [projects pjax?]
  (pjax/page
    pages/main pjax?
    {:title
     "Games"

     :css
     ["/css/project-collection.css"]

     :content
     (list
       [:div.showcase
        [:div.screenshot
         (image "/images/projects/candy-showcase.png")]
        [:div.info
         [:div.title "Candy"]
         [:div.summary
          [:p "Collect candy in a randomly generated level while avoiding the four monsters."]]
         (link-to {:class "check-it-out"} "/projects" "check it out")]]

       [:div.collection
        [:div.categories
         (for [category ["all" "games" "pcg"]]
           [:div.category category])]
        [:div.previews
         (for [{:keys [thumbnail title]} library/all]
           [:div.preview
            [:div.preview-image
              (image {:title title} thumbnail)]])]])}))

     ;(->> projects
     ;  (map preview)
     ;  (interpose html/small-content-separator))}))

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
