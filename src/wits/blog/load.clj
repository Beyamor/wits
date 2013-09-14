(ns wits.blog.load
  (:use korma.core
        [korma.db :only [with-db]])
  (:require [wits.data :as data]))

(def blogs-per-page 6)

(defentity blogs)

(def blog-list (-> (select* blogs) (order :date :desc)))

(defn all
  "Loads all of the blogs, obviously. Assumed to  be in /blogs/"
  []
  (->>
    (select blog-list)
    (with-db data/wits-db)))

(defn page
  "Loads a page of blogs"
  [page-number]
  (let [page-number (dec page-number)]
    (->>
      (select blog-list
              (offset (* page-number blogs-per-page))
              (limit blogs-per-page))
      (with-db data/wits-db))))

(defn by-url
  "Returns the blog whose url matches the one provided."
  [url]
  (->>
    (select blog-list
            (where {:url url}))
    (with-db data/wits-db)
    first))
