(defproject wits "0.1.0-SNAPSHOT"
  :description "My lovely little website"
  :url "http://www.wordsinthesky.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-auto "0.1.3"]
            [lein-exec "0.3.7"]]
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [hiccup "1.0.0"]
                 [hickory "0.7.1"]
                 [enlive "1.1.1"]
                 [markdown-clj "0.9.21"]
                 [lonocloud/synthread "1.0.4"]
                 [org.apache.commons/commons-text "1.9"]]
  :aliases {"build-site" ["exec" "-ep" "(require 'wits.generate) (wits.generate/generate!)"]}
  :auto {"build-site" {:paths ["src"
                               "blogs"
                               "resources"]
                       :file-pattern #"\.(clj|cljs|cljx|cljc|js|css|blarg)$"}})