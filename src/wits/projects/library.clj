(ns wits.projects.library
  (:use [hiccup.page :only [html5 include-js]]
        [wits.web.html :only [urlify include-coffee paragraphs]]
        [wits.util :only [prefixed -#>]]))

(def ^:private all-projects (atom []))

(defmacro defproject
  [title & {:as properties}]
  `(swap! all-projects conj (merge {:title ~title} ~@properties)))

(defn- load-projects
  [& projects]
  (dorun (map #(load (str "library/" %)) projects)))

(load-projects
  "poplar"
  "miss-the-missile"
  "space-colonization-tree"
  "slippy-slidey-ice-puzzle"
  "candy"
  "neptune-ocean-explorer"
  "the-lauded-deeds-of-the-materialist"
  "herrera"
  "beef-and-harp"
  "vikes-invaders"
  "endless-mazes"
  "chomp"
  "fruit-flies-attack-surprise"
  "colton-and-toms-good-time-text-adventure"
  "midpoint-displacement-armor"
  "game-of-life"
  "pattern-aisle"
  "evolute"
  "face-rater"
  "dabber"
  "breedxel"
  "ruin"
  "message-in-a-bottle"
  "sapce"
  "peaceful-octopus-legends"
  "words-in-the-sky")

(def all @all-projects)

(defn by-url
  "Returns some game by url"
  [url]
  (->>
    all
    (filter
      #(= url
          (-> % :title urlify)))
    first))
