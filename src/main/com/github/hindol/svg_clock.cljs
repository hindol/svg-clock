(ns com.github.hindol.svg-clock
  (:require [com.github.hindol.svg-clock.views :as views]
            [reagent.dom :as rdom]))

(defn ^:dev/after-load start
  []
  (rdom/render [views/app]
               (.getElementById js/document "app")))

(defn init
  []
  (start))

(defn ^:dev/before-load stop
  [])
