(ns caius.core-test
  (:require [clojure.test :refer :all]
            [caius.core :refer :all]
            [ring.mock.request :as mock]))

(deftest article-handler-test
  (testing "Create a basic article"
    (is (= (create-article (mock/request :post "/article"))
           "Parsing the url: "))))