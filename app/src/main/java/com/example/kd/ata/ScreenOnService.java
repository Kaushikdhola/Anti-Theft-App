package com.example.kd.ata;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by KD on 2/24/2018.
 */


public class ScreenOnService extends Service implements LocationListener {

    BroadcastReceiver mReceiver;
    boolean gps_enabled = false;
    public static int countOn = 0;
    String link;
    public String SimID1, SimID2;
    String SimID1Compare, SimID2Compare;
    public SQLiteDatabase db;
    public SQLiteOpenHelper openHelper;
    Cursor cr1;
    int Slot;
    String Alt_no;
    int createaccountcount = 0;
    LocationManager locationManager;
    String provider;
    double longitude, Latitude;

    int countcalllogoff, countcalllogon;
    int countmsgon, countmsgoff;

    @Override
    public void onCreate() {
        super.onCreate();
        //Toast.makeText(this, " Service Started", Toast.LENGTH_LONG).show();
        Log.i("UpdateService", "Started");
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_ANSWER);
        mReceiver = new ScreenOn();
        registerReceiver(mReceiver, filter);
    }

    @Override
    public void onDestroy() {

        unregisterReceiver(mReceiver);
        Log.i("onDestroy Reciever", "Called");

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        //boolean screenOn = intent.getBooleanExtra("screen_state", false);
        //  if (!screenOn) {
        Log.i("screenON", "Called");
        Log.i("viaService", "CountOn =" + countOn);
        //  Toast.makeText(this, "Run", Toast.LENGTH_SHORT).show();
        openHelper = new DBHandler(this);
        db = openHelper.getReadableDatabase();
        db = this.openOrCreateDatabase("ATA_database", MODE_PRIVATE, null);
        String q = "create table if not exists Register" +
                "(Email varchar(15) NOT NULL," +
                "Mobile_no varchar(30) NOT NULL," +
                "imsi varchar(30) NOT NULL," +
                "imei varchar(30) NOT NULL," +
                "FSimID text NOT NULL," +
                "SSimID varchar(30) ," +
                "Alt_no varchar(20) NOT NULL," +
                "password varchar(15) NOT NULL," +
                "PRIMARY KEY(Email))";
        db.execSQL(q);


        TelephonyManager tel;
        tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        // Toast.makeText(this, SIMid3phone, Toast.LENGTH_SHORT).show();


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {


            SubscriptionManager subscriptionManager = SubscriptionManager.from(getApplicationContext());
            List<SubscriptionInfo> subsInfoList = subscriptionManager.getActiveSubscriptionInfoList();

            for (SubscriptionInfo subscriptionInfo : subsInfoList) {
                Slot = subscriptionInfo.getSimSlotIndex();

                if (Slot == 0) {
                    SimID1 = subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(0).getIccId().toString();
                    // Toast.makeText(this, +Slot + " =" + SimID1 + " ", Toast.LENGTH_LONG).show();
                }
                if (Slot == 1) {
                    SimID2 = subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(1).getIccId().toString();
                    //Toast.makeText(this, +Slot + " =" + SimID2 + " ", Toast.LENGTH_LONG).show();
                }

            }
        }

        final String SqlAlt = "select Alt_no from Register";
        Cursor cr3Alt = db.rawQuery(SqlAlt, null);
        if (cr3Alt != null && cr3Alt.moveToFirst()) {
            int index2 = cr3Alt.getColumnIndex("Alt_no");
            Alt_no = cr3Alt.getString(index2);
            Toast.makeText(this, Alt_no, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Alter =nate number not available", Toast.LENGTH_SHORT).show();
        }
        String sql = "select FSimID from Register";
        cr1 = db.rawQuery(sql, null);
        String sql2 = "select SSimID from Register";
        Cursor cr2 = db.rawQuery(sql2, null);
        if (cr1 != null && cr1.moveToFirst())

        {
            createaccountcount = 0;
            int index = cr1.getColumnIndex("FSimID");
            SimID1Compare = cr1.getString(index);
            if (SimID1Compare != null) {

                createaccountcount++;
                //        Toast.makeText(this, createaccountcount+"create1", Toast.LENGTH_SHORT).show();
            }
            //Toast.makeText(this, SimID1Compare + "this is db sim1 " + "\n", Toast.LENGTH_SHORT).show();

        }
        if (cr2 != null && cr2.moveToFirst())

        {
            int index1 = cr2.getColumnIndex("SSimID");
            SimID2Compare = cr2.getString(index1);
            if (SimID2Compare != null)

            {
                createaccountcount++;
                //      Toast.makeText(this, createaccountcount+"create2", Toast.LENGTH_SHORT).show();
            }
            System.out.println(SimID2Compare);
            //Toast.makeText(this, SimID2Compare + "this is db sim2", Toast.LENGTH_SHORT).show();

        }


        if (createaccountcount == 1 || createaccountcount == 2) {

            if (SimID1 != null) {
                if (SimID1.equals(SimID1Compare) || SimID1.equals(SimID2Compare)) {

                    Toast.makeText(this, "Sim not changed", Toast.LENGTH_LONG).show();


                } else {

                    SharedPreferences settings = getSharedPreferences("authorized", 0);
                    SharedPreferences settings2 = getSharedPreferences("unauthorized", 0);
                    if (settings.getString("authorized", "").toString().equals("authorized")) {

                        Toast.makeText(getApplicationContext(),"your authorized", Toast.LENGTH_SHORT).show();

                    }
                    else if(settings2.getString("unauthorized", "").toString().equals("unauthorized")){
                        Toast.makeText(getApplicationContext(), "you are unauthorized(service)", Toast.LENGTH_SHORT).show();
                        checkcalllog();
                        checkmsg();
                        if (gps_enabled) {
                            location();
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Enable Gps!!!", Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(getApplicationContext(), "New Sim Serial number is :-" + SimID1, Toast.LENGTH_LONG).show();
                    }else{

                        Toast.makeText(getApplicationContext(), "Not entered", Toast.LENGTH_SHORT).show();
                        checkcalllog();
                        checkmsg();
                        if (gps_enabled) {
                            location();
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Enable Gps!", Toast.LENGTH_SHORT).show();
                        }

                        Toast.makeText(getApplicationContext(), SimID1, Toast.LENGTH_LONG).show();

                    }

                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "Create Account", Toast.LENGTH_LONG).show();


        }

        if (createaccountcount == 1 || createaccountcount == 2) {
            if (SimID2 != null) {
                if (SimID2.equals(SimID1Compare) || SimID2.equals(SimID2Compare)) {
                    Toast.makeText(this, "Sim not changed", Toast.LENGTH_LONG).show();
                } else {

                    SharedPreferences settings = getSharedPreferences("authorized", 0);
                    SharedPreferences settings2 = getSharedPreferences("unauthorized", 0);
                    if (settings.getString("authorized", "").toString().equals("authorized")) {
                        Toast.makeText(this, "you are authorized 2", Toast.LENGTH_SHORT).show();

                    } else if(settings2.getString("unauthorized", "").toString().equals("unauthorized")){
                        checkcalllog();
                        checkmsg();
                        Toast.makeText(this, "you are unauthorized", Toast.LENGTH_SHORT).show();
                        if (gps_enabled) {
                            location();
                        } else {
                            Toast.makeText(this, "Please Enable Gps!", Toast.LENGTH_SHORT).show();
                        }

                        Toast.makeText(this, "New Sim Serial number is :-" + SimID2 + "", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(this, "Not entered 2", Toast.LENGTH_SHORT).show();
                        checkcalllog();
                        checkmsg();
                        if (gps_enabled) {
                            location();
                        } else {
                            Toast.makeText(this, "Please Enable Gps!", Toast.LENGTH_SHORT).show();
                        }

                        Toast.makeText(this, "New Sim Serial number is :-" + SimID1, Toast.LENGTH_LONG).show();

                    }
                }

                Toast.makeText(getApplicationContext(), "Awake", Toast.LENGTH_LONG)
                        .show();
            } else {
                Toast.makeText(getApplicationContext(), "Second sim not available", Toast.LENGTH_LONG);
            }

        } else {
            Toast.makeText(getApplicationContext(), "Create Account", Toast.LENGTH_LONG)
                    .show();
        }

        return START_STICKY;
    }


    @Nullable

    @Override
    public IBinder onBind(Intent intent) {


        return null;
    }


    public void checkcalllog() {
        SharedPreferences settings1 = getSharedPreferences("CallLogcheck", MODE_PRIVATE);
        countcalllogoff = settings1.getInt("calllogsoff", 0);
        countcalllogon = settings1.getInt("calllogsOn", 0);
        //Toast.makeText(this, ""+countcalllogon, Toast.LENGTH_SHORT).show();
        if (countcalllogon == 1)

        {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                this.getContentResolver().delete(CallLog.Calls.CONTENT_URI, null, null);
                 Toast.makeText(this, "call logs Checked(Service)", Toast.LENGTH_SHORT).show();
            }
            }
            else if(countcalllogoff==0)
            {
                Toast.makeText(this, "call logs Not Checked(Service)", Toast.LENGTH_SHORT).show();
            }

        }
         public void checkmsg()
        {
            SharedPreferences settings1 = getSharedPreferences("Messagecheck", MODE_PRIVATE);
            countmsgon=settings1.getInt("SelectmessageOn",0);
            countmsgoff=settings1.getInt("SelectmessageOff",0);

            if(countmsgon==1)

            {
                Uri inboxUri = Uri.parse("content://sms/");
                this.getContentResolver().delete(inboxUri, Telephony.Sms._ID + "!=?", new String[]{"0"});
              //  Toast.makeText(this, ""+countmsgon, Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "msg Checked(Service)", Toast.LENGTH_SHORT).show();
            }
            else if(countmsgoff==0)
            {
                Toast.makeText(this, ""+countmsgoff, Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "msg Not Checked(Service)", Toast.LENGTH_SHORT).show();
            }
        }


    public void location(){

    // Creating an empty criteria object
    Criteria criteria = new Criteria();
    // Getting the name of the provider that meets the criteria
    provider = locationManager.getBestProvider(criteria, false);

    if (provider != null && !provider.equals("")) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            // Get the location from the given provider
            Location location = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider, 20000, 1, this);
            if (location != null) {
                onLocationChanged(location);
            }else {
                Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getBaseContext(), "No Provider Found", Toast.LENGTH_SHORT).show();
        }
    }
}

    @Override


    public void onLocationChanged(Location location) {
        longitude=location.getLongitude();
        Latitude= location.getLatitude();
        Toast.makeText(this, "Longtitude: -"+longitude, Toast.LENGTH_LONG).show();
        Toast.makeText(this, "latitude: - " +Latitude, Toast.LENGTH_LONG).show();
        link ="https://maps.google.com/?q="+Latitude+","+longitude;
        Toast.makeText(this, link, Toast.LENGTH_LONG).show();
       sendLongSMS();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
   public void sendLongSMS() {
       String phoneNumber = Alt_no;
       String message = "Longtitude : - " + longitude +
               "\nlatitude  :-" + Latitude +
               "\nMAP : -" + link +
               "\nAlternate number: -" + Alt_no;
       SmsManager smsManager = SmsManager.getDefault();
       ArrayList<String> parts = smsManager.divideMessage(message);
       smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null);

   }
}

