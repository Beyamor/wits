(ns wits.projects.backends.message-in-a-bottle
  (:use [compojure.core :only [GET defroutes]]
        [wits.data :as data]
        korma.core
        [korma.db :only [with-db]]))

(defentity messages
           (table :miab_messages))

(def all-messages (select* messages))

(defn get-other-messages
  [ip]
  (-> all-messages
    (where (not= ip :ip))
    select))

(defn insert-message
  [ip message]
  (insert messages
          (values {:ip ip :message message})))

(defn trade-messages
  [ip message]
  (with-db data/wits-db
           (insert-message ip message)
           (let [other-messages (get-other-messages ip)]
             (if-not (empty? other-messages)
               (let [other-message (rand-nth other-messages)]
                 (:message other-message))
               "Whoa, no messages"))))

(defroutes all-routes
           (GET "/projects/message-in-a-bottle/submit"
                {{:keys [message]} :params ip :remote-addr}
                (if-not (empty? message)
                  (trade-messages ip message)
                  {:status 400 :body "Dude, I dunno."})))
