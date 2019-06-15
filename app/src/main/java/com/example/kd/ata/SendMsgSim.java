package com.example.kd.ata;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.kd.ata.Register.context;


/**
 * Created by KD on 2/9/2018.
 */


public class SendMsgSim extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {


        Toast.makeText(context, "Sim state changed", Toast.LENGTH_SHORT).show();
        Toast.makeText(context, "This is Boot completed broadcast receiver", Toast.LENGTH_SHORT).show();


        Intent intentone = new Intent(context.getApplicationContext(), ScreenOnPassword.class);
        intentone.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(intentone);


    }


}


