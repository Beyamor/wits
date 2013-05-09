(ns wits.handler
  (:use compojure.core
        hiccup.page
        [hiccup.middleware :only (wrap-base-url)])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [wits.blog.routes :as blog]
            [ring.util.response :as ring-resp]))

(defroutes all-routes
           (GET "/" [] (ring-resp/redirect "/blog"))
           blog/all
           (route/resources "/")
           (route/not-found "Page not found"))

(def app
  (->
    (handler/site all-routes)
    wrap-base-url))
