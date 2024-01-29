(ns status-im.contexts.wallet.account.bridge-send.view
  (:require

   [react-native.core :as rn]
   [status-im.contexts.wallet.common.account-switcher.view :as account-switcher]
   [utils.re-frame :as rf]))

(defn view []
  [rn/view
   [account-switcher/view
    {:on-press            #(rf/dispatch [:navigate-back-within-stack :wallet-bridge-send])
     :icon-name           :i/arrow-left
     :accessibility-label :top-bar}]])