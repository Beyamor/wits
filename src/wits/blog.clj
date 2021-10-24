(ns wits.blog
  (:require wits.core
            wits.html
            [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.walk :as walk]
            [hiccup.core :as hic]
            [hickory.core :as hik]
            [markdown.core :as md])
  (:import [java.text SimpleDateFormat]
           (org.apache.commons.text WordUtils)
           (java.time.format DateTimeFormatter)
           (java.time LocalDate)
           (java.util Locale)))

(def blog-source-dir (clojure.java.io/file "blogs"))
(def blog-output-dir (clojure.java.io/file wits.core/output-root "blog"))

(let [formatter (DateTimeFormatter/ofPattern "dd-MM-yyyy")]
  (defn parse-date
    [s]
    (LocalDate/parse s formatter)))

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
  (let [text (slurp f)
        [props content] (parse-blog-props text)]
    (merge props {:content content})))

(defn title->file-name
  [title]
  (-> title
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
      (let [class (-> attr :class
                      (clojure.string/replace "brush: " "language-"))]
        [:pre
         [:code {:class class} (clojure.string/trim text)]]))))

(defn blog->html
  [{:keys [title content] :as blog}]
  (let [content (-> content
                    md/md-to-html-string
                    hik/parse
                    hik/as-hiccup
                    capitalize-headings
                    syntaxhighlight->highlight)
        title (WordUtils/capitalizeFully title)]
    (hic/html
      [:html
       [:head
        [:meta {:charset "utf-8"}]
        [:title title]
        wits.core/resources-html]
       [:body
        [:div#blog
         [:h1.title title]
         (when (:date blog)
           [:h2.date (format-date (:date blog))])
         [:div#content
          content]
         [:script {:type "text/javascript"}
          "hljs.highlightAll();"]]]])))

(defn generate-blogs!
  []
  (doseq [blog-file (->> blog-source-dir
                         file-seq
                         (filter is-blog-file?)
                         (filter #(clojure.string/includes? (.getName %) "objects-with")))
          :let [blog (parse-blog-file blog-file)
                file-name (title->file-name (:title blog))
                file-contents (blog->html blog)
                output-file (clojure.java.io/file blog-output-dir file-name)]]
    (println file-name)
    (clojure.java.io/make-parents output-file)
    (spit output-file file-contents)))