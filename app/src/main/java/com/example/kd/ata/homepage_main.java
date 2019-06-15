package com.example.kd.ata;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class homepage_main extends AppCompatActivity {

    private static SQLiteDatabase writableDatabase;
    String per[]={Manifest.permission.READ_PHONE_STATE,Manifest.permission.SEND_SMS,Manifest.permission.RECEIVE_BOOT_COMPLETED,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.PROCESS_OUTGOING_CALLS,Manifest.permission.READ_CONTACTS,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_SMS,Manifest.permission.WRITE_CONTACTS,Manifest.permission.READ_LOGS,Manifest.permission.READ_CALL_LOG,Manifest.permission.WRITE_CALL_LOG};
    public static final int RequestPermissionCode = 7;
    int countper;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_main);
        requestPermission();
        ImageButton imagebtn1 = (ImageButton) findViewById(R.id.imageButton3);
        imagebtn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(getApplicationContext(), loginpage.class);
                startActivity(intent);
            }
        });
        ImageButton imagebtn2 = (ImageButton) findViewById(R.id.imageButton2);
        imagebtn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(getApplicationContext(), Duplicate_contact_Remover_homepage.class);
                startActivity(intent);
            }
        });


        startService(new Intent(this, ScreenOnService.class));

    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,per, RequestPermissionCode);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean ReadphonestatePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean SendsmsPermissions = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean RecievebootcomPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean WriteStoragePermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean Accessfinelocationpermission = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    boolean ProcessOutgoingPermission = grantResults[5] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadcContactsPermission = grantResults[6] == PackageManager.PERMISSION_GRANTED;
                    boolean AccessNetworkPermission = grantResults[7] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadsmsPermission = grantResults[8] == PackageManager.PERMISSION_GRANTED;
                    //boolean WritelogsPermission = grantResults[9] == PackageManager.PERMISSION_GRANTED;
                    //boolean Readlogspermission = grantResults[10] == PackageManager.PERMISSION_GRANTED;
                    //boolean ReadcalllogPermission = grantResults[11] == PackageManager.PERMISSION_GRANTED;
                    //boolean WritecalllogPermission = grantResults[12] == PackageManager.PERMISSION_GRANTED;
                    if (ReadphonestatePermission && SendsmsPermissions && RecievebootcomPermission && WriteStoragePermission && Accessfinelocationpermission && ProcessOutgoingPermission && ReadcContactsPermission && AccessNetworkPermission && ReadsmsPermission) {

                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(this,"Permission Denied",Toast.LENGTH_LONG).show();
                        showSettingsDialog();
                    }
                }

                break;
        }
    }

    private void showSettingsDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private static int id;

        private static String email;
        private static String password;
        private static String mobileno;
        private static String altmobileno;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setaltMobileno(String altmobileno) {
        this.altmobileno = altmobileno;
    }

    public String getaltMobileno() {
        return altmobileno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


/*
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

    if (requestCode == READ_PHONE_STATE) {

        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            countpermission++;

        }
    }

    if (requestCode == RECEIVE_BOOT_COMPLETED) {

        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            countpermission++;
        }
    }

    if (requestCode == WRITE_EXTERNAL_STORAGE) {

        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            countpermission++;
        }
    }

    if (requestCode == ACCESS_FINE_LOCATION) {

        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            countpermission++;
        }
    }

    if (requestCode == READ_CONTACTS) {

        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            countpermission++;
        }
    }

    if (requestCode == SEND_SMS) {

        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            countpermission++;
        }
    }

    if (requestCode == PROCESS_OUTGOING_CALLS) {

        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            countpermission++;

        }
    }
    Toast.makeText(this, countpermission + "just", Toast.LENGTH_SHORT).show();
    if (countpermission >= 7) {

        //startService(new Intent(this, ScreenOnService.class));
    }
    else{

    }


*/
/*switch (requestCode) {

            case READ_PHONE_STATE :
                {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startService(new Intent(this, ServiceATA.class));
                    startService(new Intent(this, ScreenOnService.class));

                } else {

                    Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }*/





    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());

                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

}
