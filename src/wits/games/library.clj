(ns wits.games.library
  (:use [hiccup.page :only [include-js]]
        [wits.web.html :only [include-coffee paragraphs]]
        [wits.util :only [-#>]]))

(defn- thumbnail
  [name]
  (str "/images/games/" name "-thumbnail.png"))

(def all
  [{:title
    "Candy"

    :thumbnail
    (thumbnail "candy")

    :short-description
    (paragraphs
      "Collect candy in a randomly generated level while avoiding the four monsters.")

    :code
    (list
      (include-js
        "/js/lib/coffee-script.js")
      (map #(include-coffee (str "/games/candy/coffee/ame/" % ".coffee"))
           ["ns"
            "debug"
            "util"
            "math"
            "gfx"
            "physics"
            "input"
            "es"
            "esregedit"
            "game"
            "components/core"
            "components/topdownPhysics"
            "components/topdown"
            "components/gfx"
            "systems/topdownPhysics"
            "systems/topdown"
            "systems/gfx"])
      (include-coffee "/games/candy/coffee/game.coffee")
      [:canvas {:id "game-canvas" :width 544 :height 544} "Hey dork, get a browser that supports canvas."])

    :description
    (paragraphs
      "In Candy, you must collect candy in a randomly generated level while avoiding the four monsters, each of whom has its own personality. Watch out!"
      "The game was written in Coffeescript and was my first attempt at using Entity-component-system style architecture. So, that went okay."
      "Candy was made for UVic GameDev's Jamoween game jam.")}])

(defn by-title
  "Returns some game by title"
  [title]
  (->>
    all
    (filter
      (-#> :title .toLowerCase (= (.toLowerCase title))))
    first))
