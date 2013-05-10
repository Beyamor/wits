(ns wits.blog.routes
  (:use compojure.core
        wits.blog.views))

(defroutes all
           (GET "/blog" [] (blog-roll)))
