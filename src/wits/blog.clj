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

(defn parse-tags
  [s]
  (-> s
      (clojure.string/split #",")
      (->>(map clojure.string/trim))
      (->> (into #{}))))

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
              :tags (parse-tags val)
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
    (fn [attr [text]]
      [:h1 attr (WordUtils/capitalizeFully text)])))

(defn heading->id
  [text]
  (-> text
      clojure.string/lower-case
      clojure.string/trim
      (clojure.string/replace #"\s+" "-")
      (clojure.string/replace #"[^\w-]" "")))

(defn identify-headings
  [content]
  (wits.html/transform-tag
    content :h1
    (fn [attr [text]]
      [:h1 (assoc attr :id (heading->id text))
       text])))

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

(defn wrap-images
  [content]
  (wits.html/transform-tag
    content :p
    (fn [attr [img :as body]]
      (if (= :img (first img))
        [:div.img-container img]
        (apply vector :p attr body)))))


(def tag->description
  {"code" "Programming"
   "c" "C Programming Language"
   "cpp" "C++"
   "clojure" "Clojure"
   "java" "Java"
   "js" "JavaScript"
   "python" "Python"
   "procgen" "Procedural Generation"
   "gamedev" "Game Development"
   "langdev" "Programming Language Development"
   "fp" "Functional Programming"})

(defn tag-element
  [tag rel-freq]
  (let [adj (case rel-freq
              :few "smaller"
              :many "larger"
              nil)]
    [:a.tag
     (merge
       {:href (str "/blog/tags/" tag)}
       (when-let [description (tag->description tag)]
         {:title description})
       (when adj
         {:style (str "font-size: " adj)}))
     (str "#" tag)]))

(defn blog->page
  [{:keys [title content tags] :as blog}]
  (let [content (-> content
                    md/md-to-html-string
                    hik/parse
                    hik/as-hiccup
                    capitalize-headings
                    identify-headings
                    syntaxhighlight->highlight
                    wrap-images)
        title (WordUtils/capitalizeFully title)
        tags (sort tags)]
    {:title title
     :body [:div#blog
            [:h1.title title]
            (when (:date blog)
              [:div.date (format-date (:date blog))])
            [:div.tags
             (map #(tag-element % nil) tags)]
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
  [file blogs tag-cloud]
  (wits.core/generate-page!
    {:title "Blogs"
     :file (conj file "index.html")
     :body [:div
            [:div.tag-cloud
             (let [sorted-tags (sort (keys tag-cloud))
                   sorted-freqs (sort (vals tag-cloud))]
               (for [tag sorted-tags
                     :let [freq (get tag-cloud tag)
                           idx (.indexOf sorted-freqs freq)
                           rel-freq (cond
                                      (< (/ idx (count sorted-freqs)) 1/3) :few
                                      (>= (/ idx (count sorted-freqs)) 2/3) :many
                                      :else :some)]]
                 (tag-element tag rel-freq)))]
            [:div.blog-list
             (for [blog (reverse (sort-by :date blogs))]
               [:div.blog-list-entry
                [:a {:href (blog->link blog)}
                 (WordUtils/capitalizeFully (:title blog))]
                [:span.date (format-date (:date blog))]])]]}))

(defn get-tag-cloud
  [blogs]
  (->> blogs
       (map :tags)
       (apply concat)
       frequencies))

(defn generate!
  []
  (let [blogs (->> blog-source-dir
                   file-seq
                   (filter is-blog-file?)
                   (map parse-blog-file))
        tag-cloud (get-tag-cloud blogs)]
    (generate-blogs! blogs)
    (generate-list! ["blog"] blogs tag-cloud)
    (doseq [tag (keys tag-cloud)
            :let [blogs (filter #(contains? (:tags %) tag) blogs)]]
      (generate-list! ["blog" "tags" tag] blogs tag-cloud))))