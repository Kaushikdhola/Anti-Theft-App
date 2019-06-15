package com.example.kd.ata;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;


public class LaunchAppViaDialReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String phoneNumber = getResultData();
        if (phoneNumber == null) {
            phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        }
        if(phoneNumber.equals("12345")){
            setResultData(null);
            Intent i=new Intent(context,splash_screen.class);
            PackageManager packageManager= context.getPackageManager();
            ComponentName componentName = new ComponentName(context,splash_screen.class);
            packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
