(ns wits.blog.routes
  (:use compojure.core
        wits.blog.views
        [wits.web.pjax :only [PJAX]])
  (:require [wits.blog.views :as view]
            [wits.blog.load :as load]))

(defroutes all
           (PJAX "/blog"
                []
                (->
                  (load/page 1)
                  (view/blog-roll pjax?)))
           (PJAX ["/blog/page/:page", :page #"[0-9]+"]
                 [page]
                 (->
                   (load/page (Integer. page))
                   (view/blog-roll pjax?)))
           (PJAX "/blog/entries/:blog-url"
                [blog-url]
                (->
                  (load/by-url blog-url)
                  (view/blog-entry pjax?))))
