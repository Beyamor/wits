(ns wits.projects.routes
  (:use compojure.core
        [hiccup.page :only [html5 include-js]]
        [wits.web.pjax :only [PJAX]])
  (:require [wits.projects.views :as view]
            [wits.projects.load :as load]
            [wits.projects.presentation :as presentation]
            [wits.web.apps :as apps]
            [wits.web.pages :as pages]
            wits.projects.backends.message-in-a-bottle))

(defroutes project-backends
           wits.projects.backends.message-in-a-bottle/all-routes)

(defroutes all

           (PJAX "/projects"
                []
                (->
                  (load/all)
                  (->> (remove :hidden))
                  (->> (map presentation/add-properties))
                  (view/collection pjax?)))

           (PJAX "/projects/:url"
                 [url]
                 (->
                   url
                   load/by-url
                   presentation/add-properties
                   presentation/full-project
                   (view/full-project pjax?)))

           (GET "/projects/:url/canvas"
                [url]
                (->
                  url
                  load/by-url
                  presentation/add-properties
                  presentation/for-canvas
                  pages/content-wrapper))

           project-backends)
