(ns wits.games.library
  (:use [wits.web.html :only [paragraphs]]))

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

    :description
    (paragraphs
      "In Candy, you must collect candy in a randomly generated level while avoiding the four monsters, each of whom has its own personality. Watch out!"
      "The game was written in Coffeescript and was my first attempt at using Entity-component-system style architecture. So, that went okay."
      "Candy was made for UVic GameDev's Jamoween game jam.")}])
