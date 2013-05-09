(ns wits.blog.routes
  (:use compojure.core
        wits.blog.pages))

(defroutes all
           (GET "/blog" [] (blog-roll)))
