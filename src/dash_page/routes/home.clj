(ns dash-page.routes.home
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [selmer.parser :as parser]
            [dash-page.utils.utils :as util]
            [ring.util.response :refer [content-type response]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.util.response :as response]
            [dash-page.hazelcast.hazelcast :as hz]))

(defroutes no-page
           (route/not-found "Sorry, No"))

(defroutes api-routes
           (GET "/rest/api/feeds" [] (response {:name "Clojure Selmer Tutorial"}))
           (GET "/rest/api/hz/serverOverview" [] (response {:data (hz/get-data util/serverOverviewMap)})))

(defroutes home-routes
           (GET "/" [] (response/redirect "/index.html"))
           (GET "/index.html" [] (parser/render-file "index.html" {:name "Hazelcast Dash Page"}))
           (route/resources "/"))
