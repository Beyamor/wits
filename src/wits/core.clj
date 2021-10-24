(ns wits.core
  (:require [wits.blog :as b]
            [clojure.walk :as walk]
            [hiccup.core :as hic]
            [hickory.core :as hik]
            [markdown.core :as md])
  (:import [org.apache.commons.text WordUtils]))

(def blog-source-dir (clojure.java.io/file "blogs"))
(def output-root (clojure.java.io/file "target/site"))
(def blog-output-dir (clojure.java.io/file output-root "blog"))
(def css-files ["shades-of-purple.min.css"])
(def js-files ["highlight.min.js"])

(defn title->file-name
  [title]
  (-> title
      clojure.string/lower-case
      (clojure.string/replace " " "-")
      (clojure.string/replace #"[^a-z0-9_\-]" "")
      (str ".html")))

(defn capitalize-headings
  [content]
  (walk/prewalk
    (fn [el]
      (if (and (vector? el) (= :h1 (first el)))
        [:h1 (WordUtils/capitalizeFully (last el))]
        el))
    content))

(defn syntaxhighlight->highlight
  [content]
  (walk/prewalk
    (fn [el]
      (if (and (vector? el) (= :pre (first el)))
        (let [[_ attr text] el
              class (-> attr :class
                        (clojure.string/replace "brush: " "language-"))]
          [:pre
           [:code {:class class} (clojure.string/trim text)]])
        el))
    content))

(def resources-html
  (concat
    (for [css css-files]
      [:link {:rel "stylesheet" :href (str "/css/" css)}])
    (for [js js-files]
      [:script {:type "text/javascript" :src (str "/js/" js)}])))

(defn blog->html
  [{:keys [title content]}]
  (let [content (-> content
                    md/md-to-html-string
                    hik/parse
                    hik/as-hiccup
                    capitalize-headings
                    syntaxhighlight->highlight)]
    (hic/html
      [:html
       [:head
        [:meta {:charset "utf-8"}]
        [:title (WordUtils/capitalize title)]
        resources-html]
       [:body
        [:div#content
         content]
        [:script {:type "text/javascript"}
         "hljs.highlightAll();"]]])))

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

(defn copy-resources!
  []
  (doseq [file-type ["js" "css"]
          input (file-seq (clojure.java.io/file "resources" file-type))
          :when (clojure.string/ends-with? (.getName input) (str "." file-type))
          :let [output (clojure.java.io/file output-root file-type (.getName input))]]
    (clojure.java.io/make-parents output)
    (clojure.java.io/copy input output)))

(defn generate!
  []
  (clojure.java.io/make-parents output-root)
  (copy-resources!)
  (generate-blogs!))

(generate!)