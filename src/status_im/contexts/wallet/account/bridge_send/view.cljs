(ns status-im.contexts.wallet.account.bridge-send.view
  (:require
    [quo.theme :as quo.theme]
    [react-native.core :as rn]
    [status-im.contexts.wallet.account.bridge-send.style :as style]
    [status-im.contexts.wallet.common.utils :as utils]
    [status-im.contexts.wallet.send.input-amount.view :as input-amount]
    [utils.re-frame :as rf]))

(defn- view-internal
  []
  (let [send-bridge-data (rf/sub [:wallet/wallet-send])
        token            (:token send-bridge-data)
        total-balance    (utils/total-token-units-in-all-chains token)
        limit-crypto     (utils/get-standard-crypto-format token total-balance)
        crypto-decimals  (utils/get-crypto-decimals-count token)
        send-type        (:type send-bridge-data)]
    [rn/view {:style style/bridge-send-wrapper}
     [input-amount/view
      {:crypto-decimals crypto-decimals
       :limit-crypto    limit-crypto
       :type            send-type}]]))

(def view (quo.theme/with-theme view-internal))