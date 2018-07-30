(ns dash-page.db.dao.user-dao
  (:require [clojure.java.jdbc :as jdbc]
            [dash-page.db.pool.hikari-pool :as pool]))

(defn get-user [username pass]
  (jdbc/with-db-connection
    [db-con {:datasource @pool/pool}]
    (let [rows (jdbc/query db-con ["select * from users where login = ? and password = ?" username pass])]
      (first rows))))
