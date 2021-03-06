(ns wits.projects.routes
  (:use compojure.core
        [hiccup.page :only [html5 include-js]]
        [wits.web.pjax :only [PJAX]])
  (:require [wits.projects.views :as view]
            [wits.projects.load :as load]
            [wits.projects.presentation :as presentation]
            [wits.web.apps :as apps]
            [wits.web.pages :as pages]
            wits.projects.backends.message-in-a-bottle
            [lonocloud.synthread :as ->]
            [ring.util.response :as resp]))

(defroutes project-backends
           wits.projects.backends.message-in-a-bottle/all-routes)

(defroutes all
           (PJAX "/projects"
                []
                (some->
                  (load/all)
                  (->> (remove :hidden))
                  (->> (map presentation/prepare))
                  (view/collection pjax?)))

           (PJAX "/projects/:url"
                 [url]
                 (some->
                   url
                   load/by-url
                   presentation/prepare
                   (->/as project
                          (->/if (:full-page? project)
                                 view/full-page
                                 (->
                                   presentation/full-project
                                   (view/full-project pjax?))))))

           (GET "/projects/:url/canvas"
                [url]
                (some->
                  url
                  load/by-url
                  presentation/prepare
                  presentation/for-canvas
                  pages/content-wrapper))

           project-backends)
