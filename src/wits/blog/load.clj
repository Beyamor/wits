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

(defn- total-number-of-pages
  []
  (->>
    (select blog-list (aggregate (count :*) :count))
    first :count
    (with-db data/wits-db)
    (* (/ blogs-per-page))))

(defn page
  "Loads a page of blogs"
  [page-number]
  {:blogs
   (->>
     (select blog-list
             (offset (* (dec page-number) blogs-per-page))
             (limit blogs-per-page))
     (with-db data/wits-db))

   :previous-page
   (when (> page-number 1)
     (dec page-number))

   :next-page
   (when (< page-number (total-number-of-pages))
     (inc page-number))})

(defn by-url
  "Returns the blog whose url matches the one provided."
  [url]
  (->>
    (select blog-list
            (where {:url url}))
    (with-db data/wits-db)
    first))
