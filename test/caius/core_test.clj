(ns caius.core-test
  (:require [clojure.test :refer :all]
            [caius.core :refer :all]
            [ring.mock.request :as mock]))

(deftest app-test
  (testing "Create a basic article"
    (is (= (app (mock/request :post "/article" {:url "http://www.example.com/"}))
           {:status 200,
            :headers {"Content-Type" "text/html; charset=utf-8"},
            :body "Parsing the url: http://www.example.com/"}))))
