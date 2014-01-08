(ns wits.home.views
  (:use hiccup.element
        [hiccup.util :only [escape-html]]
        [hiccup.page :only [html5 include-css include-js]]
        [wits.web.html :only [sections html->hiccup html->enlive]]
        [markdown.core :only [md-to-html-string]])
  (:require [markdown.core :as md]
            [wits.web.pages :as pages]
            [wits.web.html :as html]
            [wits.web.pjax :as pjax]))

(def content
  "![me](http://i.imgur.com/MR1ZxOo.jpg)

  Hey there. I'm Tom Gibson.
  
  I'm a software engineer from UVic who gets excited about programming languages, procedural generation, and cardigans.
  
  While you're here, feel free to read my latest rambling over in [the blog](/blog) or check out some of my [projects](/projects). For more of me, hit up my [GitHub](https://github.com/Beyamor) and [Stack Overflow](http://stackoverflow.com/users/1308287/beyamor) accounts.

  Got questions or just want to shoot the breeze? Let's talk.")

(defn home-page
  [pjax?]
  (pjax/page
    pages/main pjax?
    {:title
     "Words in the Sky"

     :content
     [:div.page
      (md-to-html-string content)]}))
