(ns wits.projects.load
  (:use korma.core
        [korma.db :only [with-db]])
  (:require [wits.data :as data]))

(defentity projects)

(def project-list (-> (select* projects) (order :ordering :asc)))

(defn load-project
  [project]
  (some-> project
    :data
    read-string
    eval))

(defn all
  "Loads all of the projects."
  []
  (->>
    (select project-list)
    (with-db data/wits-db)
    (map load-project)))

(defn by-url
  "Returns the project whose url matches the one provided."
  [url]
  (some->>
    (select project-list
            (where {:url url}))
    (with-db data/wits-db)
    first
    load-project))
