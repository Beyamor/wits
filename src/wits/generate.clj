(ns wits.generate
  (:require wits.core
            wits.blog))

(defn copy-resources!
  []
  (doseq [file-type ["js" "css"]
          input (file-seq (clojure.java.io/file "resources" file-type))
          :when (clojure.string/ends-with? (.getName input) (str "." file-type))
          :let [output (clojure.java.io/file wits.core/output-root file-type (.getName input))]]
    (clojure.java.io/make-parents output)
    (clojure.java.io/copy input output)))

(defn generate!
  []
  (try
    (clojure.java.io/make-parents wits.core/output-root)
    (copy-resources!)
    (wits.blog/generate!)
    (catch Throwable t
      (println (.getMessage t))
      (.printStackTrace t))))