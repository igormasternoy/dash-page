(ns dash-page.handler
  (:require [compojure.core :refer :all]
            [clojure.tools.logging :as log]
            [ring.adapter.jetty :as ring]
            [compojure.handler :as handler]

            [ring.middleware.defaults :refer [wrap-defaults site-defaults api-defaults ]]
            [ring.middleware.format-params :refer [wrap-json-params]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.cookies :refer [wrap-cookies]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.middleware.absolute-redirects :refer [wrap-absolute-redirects]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [hiccup.middleware :refer [wrap-base-url]]
            [ring.logger :as logger]
            [mount.core :as mount :refer [defstate]]
            [dash-page.db.dao.user-dao :as user-dao]
            [dash-page.db.pool.hikari-pool :as hikari-pool]
            [dash-page.hazelcast.hazelcast :as hz-client :refer [hz-instance]]
            [dash-page.routes.home :refer [home-routes api-routes no-page]]))

(defn init []
  (log/info "guestbook is starting"))

(defn destroy []
  (log/info "dash-page is shutting down"))

(def app
  (routes (-> api-routes
              (wrap-json-body {:keywords? true})
              (wrap-json-response {:pretty true})
              (wrap-defaults api-defaults))
          (-> home-routes
              (wrap-routes wrap-base-url wrap-defaults site-defaults))
          no-page))

(defn start [port]
  (ring/run-jetty (logger/wrap-with-logger app) {:port  port
                       :join? false}))

(defn -main []
  (mount/start #'hz-client/hz-instance #'hikari-pool/pool)
  (log/info (user-dao/get-user "igor" "pass"))
  (selmer.parser/set-resource-path! (clojure.java.io/resource "public/"))
  (let [port (Integer. (or (System/getenv "PORT") "8080"))]
    (start port)))