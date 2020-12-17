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
  "![me](http://i.imgur.com/hOyQCFS.png)

  Hey there. I'm Tom Gibson.
  
  I'm a software engineer who gets excited about programming languages, procedural generation, and cardigans.

  While you're here, feel free to read my latest rambling over in [the blog](/blog) or check out some of my [projects](/projects). For more of me, hit up my [GitHub](https://github.com/Beyamor) and [Stack Overflow](http://stackoverflow.com/users/1308287/beyamor) accounts.

  Got questions or just want to shoot the breeze? Find me on <a href=\"https://www.linkedin.com/in/tom-gibson-60a6b9156\">LinkedIn</a>.")

(def home-page
  {:title
   "Words in the Sky"

   :content
   [:div.page
    (-> content
        md-to-html-string
        html->hiccup)]})