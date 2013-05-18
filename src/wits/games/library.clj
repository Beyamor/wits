(ns wits.games.library
  (:use [hiccup.page :only [html5 include-js]]
        [wits.web.html :only [include-coffee paragraphs]]
        [wits.util :only [prefixed -#>]]))

(defn- thumbnail
  [name]
  (str "/images/games/" name "-thumbnail.png"))

(def all
  [{:title
    "Candy"

    :url
    "candy"

    :thumbnail
    (thumbnail "candy")

    :short-description
    (paragraphs
      "Collect candy in a randomly generated level while avoiding the four monsters.")

    :implementation
    {:type :canvas
     :dimensions [544 :by 544]
     :js ["/js/lib/jquery-1.9.1.min"
          "/js/lib/coffee-script"
          (prefixed "/games/candy/js/ame/"
                    "ns"
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
                    "systems/gfx")
          (prefixed "/games/candy/js/candy/"
                    "game")]}
      
    :description
    (paragraphs
      "In Candy, you must collect candy in a randomly generated level while avoiding the four monsters, each of whom has its own personality. Watch out!"
      "The game was written in Coffeescript and was my first attempt at using Entity-component-system style architecture. So, that went okay."
      "Candy was made for UVic GameDev's Jamoween game jam.")}])

(defn by-url
  "Returns some game by url"
  [url]
  (->>
    all
    (filter
      (-#> :url (= url)))
    first))
