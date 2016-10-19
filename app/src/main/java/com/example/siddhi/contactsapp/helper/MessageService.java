package com.example.siddhi.contactsapp.helper;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Created by Siddhi on 10/5/2016.
 */
public class MessageService extends Service {

    ArrayList<String>  numbers = new ArrayList<>();
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        numbers = intent.getStringArrayListExtra("numbers");

        for (String number:numbers) {

              sendMsg(number);

           // sendSMS("8655864341","Hello");
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public void sendMsg(final String num){
        String SENT = "SMS_SENT";
        final SmsManager sms = SmsManager.getDefault();
        final PendingIntent sentPI = PendingIntent.getBroadcast(MessageService.this, 0,new Intent(SENT), 0);
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                sms.sendTextMessage(num, null, "Hi,add me to your unique contact list and you never need to update" +
                        " any changes anymore! Click download below to download the App.", sentPI, null);
            }
        }, 3000);

    }

    @Override
    public IBinder onBind(Intent intent)
    {

    //    numbers = intent.getStringArrayListExtra("numbers");

        return null;
    }

    private void sendSMS (String phoneNumber, String message){
        ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
        ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<PendingIntent>();
        PendingIntent sentPI = PendingIntent.getBroadcast(getApplicationContext(), 0,
                new Intent(getApplicationContext(), SmsSentReceiver.class), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(getApplicationContext(), 0,
                new Intent(getApplicationContext(), SmsDeliveredReceiver.class), 0);
        try {
            SmsManager sms = SmsManager.getDefault();
            ArrayList<String> mSMSMessage = sms.divideMessage(message);
            for (int i = 0; i < mSMSMessage.size(); i++) {
                sentPendingIntents.add(i, sentPI);
                deliveredPendingIntents.add(i, deliveredPI);
            }
            sms.sendMultipartTextMessage(phoneNumber, null, mSMSMessage,
                    sentPendingIntents, deliveredPendingIntents);

        } catch (Exception e) {

            e.printStackTrace();
            Toast.makeText(getBaseContext(), "SMS sending failed...", Toast.LENGTH_SHORT).show();
        }

    }

    public class SmsDeliveredReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent arg1) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(context, "SMS delivered", Toast.LENGTH_SHORT).show();
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(context, "SMS not delivered", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    public class SmsSentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent arg1) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(context, "SMS Sent", Toast.LENGTH_SHORT).show();

                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Toast.makeText(context, "SMS generic failure", Toast.LENGTH_SHORT)
                            .show();

                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    Toast.makeText(context, "SMS no service", Toast.LENGTH_SHORT)
                            .show();

                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    Toast.makeText(context, "SMS null PDU", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    Toast.makeText(context, "SMS radio off", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
