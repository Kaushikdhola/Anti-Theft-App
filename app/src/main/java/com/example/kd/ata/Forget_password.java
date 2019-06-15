package com.example.kd.ata;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Forget_password extends AppCompatActivity {
    SQLiteDatabase db;
    SQLiteOpenHelper openHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        openHelper = new DBHandler(this);
        db = openHelper.getReadableDatabase();
        db = openOrCreateDatabase("ATA_database", MODE_PRIVATE, null);

        Button button1 = (Button) findViewById(R.id.check);


                //Intent intent = new Intent(getApplicationContext(), loginpage.class);
                //startActivity(intent);


    }

    public void check(View view) {
        EditText email, ano;
        String Semail, Sano;
        email = (EditText) findViewById(R.id.email);
        ano = (EditText) findViewById(R.id.alt_no);
        Semail = email.getText().toString();
        Sano = ano.getText().toString();


        Cursor cr1, cr2;
        Boolean l = false;
        if (email.length() < 5) {
            email.setError("Enter Valid Username");
        } else if (ano.length() < 2) {
            ano.setError("Enter Valid Password");
        } else {
            cr1 = db.query("Register", new String[]{"Alt_no"}, "Email=?", new String[]{Semail}, null, null, null);

            String d_password1, d_password2;
            if (cr1.moveToNext()) {
                d_password1 = cr1.getString(0);
                if (Sano.equals(d_password1))
                    l = true;
            } else {
                Toast.makeText(this, "User doesn't exists.", Toast.LENGTH_LONG).show();
            }

            if (l == true) {
                SharedPreferences sp = getSharedPreferences("logged_users", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed = sp.edit();
                ed.putString(Semail, "true");
                ed.apply();

                //Toast.makeText(this, "you can change password", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, forgetpassword_change.class);
                intent.putExtra("email",Semail);
                startActivity(intent);
            } else {
                Toast.makeText(this, "ALternate number incorrect", Toast.LENGTH_LONG).show();
            }
        }
    }
}
