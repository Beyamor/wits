(ns wits.blog.core
  (:use [clojure.string
         :only [blank? trim split-lines lower-case replace]
         :rename {replace s-replace}])
  (:import java.text.SimpleDateFormat))

(defn- parse-params
  "Taking a sequence of the blog parameter lines,
   this returns a map of what their specifications."
  [param-lines]
  (reduce
    (fn [result param-line]
      (let [[_ param-name param-value] (->> param-line
                                         trim 
                                         (re-find #"(\w+): +(.*)"))]
        (case (-> param-name lower-case)
          "title"
          (assoc result :title param-value)

          "date"
          (assoc result :date param-value))))
    {}
    param-lines))

(defn parse-blog
  "Spits out, y'know, contents of a blog file."
  [raw-blog]
  (let [lines (-> raw-blog
                trim
                split-lines)
        param-lines (->> lines
                      (take-while (complement blank?)))
        contents (->> lines
                   (drop-while (complement blank?))
                   (drop-while blank?)
                   (interpose \newline)
                   (apply str))
        {:keys [title date]} (parse-params param-lines)]
    {:title title
     :date date
     :content contents}))

(defn sort-by-date
  "Sorts a sequence of blogs by their dates."
  [blogs]
  (let [formatter (SimpleDateFormat. "MM-dd-yyyy")
        parse-date #(.parse formatter %)]
    (sort-by
      #(->> % :date parse-date)
      #(* -1 (compare %1 %2))
      blogs)))

(defn blog->url
  "Given a blog, this returns the url that should identity it."
  [blog]
  (->
    (:title blog)
    (s-replace #" " "-")
    (s-replace #"[^\w\-]" "")))
