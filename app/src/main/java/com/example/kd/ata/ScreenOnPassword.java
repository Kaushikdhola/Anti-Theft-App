package com.example.kd.ata;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ScreenOnPassword extends AppCompatActivity {


    SQLiteDatabase db;
    TextView forgetpassword,checkclicklimit;
    SQLiteOpenHelper openHelper;
    EditText password, email;
    String sEmail, Spassword;
    int clickcount=1;
    int clickleft=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_on_password);
        SharedPreferences preferences =getSharedPreferences("authorized",0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        SharedPreferences preferences2 =getSharedPreferences("unauthorized",0);
        SharedPreferences.Editor editor2 = preferences2.edit();
        editor2.clear();
        editor2.commit();

        openHelper = new DBHandler(this);
        db = openHelper.getReadableDatabase();
        db = openOrCreateDatabase("ATA_database", MODE_PRIVATE, null);
        email = (EditText) findViewById(R.id.email);
        checkclicklimit=(TextView)findViewById(R.id.passlimit);
        password = (EditText) findViewById(R.id.pwd);
        forgetpassword = (TextView) findViewById(R.id.forgetpass);

        forgetpassword.setOnClickListener(new View.OnClickListener()

        {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Forget_password.class);
                startActivity(intent);
            }
        });
    }

    public void passcheck(View view) {
        sEmail = email.getText().toString();
        Spassword = password.getText().toString();
        Cursor cr1;
        Boolean l = false;
        if (email.length() < 5) {
            email.setError("Enter Valid Username");
        } else if (email == null) {
            email.setError("Entr email");
        } else if (password.length() < 2) {
            password.setError("Enter Valid Password");
        } else if (password == null) {
            password.setError("Entr password");
        } else {
            cr1 = db.query("Register", new String[]{"password"}, "Email=?", new String[]{sEmail}, null, null, null);

            String d_password1;
            if (cr1.moveToNext()) {
                d_password1 = cr1.getString(0);
                if (Spassword.equals(d_password1))
                    l = true;
            } else {
                Toast.makeText(this, "User doesn't exists.", Toast.LENGTH_LONG).show();

            }



            if (l == true) {

                //Toast.makeText(this, "You are authorized", Toast.LENGTH_LONG).show();

                SharedPreferences spau = getSharedPreferences("authorized", Context.MODE_PRIVATE);
                SharedPreferences.Editor edau = spau.edit();
                edau.putString("authorized", "authorized");
                //ed.apply();
                edau.commit();
                Toast.makeText(this, "Your authorized", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), homepage_main.class);
                startActivity(i);
            } else {

                SharedPreferences spun = getSharedPreferences("unauthorized", Context.MODE_PRIVATE);
                SharedPreferences.Editor edun = spun.edit();
                edun.putString("unauthorized", "unauthorized");
                //ed.apply();
                edun.commit();
                clickcount++;

                //Toast.makeText(this, "Incorrect Password", Toast.LENGTH_LONG).show();
                if(clickcount > 3) {

                    Intent i = new Intent(getApplicationContext(), ScreenOnService.class);
                    startService(i);
                    finish();
                }
                else{
                    clickleft=clickleft-1;
                    checkclicklimit.setText("You have "+clickleft+" tries left");
                }

            }
        }
    }

    protected void onUserLeaveHint()

    {
        super.onUserLeaveHint();
        SharedPreferences sp = getSharedPreferences("unauthorized", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();

        ed.putString("unauthorized", "unauthorized");
        //ed.apply();
        ed.commit();

        Intent i = new Intent(getApplicationContext(), ScreenOnService.class);
        startService(i);
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?\nIt may delete data")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sp = getSharedPreferences("unauthorized", Context.MODE_PRIVATE);
                        SharedPreferences.Editor ed = sp.edit();
                        ed.putString("unauthorized", "unauthorized");
                        //ed.apply();
                        ed.commit();
                 Intent i = new Intent(getApplicationContext(), ScreenOnService.class);
                        startService(i);

                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    }





