(ns wits.blog.routes
  (:use compojure.core
        wits.blog.views
        [wits.core.pjax :only [PJAX]]))

(defroutes all
           (PJAX "/blog" [] (blog-roll pjax?))
           (PJAX "/blog/entries/:blog-url" [blog-url] (blog blog-url pjax?)))
