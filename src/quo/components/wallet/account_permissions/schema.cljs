(ns quo.components.wallet.account-permissions.schema
  (:require [quo.components.wallet.required-tokens.view :as required-tokens]))

(def ?schema
  [:=>
   [:catn
    [:props
     [:map
      [:account
       [:map
        [:name [:maybe :string]]
        [:address [:maybe :string]]
        [:emoji [:maybe :string]]
        [:customization-color {:optional true} [:maybe :schema.common/customization-color]]]]
      [:token-details {:optional true} [:maybe [:sequential required-tokens/?schema]]]
      [:keycard? {:optional true} [:maybe :boolean]]
      [:checked? {:optional true} [:maybe :boolean]]
      [:disabled? {:optional true} [:maybe :boolean]]
      [:on-change {:optional true} [:maybe fn?]]
      [:container-style {:optional true} [:maybe :map]]
      [:customization-color {:optional true} [:maybe :schema.common/customization-color]]
      [:theme :schema.common/theme]]]]
   :any])
