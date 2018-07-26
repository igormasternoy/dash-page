(ns dash-page.hazelcast.serializers
  (:require [dash-page.utils.utils :as util]
            [clojure.tools.logging :as log])
  (:import (com.hazelcast.nio.serialization DataSerializableFactory IdentifiedDataSerializable)
           (com.hazelcast.nio ObjectDataOutput ObjectDataInput)))

(defprotocol Valuable
  (get-value [this]))

(defn get-jmx-server-overview []
  (let [field (atom nil)]
    (reify Valuable
      (get-value [_] @field)
      IdentifiedDataSerializable
      (getFactoryId [_] util/object-id-server-overview-factory-id)
      (getId [_] util/object-id-server-overview)
      (^void readData [_ ^ObjectDataInput input] (reset! field {:name      (.readUTF input)
                                                                :node      (.readUTF input)
                                                                :privateIp (.readUTF input)
                                                                :publicIp  (.readUTF input)
                                                                ;
                                                                :releaseNumber (.readUTF input)
                                                                :buildNumber (.readUTF input)
                                                                }))
      (^void writeData [_ ^ObjectDataOutput output] (log/info "WRITTEN INPUT!!!")))))

(defn get-data-serializable-factory []
  (reify DataSerializableFactory
    (create [_ input]
      (cond
        (= util/object-id-server-overview input) (get-jmx-server-overview)
        :else (throw (RuntimeException. "Serializer type not found"))))))

