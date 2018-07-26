(ns dash-page.hazelcast.hazelcast
  (:require [chazel.core :as hz]
            [mount.core :refer [defstate]]
            [dash-page.hazelcast.serializers :as serializers])
  (:import (com.hazelcast.client.config ClientConfig)
           (com.hazelcast.config GroupConfig)
           (dash_page.hazelcast.serializers Valuable)))

(defn client-config [{:keys [hosts retry-ms retry-max group-name group-password near-cache]
                      :or {hosts ["127.0.0.1"]
                           retry-ms 5000
                           retry-max 720000
                           group-name "dev"
                           group-password "dev-pass"}}]
  (let [config (ClientConfig.)
        groupConfig (GroupConfig. group-name group-password)
        near-cache (when near-cache
                     (hz/near-cache-config near-cache))]
    (doto config
      (.getNetworkConfig)
      (.addAddress (into-array hosts))
      (.setConnectionAttemptPeriod retry-ms)
      (.setConnectionAttemptLimit retry-max))
    (-> config
      (.getSerializationConfig)
      (.addDataSerializableFactory 1 (serializers/get-data-serializable-factory)))
    (.setGroupConfig config groupConfig)
    (hz/call .addNearCacheConfig config near-cache)  ;; only set near cache config if provided
    config
    )
  )

(defn start-hazelcast-client []
  (with-redefs [chazel.core/client-config (fn [input] (client-config input))]
    (hz/client-instance {:group-name "dev",
                         :group-password "dev-pass",
                         :hosts ["127.0.0.1"],
                         :retry-ms 5000,
                         :retry-max 720000})))

(defstate hz-instance :start (start-hazelcast-client)
          :stop (hz/shutdown-client hz-instance))

(defn get-data [map] (let [value (hz/hz-map map hz-instance)
                           vals (.values value)]
                       (for [^Valuable val vals]
                         (serializers/get-value val))))