(defproject dash-page "0.1.0-SNAPSHOT"
  :description "Dash Page for Hazelcast Map"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 ;Logging
                 [org.clojure/tools.logging "0.4.1"]
                 [org.slf4j/slf4j-log4j12 "1.7.1"]
                 [log4j/log4j "1.2.17" :exclusions [javax.mail/mail
                                                    javax.jms/jms
                                                    com.sun.jmdk/jmxtools
                                                    com.sun.jmx/jmxri]]
                 ;WEB
                 [compojure "1.5.2"]
                 [hiccup "1.0.5"]
                 [ring-server "0.4.0"]
                 [ring/ring-json "0.4.0"]
                 [ring/ring-defaults "0.3.2"]
                 [selmer "1.11.8"]
                 ;Supporting
                 [mount "0.1.12"]
                 ;HZ
                 [chazel "0.1.17"]
                 ]
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
