(ns wits.projects.library
  (:use [hiccup.page :only [html5 include-js]]
        [wits.web.html :only [include-coffee paragraphs]]
        [wits.util :only [prefixed -#>]]))

(defn- thumbnail
  [name]
  (str "/images/projects/" name "-thumbnail.png"))

(def all
  [{:title
    "Candy"

    :category
    :game

    :url
    "candy"

    :thumbnail
    (thumbnail "candy")

    :short-description
    "Collect candy in a randomly generated level while avoiding the four monsters."

    :implementation
    {:type :canvas
     :dimensions [544 :by 544]
     :js ["/js/lib/jquery-1.9.1.min"
          "/js/lib/coffee-script"
          (prefixed "/projects/candy/js/ame/"
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
          (prefixed "/projects/candy/js/candy/"
                    "game")]}
      
    :description
    (paragraphs
      "In Candy, you must collect candy in a randomly generated level while avoiding the four monsters, each of whom has its own personality. Watch out!"
      "The game was written in Coffeescript and was my first attempt at using Entity-component-system style architecture. So, that went okay."
      "Candy was made for UVic GameDev's Jamoween game jam.")}

   {:title
    "Beef and Harp"

    :category
    :game

    :url
    "beef-and-harp"

    :thumbnail
    (thumbnail "beef-and-harp")

    :short-description
    "A two-player rhythm game/shmup made for Global Game Jam 2013."

    :description
    (paragraphs
      "Beef and Harp is a two-player game, one half rhythm game and one half shmup."
      "In the bottom screen, Beef must protect Harp from the baddies. However, Beef's strength comes from Harp.
       In the top screen, Harp must try to stay in sync with Beef's heartbeat. The more in sync the pair are, the stronger Beef is."
      "The game is built on FlashPunk was created for Global Game Jam 2013.")

    :implementation
    {:type :flash
     :dimensions [800 :by 600]
     :swf "/projects/beef-and-harp/BeefHarp.swf"}

    :source
    "https://github.com/Beyamor/ggj-2013"}

   {:title
    "Vikes Invaders"

    :category
    :game

    :url
    "vikes-invaders"

    :thumbnail
    (thumbnail "vikesinvaders")

    :description
    "A Space Invaders clone jammed out in a day. Webcam integration was added for UVic Clubs Days."

    :implementation
    {:type :flash
     :dimensions [800 :by 600]
     :swf "/projects/vikesinvaders/vikesinvaders.swf"}

    :source
    "https://github.com/Beyamor/vikes-invaders"}

   {:title
    "Endless Mazes"

    :category
    :pcg

    :url
    "endless-mazes"

    :thumbnail
    (thumbnail "endless-mazes")

    :short-description
    "An example of a randomly generated infinite maze."

    :description
    (paragraphs
      "An example of a randomly generated infinite maze."
      "Move with the arrow keys/WASD. Press backspace to create a new maze.")

    :implementation
    {:type :flash
     :dimensions [800 :by 600]
     :swf "/projects/endless-mazes/EndlessMazes.swf"}}

   {:title
    "Chomp"

    :type
    :category

    :url
    "chomp"

    :thumbnail
    (thumbnail "chomp")

    :short-description
    "A quick riff on Snake"

    :description
    (paragraphs
      "A quick riff on Snake."
      "Move with the arrows keys/WASD. Avoid the poop.")

    :implementation
    {:type :flash
     :dimensions [640 :by 480]
     :swf "/projects/chomp/Chomp.swf"}}])

(defn by-url
  "Returns some game by url"
  [url]
  (->>
    all
    (filter
      (-#> :url (= url)))
    first))
