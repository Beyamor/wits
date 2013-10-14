(ns wits.projects.backends.message-in-a-bottle
  (:use [compojure.core :only [GET defroutes]]))

(defroutes all-routes
           (GET "/projects/message-in-a-bottle/submit"
                {{:keys [message]} :params ip :remote-addr}
                (if (empty? message)
                  {:status 400 :body "Dude, I dunno."}
                  (str ip " - " message))))
