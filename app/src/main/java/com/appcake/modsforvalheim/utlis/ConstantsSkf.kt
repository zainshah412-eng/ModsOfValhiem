package com.appcake.modsforvalheim.utlis

import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustEvent
import com.adjust.sdk.AdjustPlayStoreSubscription
import com.pushwoosh.Pushwoosh
import com.pushwoosh.tags.Tags

open  class ConstantsSkf {

    companion object {
        const val ADJUST_EXIT_SUB_EVENT = "" // when user click on cross icon in Nomral subscription screen


        const val ADJUST_NOTIFICATION_SHOWED_EVENT = "" //when we show notification

        const val ADJUST_NOTIFICATION_CLICKED_EVENT = ""  // when user click on notification


        const val ADJUST_DISCOUNT_EXIT_EVENT = "" // when user click on cross icon in Special subscription screen


        const val ADJUST_DISCOUNT_PROMO_PAY = "" // when user click on purchase in Special subscription screen
        fun logSubscriptionEvent(
            price: Long,
            currency: String,
            sku: String,
            orderId: String,
            signature: String,
            purchaseToken: String,
            purchaseTime: Long
        ) {

            val subscription = AdjustPlayStoreSubscription(

                price,

                currency,

                sku,

                orderId,

                signature,

                purchaseToken

            )

            subscription.purchaseTime = purchaseTime

            Adjust.trackPlayStoreSubscription(subscription)

        }

        fun adjustEvent(eventName: String) {

            Adjust.trackEvent(AdjustEvent(eventName))

        }


        fun pushWooshTagsForSubscription(tag: Boolean) {

            Pushwoosh.getInstance().setTags(Tags.booleanTag("SubscriptionPurchased", tag))

        }
    }
}