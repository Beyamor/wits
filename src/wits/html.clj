(ns wits.html
  (:require [clojure.walk :as walk]))

(defn transform-tag
  [html tag f]
  (walk/prewalk
    (fn [el]
      (if (and (vector? el) (= tag (first el)))
        (let [[attr body] (if (map? (second el))
                            [(second el) (drop 2 el)]
                            [{} (rest el)])]
          (f attr body))
        el))
    html))