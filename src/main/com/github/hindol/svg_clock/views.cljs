(ns com.github.hindol.svg-clock.views
  (:require ["react-bulma-components" :as bulma]
            [reagent.core :as r]
            [reagent.ratom :as ratom]))

(defonce timer (r/atom (js/Date.)))

(defonce timer-updater (js/setInterval #(reset! timer (js/Date.)) 1000))

(def container
  (r/adapt-react-class bulma/Container))

(def columns
  (r/adapt-react-class bulma/Columns))

(def column
  (r/adapt-react-class bulma/Columns.Column))

(def form-field
  (r/adapt-react-class bulma/Form.Field))

(def form-label
  (r/adapt-react-class bulma/Form.Label))

(def form-control
  (r/adapt-react-class bulma/Form.Control))

(def form-input
  (r/adapt-react-class bulma/Form.Input))

(defn clock
  [time]
  (let [hour   (.getHours time)
        minute (.getMinutes time)
        second (.getSeconds time)]
    [:svg {:view-box "0 0 100 100"}
     [:circle#face {:cx 50 :cy 50 :r 45}]
     [:g#ticks
      (for [i (range 0 60 1)]
        ^{:key i}
        [:line.tick {:x1 50
                     :y1 6
                     :x2 50
                     :y2 (if (= 0 (rem i 5))
                           10
                           7.5)
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
                          :transform (str "rotate(" (* 6 second) " 50 50)")}]
      [:circle#pin {:cx 50 :cy 50 :r 1.5}]]]))

(defn form
  []
  [:form
   [form-field
    [form-label "Background"]
    [form-control
     [form-input {:type          "color"
                  :default-value "#84A59D"}]]]
   [form-field
    [form-label "Face"]
    [form-control
     [form-input {:type          "color"
                  :default-value "#F6BD60"}]]]
   [form-field
    [form-label "Hour & Minute Hand"]
    [form-control
     [form-input {:type          "color"
                  :default-value "#F5CAC3"}]]]
   [form-field
    [form-label "Second Hand"]
    [form-control
     [form-input {:type          "color"
                  :default-value "#F28482"}]]]])

(defn throttle
  [f interval]
  (js/goog.functions.throttle f interval))

(defn app
  []
  (let [client-height  js/document.documentElement.clientHeight
        scroll-top     (r/atom js/document.documentElement.scrollTop)
        visible-height (ratom/reaction (- client-height @scroll-top))]
    (.addEventListener js/document
                       "scroll"
                       (throttle (fn []
                                   (js/console.log "Throttled event...")
                                   (reset! scroll-top js/document.documentElement.scrollTop))
                                 100))
    (fn []
      [container {:breakpoint "fluid"}
       [columns {:class     "is-gapless"
                 :multiline true}
        [column {:size "full"}
         [columns {:id         "clock-container"
                   :class      "is-gapless"
                   :centered   true
                   :v-centered true
                   :style      {:height client-height}}
          [column {:size "half"}
           [clock @timer]]]]
        [column {:size   "full"}
         [columns {:id         "form-container"
                   :centered   true
                   :v-centered true
                   :style      {:height (/ client-height 2)}}
          [column {:size "half"}
           [form]]]]]])))
