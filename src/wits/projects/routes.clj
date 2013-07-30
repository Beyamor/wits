(ns wits.projects.routes
  (:use compojure.core
        [hiccup.page :only [html5 include-js]]
        [wits.web.pjax :only [PJAX]])
  (:require [wits.projects.views :as view]
            [wits.projects.library :as projects]
            [wits.projects.presentation :as presentation]
            [wits.web.apps :as apps]
            [wits.web.pages :as pages]))

(defroutes all

           (PJAX "/projects"
                []
                (->
                  projects/all
                  (->> (map presentation/add-properties))
                  (view/collection pjax?)))

           (PJAX "/projects/:url"
                 [url]
                 (->
                   url
                   projects/by-url
                   presentation/add-properties
                   presentation/full-project
                   (view/full-project pjax?)))

           (GET "/projects/:url/canvas"
                [url]
                (->
                  url
                  projects/by-url
                  presentation/add-properties
                  presentation/for-canvas
                  pages/content-wrapper)))
