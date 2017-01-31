(ns caius.core
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [bouncer.core :as b]
            [bouncer.validators :as v]
            [ring.middleware.json :refer [wrap-json-params]]
            [ring.util.response :refer [response status]]
            [ring.middleware.defaults :as middleware]
            [clojure.core.async :as async]))

(defn grab-dom [url]
  (let [c (async/chan)]
    (async/go )))

(defn validate-article-params [params]
  (first
   (b/validate params
               :url v/required)))

(defn create-article [{:keys [params]}]
  (if-let [errors (validate-article-params params)]
    (-> (response errors)
        (status 400))
    (str "Parsing the url: " (:url params))))

(defroutes app-routes
  (GET "/" [] "<h1>Hello World</h1>")
  (POST "/article" request (create-article request))
  (route/not-found "<h1>Page not found</h1>"))

(def app
  (-> (middleware/wrap-defaults app-routes middleware/api-defaults)
      (wrap-json-params)))
