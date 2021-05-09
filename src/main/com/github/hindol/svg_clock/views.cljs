(ns com.github.hindol.svg-clock.views
  (:require ["react-bulma-components" :as bulma]
            [reagent.core :as r]))

(defonce timer (r/atom (js/Date.)))

(defonce timer-updater (js/setInterval #(reset! timer (js/Date.)) 1000))

(def container
  (r/adapt-react-class bulma/Container))

(def columns
  (r/adapt-react-class bulma/Columns))

(def column
  (r/adapt-react-class bulma/Columns.Column))

(defn clock
  [time]
  (let [hour   (.getHours time)
        minute (.getMinutes time)
        second (.getSeconds time)]
    [:svg {:view-box "0 0 100 100"}
     [:circle#face {:cx 50 :cy 50 :r 45}]
     [:g#ticks
      (for [i (range 0 60 5)]
        ^{:key i}
        [:line.tick {:x1 50
                     :y1 6
                     :x2 50
                     :y2 10
                     :transform (str "rotate(" (* 6 i) " 50 50)")}])]
     [:g#hands
      [:rect#hour-hand {:x         47.5
                        :y         22.5
                        :width     5
                        :height    30
                        :rx        2.5
                        :ry        2.5
                        :transform (str "rotate(" (+ (* 30 (rem hour 12))
                                                     (/ minute 2)) " 50 50)")}]
      [:rect#minute-hand {:x         48.5
                          :y         12.5
                          :width     3
                          :height    40
                          :rx        2
                          :ry        2
                          :transform (str "rotate(" (* 6 minute) " 50 50)")}]
      [:line#second-hand {:x1        50
                          :y1        50
                          :x2        50
                          :y2        16
                          :transform (str "rotate(" (* 6 second) " 50 50)")}]]]))

(defn app
  []
  [container
   [columns {:centered true}
    [column {:size "half"} [clock @timer]]]])
