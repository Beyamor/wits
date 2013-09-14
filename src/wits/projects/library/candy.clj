(defproject
  "Candy"

  :category
  :game

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
  "In Candy, you must collect candy in a randomly generated level while avoiding the four monsters, each of whom has its own personality. Watch out!
  The game was written in Coffeescript and was my first attempt at using Entity-component-system style architecture. So, that went okay.
  Candy was made for UVic GameDev's Jamoween game jam.")
