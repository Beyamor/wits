(ns wits.core)

(def output-root (clojure.java.io/file "target/site"))
(def css-files ["shades-of-purple.min.css"])
(def js-files ["highlight.min.js"])

(def resources-html
  (concat
    (for [css css-files]
      [:link {:rel "stylesheet" :href (str "/css/" css)}])
    (for [js js-files]
      [:script {:type "text/javascript" :src (str "/js/" js)}])))