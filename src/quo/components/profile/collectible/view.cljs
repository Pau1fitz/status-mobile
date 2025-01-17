(ns quo.components.profile.collectible.view
  (:require
    [quo.components.markdown.text :as text]
    [quo.components.profile.collectible.style :as style]
    [react-native.core :as rn]
    [react-native.svg :as svg]))

(defn remaining-tiles
  [amount]
  [rn/view {:style (merge style/bottom-right (style/remaining-tiles))}
   [text/text
    {:style  (style/remaining-tiles-text)
     :size   :paragraph-2
     :weight :medium}
    (str "+" amount)]])

(defn tile
  [{:keys [style size resource]}]
  (let [svg?        (and (map? resource) (:svg? resource))
        image-style (style/tile-style-by-size size)]
    [rn/view {:style style}
     (if svg?
       [rn/view
        {:style {:border-radius (:border-radius image-style)
                 :overflow      :hidden}}
        [svg/svg-uri (assoc image-style :uri (:uri resource))]]
       ;; NOTE: using react-native-fast-image here causes a crash on devices when used inside a
       ;; large flatlist. The library seems to have issues with memory consumption when used with
       ;; large images/GIFs.
       ;;
       ;; https://github.com/DylanVann/react-native-fast-image/issues/195
       [rn/image
        {:style  image-style
         :source (if (string? resource)
                   {:uri      resource
                    :priority :low}
                   resource)}])]))

(defn two-tiles
  [{:keys [images size]}]
  [:<>
   [tile
    {:style    style/top-left
     :size     size
     :resource (first images)}]
   [tile
    {:style    style/bottom-right
     :size     size
     :resource (second images)}]])

(defn three-tiles
  [{:keys [tiles size]}]
  (let [[image-1 image-2 image-3 & _] tiles]
    [:<>
     [tile
      {:style    style/top-left
       :size     size
       :resource image-1}]
     [tile
      {:style    style/top-right
       :size     size
       :resource image-2}]
     [tile
      {:style    style/bottom-left
       :size     size
       :resource image-3}]]))

(defn tile-container
  [{:keys [images]}]
  (let [num-images (count images)]
    (case num-images
      1 [tile
         {:resource (first images)
          :size     :xl}]
      2 [two-tiles
         {:images images
          :size   :lg}]
      3 [three-tiles
         {:tiles images
          :size  :md}]
      (let [[first-three-images remaining-images] (split-at 3 images)]
        [:<>
         [three-tiles {:tiles first-three-images :size :md}]
         [rn/view {:style style/tile-sub-container}
          (case num-images
            4 [tile
               {:resource (nth images 3 nil)
                :size     :md}]
            5 [two-tiles
               {:images (take 2 remaining-images)
                :size   :sm}]
            6 [three-tiles
               {:tiles (take 3 remaining-images)
                :size  :xs}]
            7 [:<>
               [three-tiles
                {:tiles (take 3 remaining-images)
                 :size  :xs}]
               [tile
                {:style    style/bottom-right
                 :size     :xs
                 :resource (nth remaining-images 3 nil)}]]
            [:<>
             [three-tiles
              {:tiles (take 3 remaining-images)
               :size  :xs}]
             [remaining-tiles (- (count remaining-images) 3)]])]]))))

(defn collectible
  [{:keys [images on-press]}]
  [rn/view {:style style/tile-outer-container}
   [rn/pressable
    {:on-press on-press
     :style    style/tile-inner-container}
    [tile-container {:images images}]]])
