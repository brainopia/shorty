(ns shorty.db
  (:require [shorty.base58 :refer [encode]]
            [clojure.java.jdbc :as sql]
            [environ.core :refer [env]]))

(def db
  (env :db-url))

(defn- row [query]
  (first (sql/query db query)))

(defn shorten [url]
  (let [n (-> ["SELECT nextval('code_seq')"] row :nextval)
        code (encode n)]
    (sql/insert! db :urls {:code code :url url :open_count 0})
    code))

(defn find-by-code [code]
  (row ["SELECT * FROM urls WHERE code=?", code]))

(defn increment-count [code]
  (sql/execute! db ["UPDATE urls SET open_count=open_count+1 WHERE code=?", code]))
