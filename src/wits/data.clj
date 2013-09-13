(ns wits.data
  (:use [korma.db :only [defdb mysql]]))

(defn- parse-config
  [config-file]
  (let [prop (java.util.Properties.)]
    (.load prop (java.io.FileInputStream. config-file))
    {:db  (.getProperty prop "database")
     :user  (.getProperty prop "user")
     :password  (.getProperty prop "password")
     :host  (.getProperty prop "host")}))

(->> "db.conf"
  parse-config
  mysql
  (defdb wits-db))
