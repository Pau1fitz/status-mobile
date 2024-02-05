(ns status-im.contexts.wallet.send.send-amount.view
  (:require
    [quo.theme]
    [status-im.contexts.wallet.send.input-amount.view :as input-amount]
    [utils.re-frame :as rf]))

(defn- view-internal
  []
  [input-amount/view
   {:on-navigate-back (fn []
                        (rf/dispatch [:navigate-back-within-stack :wallet-send-input-amount]))}])

(def view (quo.theme/with-theme view-internal))
