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
  "candy"
  "beef-and-harp"
  "vikes-invaders"
  "endless-mazes"
  "chomp"
  "fruit-flies-attack-surprise"
  "colton-and-toms-good-time-text-adventure"
  "neptune-ocean-explorer"
  "midpoint-displacement-armor"
  "space-colonization-tree"
  "game-of-life"
  "pattern-aisle"
  "words-in-the-sky"
  "evolute"
  "poplar"
  "face-rater"
  "dabber"
  "breedxel"
  "ruin")

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
