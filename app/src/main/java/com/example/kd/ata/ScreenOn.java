package com.example.kd.ata;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by KD on 2/24/2018.
 */

public class ScreenOn extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean screenOff = false;
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            screenOff = true;

            Toast.makeText(context, "this is screen on", Toast.LENGTH_SHORT).show();
            Log.i("via Receiver","Normal ScreenOFF" );


            //Intent intentone = new Intent(context.getApplicationContext(), ScreenOnPassword.class);
            //intentone.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            //context.startActivity(intentone);


        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            screenOff = false;
        } else if(intent.getAction().equals(Intent.ACTION_ANSWER)) {

        }

    }
}
