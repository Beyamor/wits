(ns wits.games.routes
  (:use compojure.core
        [hiccup.page :only [html5 include-js]]
        [wits.web.pjax :only [PJAX]])
  (:require [wits.games.views :as view]
            [wits.games.library :as games]
            [wits.games.presentation :as presentation]
            [wits.web.apps :as apps]
            [wits.web.pages :as pages]))

(defroutes all

           (PJAX "/games"
                []
                (->
                  games/all
                  (view/collection pjax?)))

           (PJAX "/games/:url"
                 [url]
                 (->
                   url
                   games/by-url
                   presentation/full-game
                   (view/full-game pjax?)))

           (GET "/games/:url/canvas"
                [url]
                (->
                  url
                  games/by-url
                  presentation/for-canvas
                  pages/content-wrapper)))
