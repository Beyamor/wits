(ns wits.games.routes
  (:use compojure.core
        [hiccup.page :only [html5 include-js]]
        [wits.web.pjax :only [PJAX]])
  (:require [wits.games.views :as view]
            [wits.games.library :as games]
            [wits.games.presentation :as presentation]))

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

           (GET "/games/:url/play"
                [url]
                (->
                  url
                  games/by-url
                  presentation/playable
                  view/playable)))
