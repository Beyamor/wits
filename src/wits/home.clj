(ns wits.home
  (:require wits.core
            [markdown.core :as md]))

(defn generate!
  []
  (wits.core/generate-page!
    {:title "Words in the Sky"
     :file "index.html"
     :body (md/md-to-html-string
"Hi! I'm Tom, a terrible machine powered by sweat and anxiety. Sometimes I program!

You can check out [the blog](/blog) for nonsense rambling or [my github](https://github.com/Beyamor) for equally nonsense code.")}))