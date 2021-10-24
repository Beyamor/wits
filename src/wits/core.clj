(ns wits.core
  (:require [wits.blog :as b]
            [clojure.walk :as walk]
            [hiccup.core :as hic]
            [hickory.core :as hik]
            [markdown.core :as md])
  (:import [org.apache.commons.text WordUtils]))

(def blog-source-dir (clojure.java.io/file "blogs"))
(def blog-output-dir (clojure.java.io/file "target/blog"))

(defn title->file-name
  [title]
  (-> title
      clojure.string/lower-case
      (clojure.string/replace " " "-")
      (clojure.string/replace #"[^a-z0-9_\-]" "")
      (str ".html")))

(defn fix-headings
  [content]
  (walk/prewalk
    (fn [el]
      (if (and (vector? el) (= :h1 (first el)))
        [:h1 (WordUtils/capitalizeFully (last el))]
        el))
    content))

(defn blog->html
  [{:keys [title content]}]
  (let [content (-> content
                    md/md-to-html-string
                    hik/parse
                    hik/as-hiccup
                    fix-headings)]
    (hic/html
      [:html
       [:head
        [:meta {:charset "utf-8"}]
        [:title (WordUtils/capitalize title)]]
       [:body
        content]])))

(defn generate-blogs!
  []
  (doseq [blog-file (->> blog-source-dir
                         file-seq
                         (filter b/is-blog-file?)
                         (filter #(clojure.string/includes? (.getName %) "objects-with")))
          :let [blog (b/parse-blog-file blog-file)
                file-name (title->file-name (:title blog))
                file-contents (blog->html blog)
                output-file (clojure.java.io/file blog-output-dir file-name)]]
    (println file-name)
    (clojure.java.io/make-parents output-file)
    (spit output-file file-contents)))

(generate-blogs!)