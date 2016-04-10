(ns wits.blog.routes
  (:use compojure.core
        wits.blog.views
        [wits.web.pjax :only [PJAX]])
  (:require [wits.blog.views :as view]
            [wits.blog.load :as load]))

(defroutes all
           (PJAX "/blog"
                []
                (some->
                  (load/page 1)
                  (view/blog-page pjax?)))
           (PJAX "/blog/all"
                 []
                 (some->
                   (load/all)
                   (view/all-blogs pjax?)))
           (PJAX ["/blog/page/:page", :page #"[0-9]+"]
                 [page]
                 (some->
                   (load/page (Integer. page))
                   (view/blog-page pjax?)))
           (PJAX "/blog/entries/:blog-url"
                [blog-url]
                (some->
                  (load/by-url blog-url)
                  (view/blog-entry pjax?))))
