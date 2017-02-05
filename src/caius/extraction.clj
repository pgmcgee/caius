(ns caius.extraction
  (:import [org.jsoup Jsoup]
           [org.jsoup.nodes Document Element]
           [org.jsoup.select Elements]
           [java.text BreakIterator]))

(defn- create-doc [dom-string]
  (Jsoup/parse dom-string "UTF-8"))

(defn- get-all-elements [doc]
  (.select doc "*"))

(defn- create-boundary [text]
  (let [boundary (BreakIterator/getSentenceInstance)]
    (.setText boundary text)
    boundary))

(defn- iterator-boundary [boundary]
  (let [current (atom 0)]
     (reify java.util.Iterator
       (hasNext [this] (not= @current BreakIterator/DONE))
       (next [this] (let [next (.next boundary)]
                      (reset! current next))))))

(defn- boundary-seq [boundary]
  (-> (iterator-seq (iterator-boundary boundary))
      (drop-last)))

(defn- number-of-sentences [text]
  (let [boundary (create-boundary text)]
    (count (boundary-seq boundary))))

(defn- get-large-elements [doc]
  (->> (get-all-elements doc)
       (remove (fn [e] (let [own-text (.ownText e)]
                         (or (= (.length own-text) 0)
                             (< (number-of-sentences own-text) 2)))))))

(defn- get-parents-from-vector [elements]
  (group-by #(.parent %) elements))

(defn- get-parents-from-map [map-elements]
  (reduce-kv (fn [m k v]
               (let [parent (.parent k)]
                 (merge-with concat m {parent v})))
             {} map-elements))

;; (defn- test-test-test [map-elements]
;;   (reduce-kv (fn [m k v]
;;                (let [parent (second (str k))]
;;                  (merge-with concat m {parent v})))
;;              {} map-elements))

(defn- get-largest-parent [map-elements]
  (->
   (into (sorted-map-by (fn [p1 p2] (compare
                                     (count (get map-elements p1))
                                     (count (get map-elements p2)))))
         map-elements)
   (keys)
   (last)))

(defn- parent-size [parents parent]
  (count (get parents parent)))

(defn- output-article [parent]
  (let [elements (.select parent "*")]
    (map #(.ownText %)
         (filter #(let [text (.ownText %)] (or (> (count text) 0)
                                               (.contains text ".")))
                 elements))))

(defn extract-article [dom-string]
  (let [doc (create-doc dom-string)
        elements (get-all-elements doc)
        large-elements (get-large-elements elements)
        parents (get-parents-from-vector large-elements)
        largest-parent (get-largest-parent parents)
        largest-parent-size (parent-size parents largest-parent)]
    (if (< (/ largest-parent-size (count large-elements)) 0.8)
      (let [new-parents (get-parents-from-map parents)
            new-largest-parent (get-largest-parent new-parents)]
        (output-article new-largest-parent))
      (output-article largest-parent))))
