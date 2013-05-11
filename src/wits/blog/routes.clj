(ns wits.blog.routes
  (:use compojure.core
        wits.blog.views
        [wits.core.pjax :only [PJAX]]))

(defroutes all
           (GET "/blog" [] (blog-roll))
           (PJAX "/blog/pjax" [] (if pjax? "pjax!" "not pjax!"))
           (GET "/blog/entries/:blog-url" [blog-url] (blog blog-url)))
