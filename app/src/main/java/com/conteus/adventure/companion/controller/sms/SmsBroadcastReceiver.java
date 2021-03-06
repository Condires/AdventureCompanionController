package com.conteus.adventure.companion.controller.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.conteus.adventure.companion.controller.MainActivity;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    public static final String SMS_BUNDLE = "pdus";

    private String TAG = this.getClass().getSimpleName();

    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            String smsBody = "";
            String smsSender = "";
            for (int i = 0; i < sms.length; ++i) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                smsBody = smsMessage.getMessageBody().toString();
                smsSender = smsMessage.getOriginatingAddress();

                smsMessageStr += "SMS From: " + smsSender + "\n";
                smsMessageStr += smsBody + "\n";
            }

            Toast.makeText(context, smsMessageStr, Toast.LENGTH_SHORT).show();

            //this will update the UI with message
            // holt sich hier die Main Activity und ruft die Methode updateList auf
            MainActivity inst = MainActivity.instance();
            if (inst != null) {
                inst.updateList(smsBody, smsSender);
            } else {
                Log.d(TAG, "kann MainActivity nicht holen, Instance ist null" );
            }

        }
    }


}