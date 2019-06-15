package com.example.kd.ata;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.test.mock.MockPackageManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ATA_homepage extends AppCompatActivity {



    final Context context = this;
    CheckBox c1, c2;
    TextView txt1;
    Button b1;
    private TextView txtProgress;
    private ProgressBar progressBar;
    private int pStatus;
    private Handler handler = new Handler();
    ProgressBar hideapp;
    int countcalllogs;
    int countmsg;
    int hideappStatus = 1;

    int countcalllogoff, countcalllogon;
    int countmsgon, countmsgoff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ata_homepage);


        txtProgress = (TextView) findViewById(R.id.txtProgress);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        hideapp = (ProgressBar) findViewById(R.id.hideapp);
        hideapp.setVisibility(View.INVISIBLE);

        b1 = (Button) findViewById(R.id.btnhideapp);
        c1 = (CheckBox) findViewById(R.id.chk1);
        c2 = (CheckBox) findViewById(R.id.chk2);

        c1.setVisibility(View.INVISIBLE);
        c2.setVisibility(View.INVISIBLE);
        txt1 = (TextView) findViewById(R.id.txt1);



        final Switch mySwitch = (Switch) findViewById(R.id.switch2);
        mySwitch.setTextOn("On"); // displayed text of the Switch whenever it is in checked or on state
        mySwitch.setTextOff("Off");

        boolean checked = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("checkBox1", false);
        c1.setChecked(checked);

        boolean checked2 = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("checkBox2", false);
        c2.setChecked(checked2);

int countmsgon1,countmsgoff1,countcalllogon1,countcalllogoff1;
        SharedPreferences settings1 = getSharedPreferences("Messagecheck", MODE_PRIVATE);
        countmsgon1=settings1.getInt("SelectmessageOn",0);
        countmsgoff1=settings1.getInt("SelectmessageOff",0);
        SharedPreferences settings2 = getSharedPreferences("CallLogcheck", MODE_PRIVATE);
        countcalllogoff1 = settings2.getInt("calllogsoff", 0);
        countcalllogon1= settings2.getInt("calllogsOn", 0);

        if(countcalllogon1==1 && countmsgon1==1)

        {
            txtProgress.setText("Safe");
        }
        else
        {
            txtProgress.setText("Not Safe");
        }

        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (mySwitch.isChecked())

                {
                    txt1.setVisibility(View.VISIBLE);
                    c1.setVisibility(View.VISIBLE);
                    c2.setVisibility(View.VISIBLE);

                    c1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                            if (c1.isChecked()) {
                                boolean checked = ((CheckBox) buttonView).isChecked();
                                countcalllogs = 1;
                                SharedPreferences shared = getSharedPreferences("CallLogcheck", MODE_PRIVATE);
                                shared.edit().remove("calllogsoff").commit();
                                SharedPreferences.Editor editor = shared.edit();
                                editor.putInt("calllogsOn", countcalllogs);
                                editor.commit();
                                PreferenceManager.getDefaultSharedPreferences(context).edit()
                                        .putBoolean("checkBox1", checked).commit();
                                Toast.makeText(context, "checked" + countcalllogs, Toast.LENGTH_SHORT).show();

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        while (pStatus <= 50) {
                                            handler.post(new Runnable() {
                                                @RequiresApi(api = Build.VERSION_CODES.N)
                                                @Override
                                                public void run() {
                                                    progressBar.setProgress(pStatus);
                                               }
                                            });}
                                        try {
                                            Thread.sleep(100);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        pStatus++;
                                    }

                                }).start();


                            } else {
                                boolean checked = ((CheckBox) buttonView).isChecked();
                                countcalllogs = 0;
                                SharedPreferences shared = getSharedPreferences("CallLogcheck", MODE_PRIVATE);
                                shared.edit().remove("calllogsOn").commit();
                                SharedPreferences.Editor editor = shared.edit();
                                editor.putInt("calllogsoff", countcalllogs);
                                editor.commit();

                                PreferenceManager.getDefaultSharedPreferences(context).edit()
                                        .putBoolean("checkBox1", checked).commit();

                                Toast.makeText(context, "not checked" + countcalllogs, Toast.LENGTH_SHORT).show();

                            }
                        }
                    });


                    c2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (c2.isChecked()) {
                                countmsg = 1;
                                boolean checked2 = ((CheckBox) buttonView).isChecked();
                                SharedPreferences shared = getSharedPreferences("Messagecheck", MODE_PRIVATE);
                                shared.edit().remove("SelectmessageOff").commit();
                                SharedPreferences.Editor editor2 = shared.edit();
                                editor2.putInt("SelectmessageOn", countmsg);
                                editor2.commit();
                                PreferenceManager.getDefaultSharedPreferences(context).edit()
                                        .putBoolean("checkBox2", checked2).commit();

                                Toast.makeText(context, "checked" + countmsg, Toast.LENGTH_SHORT).show();

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        while (pStatus <= 100) {
                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    progressBar.setProgress(pStatus);

                                                }
                                            });
                                            try {
                                                Thread.sleep(100);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            pStatus++;
                                        }
                                    }
                                }).start();
                            } else {

                                countmsg = 0;
                                boolean checked2 = ((CheckBox) buttonView).isChecked();
                                SharedPreferences shared = getSharedPreferences("Messagecheck", MODE_PRIVATE);
                                shared.edit().remove("SelectmessageOn").commit();
                                SharedPreferences.Editor editor2 = shared.edit();
                                editor2.putInt("SelectmessageOff", countmsg);
                                editor2.commit();

                                PreferenceManager.getDefaultSharedPreferences(context).edit()
                                        .putBoolean("checkBox2", checked2).commit();

                            }

                        }
                    });
                } else {
                    Toast.makeText(ATA_homepage.this, "Switch is Off", Toast.LENGTH_SHORT).show();
                    txt1.setVisibility(View.INVISIBLE);
                    c1.setVisibility(View.INVISIBLE);
                    c2.setVisibility(View.INVISIBLE);

                }


            }

        });


        /*b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                HideApp(v);

            }
        });*/


    }


    public void settingclick(View v) {
        Intent intent = new Intent(this, Settinng.class);
        startActivity(intent);

    }

    public void HideApp(View v) {

        hideapp.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (hideappStatus <= 50) {
                    hideappStatus = hideappStatus + 1;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            hideapp.setProgress(hideappStatus);

                        }
                    });
                    try {
                        Thread.sleep(100);


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();

        PackageManager packageManager = context.getPackageManager();
        ComponentName componentName = new ComponentName(getApplicationContext(),
                splash_screen.class);
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), homepage_main.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
