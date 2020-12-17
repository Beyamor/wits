(ns wits.main
  (require [clojure.java.io :as io]
           [wits.web.pages]
           [wits.blog]
           [wits.blog.views]
           [hiccup.core :as hiccup]
           [hiccup.page :as hiccup-page]))

; Assumes we're running from the root of the project
(def blog-directory (io/file "blogs"))
(def root-output-directory (io/file "target"))
(def blog-output-directory (io/file root-output-directory "blogs"))

(let [home-file (io/file root-output-directory "index.html")]
  (spit home-file
        (wits.web.pages/main
          (wits.web.pages/as-content wits.home.views/home-page))))

(def blog-files
  (->> blog-directory
       file-seq
       (filter wits.blog/is-blog-file?)
       (take 1)))

(doseq [f blog-files]
  (let [blog (wits.blog/parse-blog-file f)
        blog-view (wits.blog.views/full-blog blog)
        file-name (str (wits.blog.views/generate-url-slug blog) ".html")
        output-file (io/file blog-output-directory file-name)]
    (io/make-parents output-file)
    (spit output-file
          (wits.web.pages/main
            (wits.web.pages/as-content {:title (:title blog)
                                        :content blog-view})))))