(ns wits.blog
  (:require wits.core
            wits.html
            [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.walk :as walk]
            [hiccup.core :as hic]
            [hickory.core :as hik]
            [markdown.core :as md])
  (:import (org.apache.commons.text WordUtils)
           (java.time.format DateTimeFormatter)
           (java.time LocalDate)
           (java.util Locale)))

(def blog-source-dir (clojure.java.io/file "blogs"))
(def blog-output-dir (clojure.java.io/file wits.core/output-root "blog"))

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

(defn blog->file-name
  [blog]
  (-> blog
      :title
      clojure.string/lower-case
      (clojure.string/replace " " "-")
      (clojure.string/replace #"[^a-z0-9_\-]" "")
      (str ".html")))

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

(defn ->page
  [{:keys [title body]}]
  (hic/html
    [:html
     [:head
      [:meta {:charset "utf-8"}]
      [:title title]
      wits.core/resources-html]
     [:body
      [:div#content
       body]]]))

(defn blog->html
  [{:keys [title content] :as blog}]
  (let [content (-> content
                    md/md-to-html-string
                    hik/parse
                    hik/as-hiccup
                    capitalize-headings
                    syntaxhighlight->highlight)
        title (WordUtils/capitalizeFully title)]
    (->page
      {:title title
       :body [:div#blog
              [:h1.title title]
              (when (:date blog)
                [:h2.date (format-date (:date blog))])
              [:div#content
               content]
              [:script {:type "text/javascript"}
               "hljs.highlightAll();"]]})))

(defn generate-blogs!
  [blogs]
  (doseq [blog blogs
          :let [file-name (blog->file-name blog)
                file-contents (blog->html blog)
                output-file (clojure.java.io/file blog-output-dir file-name)]]
    (println file-name)
    (clojure.java.io/make-parents output-file)
    (spit output-file file-contents)))

(defn generate-list!
  [blogs]
  (spit (clojure.java.io/file blog-output-dir "index.html")
        (->page
          {:title "Blogs"
           :body (for [blog (reverse (sort-by :date blogs))]
                   [:div.blog-list-entry
                    [:a {:href (str "/blog/" (blog->file-name blog))}
                     (WordUtils/capitalizeFully (:title blog))]
                    [:span.date (format-date (:date blog))]])})))

(defn generate!
  []
  (let [blogs (->> blog-source-dir
                   file-seq
                   (filter is-blog-file?)
                   (map parse-blog-file))]
    (generate-blogs! blogs)
    (generate-list! blogs)))