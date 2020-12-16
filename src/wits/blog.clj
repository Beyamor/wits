(ns wits.blog
  (require [clojure.java.io :as io]
           [clojure.string :as string]))

; Assumes we're running from the root of the project
(def blog-directory (io/file "blogs"))

(defn is-blog-file?
  [f]
  (and (.isFile f)
       (string/ends-with? (.getName f) ".blarg")))

(defn read-first-line
  [s]
  (clojure.string/split s #"\r?\n" 2))

(defn parse-blog-props
  ([text]
   (parse-blog-props text {}))
  ([text props]
   (let [[line text] (read-first-line text)]
     (if (empty? (string/trim line))
       [props text]
       (let [[prop val] (string/split line #":" 2)
             prop (string/trim prop)
             val (string/trim val)
             props (assoc props (keyword prop) val)]
         (recur text props))))))

(defn parse-blog-file
  [f]
  (let [text (slurp f)
        [props content] (parse-blog-props text)]
    (merge props {:content content})))

(doseq [f (->> blog-directory
               file-seq
               (filter is-blog-file?)
               (take 1))]
  (println (parse-blog-file f)))