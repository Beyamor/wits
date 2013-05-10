(defproject wits "0.1.0-SNAPSHOT"
            :description "My lovely little website"
            :url "http://www.wordsinthesky.com"
            :license {:name "Eclipse Public License"
                      :url "http://www.eclipse.org/legal/epl-v10.html"}
            :dependencies [[org.clojure/clojure "1.4.0"]
                           [compojure "1.1.5"]
                           [hiccup "1.0.0"]
                           [markdown-clj "0.9.21"]]
            :plugins [[lein-ring "0.8.3"]]
            :ring {:handler wits.handler/app})
