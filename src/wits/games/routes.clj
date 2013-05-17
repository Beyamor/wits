(ns wits.games.routes
  (:use compojure.core
        [wits.web.pjax :only [PJAX]])
  (:require [wits.games.views :as view]
            [wits.games.library :as games]))

(defroutes all
           (PJAX "/games"
                []
                (->
                  games/all
                  (view/collection pjax?))))
