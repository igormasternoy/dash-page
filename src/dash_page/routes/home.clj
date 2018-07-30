(ns dash-page.routes.home
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [selmer.parser :as parser]
            [dash-page.utils.utils :as util]
            [ring.util.response :refer [content-type response]]
            [ring.util.response :as response]
            [dash-page.db.dao.user-dao :as user-dao]
            [dash-page.hazelcast.hazelcast :as hz]
            [cheshire.core :as cheshire]
            [clojure.tools.logging :as log]))

(defn json [form]
  (-> form
      cheshire/encode
      response
      (content-type "application/json; charset=utf-8")))

(defroutes no-page
           (route/not-found "Sorry, No"))

(defroutes api-routes
           (GET "/rest/api/login" []  (response (user-dao/get-user "igor" "pass")))
           (POST "/rest/api/login" {request :body}
             (response (user-dao/get-user (:login request) (:password request))))
           (GET "/rest/api/feeds" [] (response {:name "Clojure Selmer Tutorial"}))
           (GET "/rest/api/hz/serverOverview" [] (response {:data (hz/get-data util/serverOverviewMap)})))

(defroutes home-routes
           (GET "/" [] (response/redirect "/index.html"))
           (GET "/index.html" [] (parser/render-file "index.html" {:name "Hazelcast Dash Page"}))
           (route/resources "/"))
