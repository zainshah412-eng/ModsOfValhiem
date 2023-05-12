package com.appcake.modsforvalheim.notification

import android.content.Intent
import com.appcake.modsforvalheim.core.ui.MainActivity
import com.pushwoosh.notification.NotificationServiceExtension
import com.pushwoosh.notification.PushMessage
import org.json.JSONException
import org.json.JSONObject


class MyNotificationServiceExtension : NotificationServiceExtension() {
    override fun startActivityForPushMessage(message: PushMessage) {

        // super.startActivityForPushMessage() starts default launcher activity

        // or activity marked with ${applicationId}.MESSAGE action.

        // Simply do not call it to override this behaviour.

        // super.startActivityForPushMessage(message);
        var isPromoAction = false
        try {
            val jsonObject = JSONObject(message.customData)
            if (jsonObject.has("pushwoosh")) {
                if (jsonObject.getString("pushwoosh") == "promo") {
                    isPromoAction = true
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }


        // start your activity instead:
        val launchIntent: Intent
        launchIntent = if (isPromoAction) {
            Intent(applicationContext, MainActivity::class.java)
        } else {
            Intent(applicationContext, MainActivity::class.java)
        }
        launchIntent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        applicationContext!!.startActivity(launchIntent)
    }
}