(ns wits.web.html
  (:use [hiccup.element :only [link-to]])
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

(defn pjax-link
  "Adds a class specifying that the link is a PJAX one."
  [url content]
  (link-to {:class "pjax"} url content))

(def content-separator [:div.content-separator])
(def small-content-separator [:div.small-content-separator])

(defn paragraphs
  "Takes a buncha strings and puts each one in a paragraph."
  [& ps]
  (map (fn [p] [:p p]) ps))
