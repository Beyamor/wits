(ns wits.projects.presentation
  (:use [hiccup.page :only [include-js]]
        [hiccup.element :only [link-to image]]
        [hiccup.util :only [escape-html]]
        [wits.util :only [flatten-lists assoc-if-missing]])
  (:require [wits.web.html :as html]
            [wits.web.apps :as apps]))

(defmulti html-representation
  "Based on the type of the implementation,
   this returns the content for the project itself."
  (comp :type :implementation))

(defmethod html-representation :canvas
  [{{[width _ height] :dimensions} :implementation :keys [url]}]
  (apps/embed-canvas
    :url (str url "/canvas")
    :width width
    :height height))

(defmethod html-representation :flash
  [{{[width _ height] :dimensions :keys [swf]} :implementation}]
  (apps/embed-swf
    :swf swf
    :width width
    :height height))

(defmethod html-representation :download
  [{{{:keys [windows]} :urls} :implementation :keys [title]}]
  [:div
   (image (str "/images/projects/" (html/urlify title) ".png"))
    [:div.download-links
     "Download links:"
     [:div (when windows (link-to {:class "not-pjax"} windows "Windows"))]]])

(defmethod html-representation :html-page
  [{{[width _ height] :dimensions :keys [url]} :implementation}]
  [:iframe
   {:src url
    :width width :height height
    :class "project-container"
    :scrolling "no"}])

(defmethod html-representation :this-site
  [_]) ; Nothing!

(defn full-project
  "Prepares a project for viewing in full."
  [project]
  (-> project
    (assoc :html-representation (html-representation project))))

(defn for-canvas
  "Prepares a project for, y'know, a canvas."
  [{{:keys [js id background-color] [width _ height] :dimensions} :implementation}]
  (apps/canvas
    :js js
    :width width
    :height height
    :id id
    :background-color background-color))

(defn add-properties
  [{:keys [title] :as project}]
  (-> project
    (assoc :thumbnail (str "/images/projects/" (html/urlify title) "-thumbnail.png"))
    (assoc :showcase (str "/images/projects/" (html/urlify title) "-showcase.png"))
    (assoc :url (str "/projects/" (html/urlify title)))
    (assoc-if-missing :short-description (-> project :description clojure.string/split-lines first))))
