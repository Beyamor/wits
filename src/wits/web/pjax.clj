(ns wits.web.pjax
  (:use [compojure.core :only [make-route let-request]]
        [clout.core :only [route-compile]]
        [hiccup.element :only [javascript-tag]])
  (:require [wits.web.pages :as pages]))

(defn- prepare-route
  "Pre-compile the route.
   Copied from compojure.core."
  [route]
  (cond
    (string? route)
    `(route-compile ~route)
    (vector? route)
    `(route-compile
       ~(first route)
       ~(apply hash-map (rest route)))
    :else
    `(if (string? ~route)
       (route-compile ~route)
       ~route)))

(defmacro PJAX
  "Generate a PJAX route."
  [route bindings & body]
  `(make-route
     :get ~(prepare-route route)
     (fn [request#]
       (let-request [~bindings request#]
                    (let [~'pjax? (-> request# :headers (get "x-pjax"))]
                      ~@body)))))

(defn page
  "Given a full page template and the content attributes,
   this will either return a full page or the given content
   if the request is a PJAX one."
  [full-page-template pjax? page-attributes]
  (let [content (pages/as-content page-attributes)]
    (if pjax?
      content
      (pages/main content))))
