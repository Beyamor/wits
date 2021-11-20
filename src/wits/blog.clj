(ns wits.blog
  (:require wits.core
            wits.html
            [clojure.string :as string]
            [hickory.core :as hik]
            [markdown.core :as md])
  (:import (org.apache.commons.text WordUtils)
           (java.time.format DateTimeFormatter)
           (java.time LocalDate)
           (java.util Locale)))

(def blog-source-dir (clojure.java.io/file "blogs"))

(let [formatter1 (DateTimeFormatter/ofPattern "dd-MM-yyyy")
      formatter2 (DateTimeFormatter/ofPattern "MM-dd-yyyy")]
  (defn parse-date
    [s]
    (try
      (LocalDate/parse s formatter1)
      (catch Exception e
        (LocalDate/parse s formatter2)))))

(let [formatter (DateTimeFormatter/ofPattern "MMM dd, yyyy" Locale/ENGLISH)]
  (defn format-date
    [d]
    (.format d formatter)))

(defn is-blog-file?
  [f]
  (and (.isFile f)
       (string/ends-with? (.getName f) ".blarg")))

(defn read-first-line
  [s]
  (clojure.string/split s #"\r?\n" 2))

(defn parse-property
  [prop val]
  (let [prop (keyword (string/trim prop))
        val (string/trim val)
        val (case prop
              :date (parse-date val)
              val)]
    [prop val]))

(defn parse-blog-props
  ([text]
   (parse-blog-props text {}))
  ([text props]
   (let [[line text] (read-first-line text)]
     (if (empty? (string/trim line))
       [props text]
       (let [[prop val] (apply parse-property (string/split line #":" 2))]
         (recur text
                (assoc props prop val)))))))

(defn parse-blog-file
  [f]
  (try
    (let [text (slurp f)
          [props content] (parse-blog-props text)]
      (merge props {:content content}))
    (catch Exception e
      (throw (RuntimeException. (str "Error in " f) e)))))

(defn blog->base-file-name
  [blog]
  (-> blog
      :title
      clojure.string/lower-case
      (clojure.string/replace " " "-")
      (clojure.string/replace #"[^a-z0-9_\-]" "")))

(defn blog->file-name
  [blog]
  [(blog->base-file-name blog) "index.html"])

(defn blog->link
  [blog]
  (str "/blog/entries/" (blog->base-file-name blog)))

(defn capitalize-headings
  [content]
  (wits.html/transform-tag
    content :h1
    (fn [_ [text]]
      [:h1 (WordUtils/capitalizeFully text)])))

(defn syntaxhighlight->highlight
  [content]
  (wits.html/transform-tag
    content :pre
    (fn [attr [text]]
      (let [class (some-> attr :class
                          (clojure.string/replace "brush: " "language-"))]
        [:pre
         [:code {:class class} (if (string? text)
                                 (clojure.string/trim text)
                                 text)]]))))

(defn blog->page
  [{:keys [title content] :as blog}]
  (let [content (-> content
                    md/md-to-html-string
                    hik/parse
                    hik/as-hiccup
                    capitalize-headings
                    syntaxhighlight->highlight)
        title (WordUtils/capitalizeFully title)]
    {:title title
     :body [:div#blog
            [:h1.title title]
            (when (:date blog)
              [:span.date (format-date (:date blog))])
            [:div#content
             content]
            [:script {:type "text/javascript"}
             "hljs.highlightAll();"]]}))

(defn generate-blogs!
  [blogs]
  (doseq [blog blogs]
    (wits.core/generate-page!
      (merge {:file ["blog" "entries" (blog->file-name blog)]}
             (blog->page blog)))))

(defn generate-list!
  [blogs]
  (wits.core/generate-page!
    {:title "Blogs"
     :file ["blog" "index.html"]
     :body (for [blog (reverse (sort-by :date blogs))]
             [:div.blog-list-entry
              [:a {:href (blog->link blog)}
               (WordUtils/capitalizeFully (:title blog))]
              [:span.date (format-date (:date blog))]])}))

(defn generate!
  []
  (let [blogs (->> blog-source-dir
                   file-seq
                   (filter is-blog-file?)
                   (map parse-blog-file))]
    (generate-blogs! blogs)
    (generate-list! blogs)))