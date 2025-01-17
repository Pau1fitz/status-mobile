(ns quo.components.wallet.network-amount.view
  (:require
    [clojure.string :as string]
    [quo.components.markdown.text :as text]
    [quo.components.utilities.token.view :as token]
    [quo.components.wallet.network-amount.style :as style]
    [quo.theme :as quo.theme]
    [react-native.core :as rn]
    [schema.core :as schema]))

(def ?schema
  [:=>
   [:catn
    [:props
     [:map {:closed true}
      [:amount {:optional true} [:maybe :string]]
      [:token {:optional true} [:or :keyword :string]]
      [:theme :schema.common/theme]]]]
   :any])

(defn- view-internal
  [{:keys [amount token theme]}]
  [rn/view {:style style/container}
   [token/view {:token token :size :size-12}]
   [text/text
    {:weight :medium
     :size   :paragraph-2
     :style  style/text}
    (str amount " " (string/upper-case (clj->js token)))]
   [rn/view
    {:style (style/divider theme)}]])

(def view (quo.theme/with-theme (schema/instrument #'view-internal ?schema)))
