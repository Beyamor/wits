(ns wits.blog.load
  (:use wits.blog.core
        korma.core
        [korma.db :only [with-db]])
  (:require [wits.data :as data]))

(defentity blogs)

(defn all
  "Loads all of the blogs, obviously. Assumed to  be in /blogs/"
  []
  (->>
    (select blogs
            (order :date :desc))
    (with-db data/wits-db)))

(defn by-url
  "Returns the blog whose url matches the one provided."
  [url]
  (->>
    (all)
    (filter #(= url (blog->url %)))
    first))
