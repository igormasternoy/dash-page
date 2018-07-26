(ns dash-page.handler
  (:require [compojure.core :refer :all]
            [clojure.tools.logging :as log]
            [ring.adapter.jetty :as ring]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults api-defaults ]]
            [ring.middleware.absolute-redirects :refer [wrap-absolute-redirects]]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [hiccup.middleware :refer [wrap-base-url]]
            [mount.core :as mount :refer [defstate]]
            [dash-page.hazelcast.hazelcast :as hz-client :refer [hz-instance]]
            [dash-page.routes.home :refer [home-routes api-routes no-page]]))

(defn init []
  (log/info "guestbook is starting"))

(defn destroy []
  (log/info "dash-page is shutting down"))

(def app
  (routes (-> api-routes
              (wrap-routes wrap-json-response wrap-defaults api-defaults))
          (-> home-routes
              (wrap-routes wrap-base-url wrap-defaults site-defaults))
          no-page))

(defn start [port]
  (ring/run-jetty app {:port  port
                       :join? false}))

(defn -main []
  (mount/start #'hz-client/hz-instance)
  (selmer.parser/set-resource-path! (clojure.java.io/resource "public/"))
  (let [port (Integer. (or (System/getenv "PORT") "8080"))]
    (start port)))