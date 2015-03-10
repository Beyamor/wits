{:title
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
       "/projects/candy/js/ame/ns"
       "/projects/candy/js/ame/debug"
       "/projects/candy/js/ame/util"
       "/projects/candy/js/ame/math"
       "/projects/candy/js/ame/gfx"
       "/projects/candy/js/ame/physics"
       "/projects/candy/js/ame/input"
       "/projects/candy/js/ame/es"
       "/projects/candy/js/ame/esregedit"
       "/projects/candy/js/ame/game"
       "/projects/candy/js/ame/components/core"
       "/projects/candy/js/ame/components/topdownPhysics"
       "/projects/candy/js/ame/components/topdown"
       "/projects/candy/js/ame/components/gfx"
       "/projects/candy/js/ame/systems/topdownPhysics"
       "/projects/candy/js/ame/systems/topdown"
       "/projects/candy/js/ame/systems/gfx"
       "/projects/candy/js/candy/game"]}

 :description
 "In Candy, you must collect candy in a randomly generated level while avoiding the four monsters, each of whom has its own personality. Watch out!
 The game was written in Coffeescript and was my first attempt at using Entity-component-system style architecture. So, that went okay.
 Candy was made for UVic GameDev's Jamoween game jam."}
