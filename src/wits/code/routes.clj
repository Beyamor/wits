(ns wits.code.routes
  (:use compojure.core
        [wits.web.pjax :only [PJAX]])
  (:require [wits.code.views :as view]))

(defroutes all
           (PJAX "/code"
                []
                (->
                  (view/projects-page pjax?))))
