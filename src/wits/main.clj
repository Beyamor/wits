(ns wits.main
  (require [clojure.java.io :as io]
           [wits.blog]
           [wits.blog.views]
           [hiccup.core :as hiccup]
           [hiccup.page :as hiccup-page]))

; Assumes we're running from the root of the project
(def blog-directory (io/file "blogs"))
(def root-output-directory (io/file "target"))
(def blog-output-directory (io/file root-output-directory "blogs"))

(doseq [f (->> blog-directory
               file-seq
               (filter wits.blog/is-blog-file?)
               (take 1))]
  (let [blog (wits.blog/parse-blog-file f)
        blog-view (wits.blog.views/full-blog blog)
        file-name (str (wits.blog.views/generate-url-slug blog) ".html")
        output-file (io/file blog-output-directory file-name)]
    (io/make-parents output-file)
    (-> blog-view
        hiccup-page/html5
        (->> (spit output-file)))))