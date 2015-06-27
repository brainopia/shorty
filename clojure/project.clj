(def db-url
  (or (System/getenv "DB_URL") 
      "jdbc:postgresql://localhost/demo"))

(defproject shorty "1.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [io.pedestal/pedestal.service "0.4.0"]
                 [io.pedestal/pedestal.immutant "0.4.0"]
                 [org.clojure/java.jdbc "0.3.7"]
                 [org.postgresql/postgresql "9.4-1201-jdbc41"]
                 [environ "1.0.0"]
                 [ragtime/ragtime.sql.files "0.3.8"]
                 [org.clojure/tools.namespace "0.2.10"]
                 [org.slf4j/jul-to-slf4j "1.7.7"]]
  :plugins [[ragtime/ragtime.lein "0.3.8"]
            [lein-environ "1.0.0"]]
  :profiles {:uberjar {:aot :all}
             :shared {:env {:db-url db-url}}
             :dev [:shared]}
  :ragtime {:migrations ragtime.sql.files/migrations
            :database ~db-url}
  :main shorty.server)
