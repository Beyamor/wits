(ns wits.core
  (:require [hiccup.core :as hic])
  (:import [org.jsoup Jsoup]))

(def output-root-name "target/site")
(def output-root (clojure.java.io/file output-root-name))
(def css-files ["shades-of-purple.min.css"
                "common.css"
                "blog.css"])
(def js-files [])

(defn resources-html
  [css-files js-files]
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
    (str
      "<!DOCTYPE html>\n"
      doc)))

(defn ->page
  [{:keys [title body js css]}]
  (->html
    [:html
     [:head
      [:meta {:charset "utf-8"}]
      (when title
        [:title title])
      (wits.core/resources-html (or css css-files)
                                (or js js-files))]
     [:body
      header
      [:div#content
       body]]]))

(defn generate-page!
  [{:keys [title body file css js]}]
  (let [html (->page {:title title
                      :body body
                      :css css
                      :js js})
        output-file (if (coll? file)
                      (reduce clojure.java.io/file output-root (flatten file))
                      (clojure.java.io/file output-root file))]
    (println "Generating " file)
    (clojure.java.io/make-parents output-file)
    (spit output-file html)))