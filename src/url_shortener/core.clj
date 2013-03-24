(ns url-shortener.core
  (:use compojure.handler [compojure.core :only (GET POST defroutes)])
  (:require [net.cgrand.enlive-html :as en]
            [ring.adapter.jetty :as jetty]
            [ring.util.response :as response])
  (:use clojure.pprint))

(defonce counter (atom 10000))
(defonce urls (atom {}))

(defn shorten
  [url]
  (let [id (swap! counter inc)
        id (Long/toString id 36)]
    (swap! urls assoc id url)))

(en/deftemplate homepage)

(defn redirect 
  [id]
  (response/redirect (@urls id)))


(defroutes app
  (GET "/" request (homepage request))
  (GET "/:id" [id] (redirect id)))

(defn run
  []
  (defonce server (jetty/run-jetty #'app {:port 8080 :join? false})))


53.02
