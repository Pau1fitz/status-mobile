(ns status-im.contexts.wallet.account.bridge-to.view
  (:require
   [clojure.string :as string]
   [quo.components.markdown.text :as text]
   [quo.core :as quo]
   [quo.foundations.colors :as colors]
   [quo.foundations.resources :as quo.resources]
   [react-native.core :as rn]
   [status-im.contexts.wallet.account.bridge-to.style :as style]
   [status-im.contexts.wallet.common.account-switcher.view :as account-switcher]
   [status-im.contexts.wallet.common.utils :as utils]
   [utils.i18n :as i18n]
   [utils.re-frame :as rf]))


(defn get-balance-for-chain
  [data chain-id]
  (->> (vals data)
       (filter #(= chain-id (:chain-id %)))
       (first)))

(defn- bridge-token-component
  []
  (fn [network token]
    (let [network          (rf/sub [:wallet/network-details-by-chain-id (:chain-id network)])
          all-balances     (:balances-per-chain token)
          balance          (get-balance-for-chain all-balances (:chain-id network))
          crypto-formatted (:balance balance)
          currency        (rf/sub [:profile/currency])
          currency-symbol  (rf/sub [:profile/currency-symbol])
          fiat-value       (utils/total-token-fiat-value currency token)
          fiat-formatted   (utils/get-standard-fiat-format crypto-formatted currency-symbol fiat-value)]
      (print "----- network")
      (print network)
      ;; (print token)

      [rn/pressable
       {:style {:flex-direction     :row
                :align-items        :center
                :justify-content    :space-between
                :padding-horizontal 12
                :padding-vertical   8
                :border-radius      12
                :height             56}}
       [rn/view
        {:style {:flex-direction :row
                 :align-items    :center
                 :gap            12}}
        [rn/image
         {:source (quo.resources/get-network (:network-name network))
          :style  style/image}]
        [rn/text
         {:style {:font-weight :bold :text-transform :capitalize}}
         (:network-name network)]]

       [rn/view {:style {:flex-direction :column}}
        [rn/view
         {:style {:flex-direction :row
                  :justify-content :flex-end}}
         [rn/text {:style {:text-transform :uppercase :text-align :right}}
          (:short-name network)]
         [rn/text " " crypto-formatted]]

        [rn/view
         {:style {:flex-direction :row
                  :justify-content :flex-end}}
         [rn/text fiat-formatted]]]])))

(defn view
  []
  (let [send-bridge-data (rf/sub [:wallet/wallet-send])
        token            (:token send-bridge-data)
        token-symbol            (:symbol token)
        network-details  (rf/sub [:wallet/network-details])
        mainnet          (first network-details)
        layer-2-networks (rest network-details)
        account  (rf/sub [:wallet/current-viewing-account])
        tokens (:tokens account)
        account-token  (first (filter #(= token-symbol (% :symbol)) tokens))]
    ;; (print account-token)
    [rn/view
     [account-switcher/view
      {:on-press            #(rf/dispatch [:navigate-back-within-stack :wallet-bridge-to])
       :icon-name           :i/arrow-left
       :accessibility-label :top-bar}]

     [quo/text-combinations
      {:container-style style/header-container
       :title           (i18n/label :t/bridge-to {:name (string/upper-case (str (:label token)))})}]

     [rn/view style/mainnet-container
      [bridge-token-component mainnet account-token]]

     [rn/view
      {:style {:border-bottom-width 1
               :border-bottom-color colors/neutral-10}}]
     [quo/text-combinations
      {:container-style style/description-container
       :description     (i18n/label :t/layer-2)}]
     [rn/flat-list
      {:data                    layer-2-networks
       :render-fn               (fn [network]
                                  [bridge-token-component network account-token])
       :content-container-style style/list-content-container}]]))
