(ns wits.projects.presentation
  (:use [hiccup.page :only [include-js]]
        [hiccup.element :only [link-to image]]
        [hiccup.util :only [escape-html]]
        [markdown.core :only [md-to-html-string]]
        [wits.util :only [flatten-lists assoc-if-missing]])
  (:require [wits.web.html :as html]
            [wits.web.apps :as apps]
            [lonocloud.synthread :as ->]))

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

(defmethod html-representation :source-only
  [{{:keys [screenshot]} :implementation :keys [source title]}]
  [:a
   {:href source
    :title title
    :target "_blank"}
   [:img
    {:src screenshot}]])

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
    (assoc-if-missing :description "")
    (#(assoc-if-missing % :short-description (-> % :description clojure.string/split-lines first)))
    (->/when (= (-> project :implementation :type) :full-html-page)
             (assoc :full-page? true))))

(defn markdownify
  [project]
  (-> project
    (update-in [:description] #(.replace % "\n" "\n\n"))
    (update-in [:description] md-to-html-string)
    (update-in [:short-description] md-to-html-string)))

(defn prepare
  [project]
  (-> project
    add-properties
    markdownify))
