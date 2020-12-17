(ns wits.blog
  (require [clojure.java.io :as io]
           [clojure.string :as string])
  (:import (java.text SimpleDateFormat)))

(defn parse-date
  [s]
  (.parse (SimpleDateFormat. "dd-MM-yyyy") s))

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