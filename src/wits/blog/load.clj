(ns wits.blog.load
  (:use wits.blog.core))

(defn all
  "Loads all of the blogs, obviously. Assumed to  be in /blogs/"
  []
  (->>
    (for [file (-> "./blogs" clojure.java.io/file file-seq)
          :when (-> file .getName (.endsWith "blarg"))]
      (->
        file
        clojure.java.io/reader
        slurp
        parse-blog))
    sort-by-date))

(defn by-url
  "Returns the blog whose url matches the one provided."
  [url]
  (->>
    (all)
    (filter #(= url (blog->url %)))
    first))
