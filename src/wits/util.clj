(ns wits.util)

(defmacro -#>
  "Creates a lambda that takes one arg
   and threads it through the given body.
   (-#> inc str) == #(-> % inc str)"
  [& fns]
  `(fn [x#]
     (-> x# ~@fns)))

(defmacro -#>>
  "Creates a lambda that takes one arg
   and threads it through the given body.
   (-#>> inc str) == #(->> % inc str)"
  [& fns]
  `(fn [x#]
     (->> x# ~@fns)))
