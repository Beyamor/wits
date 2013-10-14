(ns wits.projects.backends.message-in-a-bottle
  (:use [compojure.core :only [GET defroutes]]))

(defroutes all-routes
           (GET "/projects/message-in-a-bottle/submit" {{:keys [message]} :params ip :remote-addr}
                (str ip " - " message)))
