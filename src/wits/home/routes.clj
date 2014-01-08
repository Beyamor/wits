(ns wits.home.routes
  (:use compojure.core
        [wits.web.pjax :only [PJAX]])
  (:require [wits.home.views :as view]))

(defroutes all
           (PJAX "/"
                []
                (view/home-page pjax?)))
