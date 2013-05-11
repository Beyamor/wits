(ns wits.core.pjax
  (:use [compojure.core :only [make-route let-request]]
        [clout.core :only [route-compile]]))

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
