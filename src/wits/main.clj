(ns wits.main
  (require [clojure.java.io :as io]
           [wits.web.pages]
           [wits.blog]
           [wits.blog.views]
           [wits.util]
           [hiccup.core :as hiccup]
           [hiccup.page :as hiccup-page])
  (:import (java.nio.file Paths)))

; Assumes we're running from the root of the project
(def blog-directory (io/file "blogs"))
(def root-output-directory (io/file "target"))

(defn write-file
  [{:keys [file-name page]}]
  (let [output-file (io/file root-output-directory file-name)]
    (when-not (wits.util/is-child-file? root-output-directory output-file)
      (throw (Exception. (str file-name " is not a child of " (.getCanonicalPath root-output-directory)))))
    (io/make-parents output-file)
    (spit output-file
          (wits.web.pages/main
            (wits.web.pages/as-content page)))))

(def blog-files
  (->> blog-directory
       file-seq
       (filter wits.blog/is-blog-file?)
       (take 1)))

(def pages
  (concat [wits.home.views/home-page-view]
          (wits.blog.views/generate-views
            (map wits.blog/parse-blog-file blog-files))))

(doseq [{[path & paths] :path
         :as page} pages]
  (let [relative-path (Paths/get path (into-array String paths))]
    (write-file {:file-name (.toString relative-path)
                 :page page})))