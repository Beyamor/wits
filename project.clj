(defproject wits "0.1.0-SNAPSHOT"
            :description "My lovely little website"
            :url "http://www.wordsinthesky.com"
            :license {:name "Eclipse Public License"
                      :url "http://www.eclipse.org/legal/epl-v10.html"}
            :dependencies [[org.clojure/clojure "1.4.0"]
                           [compojure "1.1.5"]
                           [hiccup "1.0.0"]
                           [enlive "1.1.1"]
                           [markdown-clj "0.9.21"]
                           [clout "1.1.0"]
                           [org.clojure/data.json "0.2.2"]
                           [mysql/mysql-connector-java "5.1.6"]
                           [korma "0.3.0-RC5"]
                           [log4j "1.2.15" :exclusions [javax.mail/mail
                                                        javax.jms/jms
                                                        com.sun.jdmk/jmxtools
                                                        com.sun.jmx/jmxri]]]
            :plugins [[lein-ring "0.8.3"]]
            :ring {:handler wits.handler/app}
            :profiles {:prod {:ring {:port 13994}}})
