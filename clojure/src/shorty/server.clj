(ns shorty.server
  (:gen-class)
  (:require [io.pedestal.http :as server]
            [shorty.routes :refer [routes]]))

(defonce server*
  (server/create-server
    {::server/routes routes
     ::server/type :immutant
     ::server/port 8080}))

(defn -main [& args]
  (server/start server*))
