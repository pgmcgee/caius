(defproject caius "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.2"]
                 [ring/ring-json "0.4.0"]
                 [ring/ring-mock "0.3.0"]
                 [ring/ring-jetty-adapter "1.5.1"]
                 [bouncer "1.0.0"]
                 [ring/ring-defaults "0.2.1"]
                 [org.clojure/core.async "0.2.395"]
                 [environ "1.1.0"]
                 [org.seleniumhq.selenium/selenium-server "3.0.1"]
                 [org.jsoup/jsoup "1.10.2"]]
  :plugins [[lein-ring "0.10.0"]]
  :ring {:handler caius.core/app})
