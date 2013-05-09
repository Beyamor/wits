(ns wits.handler
  (:use compojure.core
        hiccup.page
        [hiccup.middleware :only (wrap-base-url)])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defroutes all-routes
           (GET "/" []
                (html5
                  [:head
                   [:title (str "Words in the Sky")]]
                  [:body
                   [:div
                     [:h1 "Hello world"]
                     [:p "And how're you doing today?"]]
                   (include-css "/css/hello-world.css")]))
           (route/resources "/")
           (route/not-found "Page not found"))

(def app
  (->
    (handler/site all-routes)
    wrap-base-url))
