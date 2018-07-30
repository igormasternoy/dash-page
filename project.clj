(defproject dash-page "0.1.0-SNAPSHOT"
  :description "Dash Page for Hazelcast Map"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 ;Logging
                 [org.clojure/tools.logging "0.4.1"]
                 [org.slf4j/slf4j-log4j12 "1.7.1"]
                 [log4j/log4j "1.2.17" :exclusions [javax.mail/mail
                                                    javax.jms/jms
                                                    com.sun.jmdk/jmxtools
                                                    com.sun.jmx/jmxri]]
                 ;DATABASE
                 [org.clojure/java.jdbc "0.7.7"]
                 [mysql/mysql-connector-java "5.1.44"]
                 [hikari-cp "2.6.0"]
                 ;WEB
                 [compojure "1.5.2"]
                 [cheshire "5.8.0"]
                 [hiccup "1.0.5"]
                 [ring-server "0.4.0"]
                 [ring/ring-json "0.4.0"]
                 [ring-json-response "0.2.0"]
                 [ring-logger "1.0.1"]
                 [ring/ring-defaults "0.3.2"]
                 [ring-middleware-format "0.7.2"]
                 [selmer "1.11.8"]
                 ;Supporting
                 [mount "0.1.12"]
                 ;HZ
                 [chazel "0.1.17" :exclusions [com.hazelcast/hazelcast]]
                 [com.hazelcast/hazelcast "3.9.4"]]
  :plugins [[lein-ring "0.8.12"]]
  :ring {:handler dash-page.handler/app
         :init dash-page.handler/init
         :destroy dash-page.handler/destroy}
  :profiles
  {:uberjar {:aot :all}
   :production
   {:ring
    {:open-browser? false, :stacktraces? false, :auto-reload? false}}
   :dev
   {:dependencies [[ring-mock "0.1.5"] [ring/ring-devel "1.5.1"]]}})
