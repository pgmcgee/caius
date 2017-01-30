(ns caius.core
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.json :refer [wrap-json-params]]))

(defn create-article [{:keys [params]}]
  (str "Parsing the url: " (:url params)))

(defroutes app-routes
           (GET "/" [] "<h1>Hello World</h1>")
           (POST "/article" request (create-article request))
           (route/not-found "<h1>Page not found</h1>"))

(def app
  (-> (handler/site app-routes)
      (wrap-json-params)))