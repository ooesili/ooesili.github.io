(ns idempotency.core
  (:gen-class)
  (:require [idempotency.presentation :refer [presentation]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.content-type :refer [wrap-content-type]]))

(defn handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body presentation})

(def app
  (-> handler
      (wrap-resource "/")
      wrap-content-type))

(defn -main
  [& args]
  (println presentation))
