(ns wits.generate
  (:require [wits.blog]
            [wits.core]
            [wits.home])
  (:import (java.nio.file Paths)))

(defn copy-resources!
  []
  (let [input-root-path (Paths/get "resources" (into-array String []))
        output-root-path (Paths/get wits.core/output-root-name (into-array String []))]
    (doseq [[dir ext] [["js" "js"] ["css" "css"] ["images" "png"]]
            input-file (file-seq (clojure.java.io/file "resources" dir))
            :when (clojure.string/ends-with? (.getName input-file) (str "." ext))
            :let [input-path (.toPath input-file)
                  relative-path (.relativize input-root-path input-path)
                  output-path (.resolve output-root-path relative-path)
                  output (.toFile output-path)]]
      (clojure.java.io/make-parents output)
      (clojure.java.io/copy input-file output))))

(defn generate!
  []
  (try
    (clojure.java.io/make-parents wits.core/output-root)
    (copy-resources!)
    (wits.blog/generate!)
    (wits.home/generate!)
    (catch Throwable t
      (println (.getMessage t))
      (.printStackTrace t))))