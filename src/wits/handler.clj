(ns wits.handler
  (:use compojure.core
        hiccup.page
        [hiccup.middleware :only (wrap-base-url)])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [wits.home.routes :as home]
            [wits.blog.routes :as blog]
            [wits.projects.routes :as projects]
            [wits.web.pages :as wits-pages]
            [wits.web.pjax :as pjax]
            [ring.util.response :as ring-resp]))

(defroutes all-routes
           home/all
           blog/all
           projects/all
           (route/files "/"
                        {:root (get (System/getenv) "WITS_FILES")})
           (route/not-found "Page not found"))

(def app
  (->
    (handler/site all-routes)
    wrap-base-url))
