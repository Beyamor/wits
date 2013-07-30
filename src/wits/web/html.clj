(ns wits.web.html
  (:use [hiccup.element :only [link-to]]
        [hiccup.util :only [to-uri]])
  (:require [net.cgrand.enlive-html :as enlive])
  (:import java.io.StringReader))

(defn sections
  "Takes section-name/content pairs
   and returns a list of divs
   each of which has the provided section name and content."
  [& args]
  (for [[section-name content] (partition 2 args)]
    [:div {:class section-name} content]))

(defn enlive->hiccup
  "Taking some Enlive HTML structure,
   this transforms it into a Hiccup structure."
  [el]
  (if-not (string? el)
    (->>
      (map enlive->hiccup (:content el))
      (concat [(:tag el) (:attrs el)])
      (keep identity)
      vec)
    el))

(defn html->enlive
  "Taking some HTML string, this returns an Enlive data structure."
  [html]
  (-> html enlive/html-snippet))

(defn html->hiccup
  "Taking some HTML string, this returns a Hiccup data structure."
  [html]
  (->> html html->enlive (map enlive->hiccup)))

(defn content
  "Gets some Hiccup element's contents."
  [el]
  (last el))

(defn set-content
  "Sets some Hiccup element's contents."
  [el new-content]
  (-> (butlast el) vec (conj new-content)))

(defn tag
  "Gets the tag of some Hiccp element."
  [el]
  (first el))

(defn tagged?
  "Returns true if the given Hiccup element has the provided tag."
  [tag-to-match el]
  (= tag-to-match (tag el)))

(def content-separator [:div.content-separator])
(def small-content-separator [:div.small-content-separator])

(defn paragraphs
  "Takes a buncha strings and puts each one in a paragraph."
  [& ps]
  (map (fn [p] [:p p]) ps))

(defn include-coffee
  "Includes a list of coffee scripts."
    [& scripts]
    (for [script scripts]
      [:script {:type "text/coffeescript", :src (to-uri script)}]))

(defn swf
  "The Hiccup representation of a swf"
  [& {:keys [width height source]}]
  [:object
    {:classid "clsid:d27cdb6e-ae6d-11cf-96b8-444553540000"
     :width width
     :height height}
    [:param
     {:name "movie"
      :value source}]
    "<!--[if !IE]>-->"
    [:object
     {:type "application/x-shockwave-flash"
      :data source
      :width width
      :height height}
     [:param
      {:name "movie"
       :value source}]]
    "<!--<![endif]-->"])

(defn urlify
  [s]
  (-> s
    clojure.string/lower-case
    (clojure.string/replace #"[ -_]" "-")
    (clojure.string/replace #"[^a-z0-9\-]" "")))

(defn str->p
  [s]
  (for [line (clojure.string/split-lines s)]
    [:p line]))

(defn has-attrs?
  [form]
  {:pre [(sequential? form)]}
  (and (> (count form) 1)
       (map? (second form))))

(defn get-attrs
  [form]
  (if (has-attrs? form)
    (second form)
    {}))

(defn get-attr
  [form attr]
  (get (get-attrs form) attr))

(defn has-attr?
  [form attr]
  (and (has-attrs? form)
       (contains? (get-attrs form) attr)))

(defn set-attr
  [form attr value]
  (if (has-attrs? form)
    (assoc-in form [1 attr] value)
    (list* (first form) {attr value} (rest form))))

(defn get-classes
  [form]
  (-> form
    get-attrs
    (get :class "")
    (#(do
        (println "classes for" (get-attr form :href) "are" %)
        %))
    (clojure.string/split #" ")
    (->> (into #{}))))

(defn has-class?
  [form clazz]
  (contains? (get-classes form) clazz))

(defn add-class
  [form clazz]
  (->> form
    get-classes
    (interpose " ")
    (apply str clazz " ")
    (set-attr form :class)))

(defn has-internal-url?
  [a]
  (and (has-attr? a :href)
       (.startsWith (str (get-attr a :href)) "/")))
