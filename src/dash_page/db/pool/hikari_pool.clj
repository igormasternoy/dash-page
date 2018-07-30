(ns dash-page.db.pool.hikari-pool
  (:require [hikari-cp.core :refer :all]
            [mount.core :refer [defstate]]))

(def datasource-options {:auto-commit        true
                         :read-only          false
                         :connection-timeout 30000
                         :validation-timeout 5000
                         :idle-timeout       600000
                         :max-lifetime       1800000
                         :minimum-idle       10
                         :maximum-pool-size  10
                         :pool-name          "db-pool"
                         :adapter            "mysql"
                         :username           "bam"
                         :password           "bam"
                         :database-name      "dash_page"
                         :server-name        "localhost"
                         :port-number        3306
                         :register-mbeans    false})

(defstate pool :start (delay (make-datasource datasource-options))
               :stop (close-datasource pool))

