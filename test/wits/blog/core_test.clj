(ns wits.blog.core-test
  (:use clojure.test
        wits.blog.core))

(deftest can-parse-blog
         (let [contents "HEre

                        are some
                        contents"]
           (is (= {:title "title"
                   :date "2013-04-10"
                   :contents contents}
                  (parse-blog
                    (str
                      "
                      title: title
                      date: 2013-04-10

                      "
                      \newline
                      contents))))))
