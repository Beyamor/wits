(ns wits.projects.backends.message-in-a-bottle
  (:use [compojure.core :only [GET defroutes]]))

(defroutes all-routes
           (GET "/projects/message-in-a-bottle/submit" {ip :remote-addr}
                ip))
