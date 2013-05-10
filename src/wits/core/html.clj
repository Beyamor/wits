(ns wits.core.html)

(defn sections
  "Takes section-name/content pairs
   and returns a list of divs
   each of which has the provided section name and content."
  [& args]
  (for [[section-name content] (partition 2 args)]
    [:div {:class section-name} content]))
