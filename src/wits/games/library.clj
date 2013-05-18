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
      "Candy was made for UVic GameDev's Jamoween game jam.")}

   {:title
    "Beef and Harp"

    :url
    "beef-and-harp"

    :thumbnail
    (thumbnail "beef-and-harp")

    :short-description
    (paragraphs
      "A two-player rhythm game/shmup made for Global Game Jam 2013.")

    :description
    (paragraphs
      "Beef and Harp is a two-player game, one half rhythm game and one half shmup."
      "In the bottom screen, Beef must protect Harp from the baddies. However, Beef's strength comes from Harp.
       In the top screen, Harp must try to stay in sync with Beef's heartbeat. The more in sync the pair are, the stronger Beef is."
      "The game is built on FlashPunk was created for Global Game Jam 2013.")

    :implementation
    {:type :flash
     :dimensions [800 :by 600]
     :swf "/games/beef-and-harp/BeefHarp.swf"}

    :source
    "https://github.com/Beyamor/ggj-2013"}])

(defn by-url
  "Returns some game by url"
  [url]
  (->>
    all
    (filter
      (-#> :url (= url)))
    first))
