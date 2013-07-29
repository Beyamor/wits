(ns wits.projects.presentation
  (:use [hiccup.page :only [include-js]]
        [hiccup.util :only [escape-html]]
        [wits.util :only [flatten-lists]])
  (:require [wits.web.html :as html]
            [wits.web.apps :as apps]))

(defmulti html-representation
  "Based on the type of the implementation,
   this returns the content for the project itself."
  #(-> % :implementation :type))

(defmethod html-representation :canvas
  [{{[width _ height] :dimensions} :implementation :keys [url]}]
  (apps/embed-canvas
    :url (str "/projects/" url "/canvas")
    :width width
    :height height))

(defmethod html-representation :flash
  [{{[width _ height] :dimensions :keys [swf]} :implementation}]
  (apps/embed-swf
    :swf swf
    :width width
    :height height))

(defn full-project
  "Prepares a project for viewing in full."
  [project]
  (-> project
    (assoc :html-representation (html-representation project))))

(defn for-canvas
  "Prepares a project for, y'know, a canvas."
  [{{:keys [js] [width _ height] :dimensions} :implementation}]
  (apps/canvas
    :js js
    :width width
    :height height))
