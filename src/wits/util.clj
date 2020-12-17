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

(defn prefixed
  "Prefixes a bunch of strings with the given prefix."
  [prefix & strings]
  (map #(str prefix %) strings))

(defn flatten-lists
  "Flattens the lists in the top level."
  [xs]
  (reduce
    (fn [results x]
      (if (or (list? x) (seq? x))
        (concat results x)
        (conj results x)))
    [] xs))

(defn assoc-if-missing
  [m k v]
  (if (contains? m k)
    m
    (assoc m k v)))

(defn is-child-file?
  [maybe-parent child]
  (cond
    (or (not (.exists maybe-parent))
        (not (.isDirectory maybe-parent)))
    false

    (= (.getCanonicalFile maybe-parent)
       (.getCanonicalFile child))
    true

    :else
    (is-child-file? maybe-parent (.getParentFile child))))