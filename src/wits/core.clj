(ns wits.core
  (:require [hiccup.core :as hic])
  (:import [org.jsoup Jsoup]))

(def output-root (clojure.java.io/file "target/site"))
(def css-files ["shades-of-purple.min.css"
                "common.css"
                "blog.css"])
(def js-files ["highlight.min.js"])

(def resources-html
  (concat
    (for [css css-files]
      [:link {:rel "stylesheet" :href (str "/css/" css)}])
    (for [js js-files]
      [:script {:type "text/javascript" :src (str "/js/" js)}])))

(def header
  [:div#header
   [:h1 [:a {:href "/"} "Words in the Sky"]]
   (for [[text link] [["Blog" "/blog"]]]
     [:a {:href link} text])])

(defn ->html
  [e]
  (let [raw-html (hic/html e)
        doc (doto (Jsoup/parse raw-html)
              (-> .outputSettings (doto
                                    (.prettyPrint true))))]
    (str doc)))

(defn ->page
  [{:keys [title body]}]
  (->html
    [:html
     [:head
      [:meta {:charset "utf-8"}]
      (when title
        [:title title])
      wits.core/resources-html]
     [:body
      header
      [:div#content
       body]]]))

(defn generate-page!
  [{:keys [title body file]}]
  (let [html (->page {:title title
                      :body body})
        output-file (if (coll? file)
                      (reduce clojure.java.io/file output-root (flatten file))
                      (clojure.java.io/file output-root file))]
    (println "Generating " file)
    (clojure.java.io/make-parents output-file)
    (spit output-file html)))