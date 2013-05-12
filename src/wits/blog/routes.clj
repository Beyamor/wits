(ns wits.blog.routes
  (:use compojure.core
        wits.blog.views
        [wits.web.pjax :only [PJAX]])
  (:require [wits.blog.views :as view]
            [wits.blog.load :as load]))

(defroutes all
           (GET "/blog"
                []
                (->
                  (load/all)
                  view/blog-roll))
           (GET "/blog/entries/:blog-url"
                [blog-url]
                (->
                  (load/by-url blog-url)
                  view/blog-entry)))
