(ns wits.handler
  (:use compojure.core
        hiccup.page
        [hiccup.middleware :only (wrap-base-url)])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [wits.blog.routes :as blog]
            [wits.code.routes :as code]
            [wits.games.routes :as games]
            [wits.web.pages :as wits-pages]
            [wits.web.pjax :as pjax]
            [ring.util.response :as ring-resp]))

(defroutes all-routes
           (pjax/PJAX "/" []
                      (pjax/page
                        wits-pages/main pjax?
                        {:css
                         ["/css/home.css"]}))
           blog/all
           code/all
           games/all
           (route/resources "/")
           (route/not-found "Page not found"))

(def app
  (->
    (handler/site all-routes)
    wrap-base-url))
