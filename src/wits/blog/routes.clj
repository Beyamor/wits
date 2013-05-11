(ns wits.blog.routes
  (:use compojure.core
        wits.blog.views))

(defroutes all
           (GET "/blog" [] (blog-roll))
           (GET "/blog/entries/:blog-url" [blog-url] (blog blog-url)))
