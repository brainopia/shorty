(ns shorty.routes
  (:require [shorty.db :as db]
            [io.pedestal.impl.interceptor :refer [terminate]]
            [io.pedestal.http.body-params :refer [body-params]]
            [io.pedestal.interceptor.helpers :refer [defbefore]]
            [io.pedestal.http.route.definition :refer [defroutes]]))

(defn shorten [{{url "url"} :form-params}]
  {:status 200 :body (db/shorten url)})

(defn expand [{{url :url} :code}]
  {:status 200 :body url})

(defn redirect [{{:keys [code url]} :code}]
  (db/increment-count code)
  {:status 301 :headers {"Location" url}})

(defn stats [{{open-count :open_count} :code}]
  {:status 200 :body (str open-count)})

(defbefore find-by-code [context]
  (let [row (db/find-by-code (-> context :request :path-params :code))]
    (if row
      (assoc-in context [:request :code] row)
      (terminate context))))

(defroutes routes
  [[["/shorten" ^:interceptors [(body-params)] {:post shorten}]
    ["/" ^:interceptors [find-by-code]
      ["/expand/:code" {:get expand}]
      ["/r/:code" {:get redirect}]
      ["/statistics/:code" {:get stats}]]]])
