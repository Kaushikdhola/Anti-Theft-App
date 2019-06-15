package com.example.kd.ata;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class forgetpassword_change extends AppCompatActivity {


    EditText epassword, cpassword;
    Button change;
    SQLiteDatabase db;
    SQLiteOpenHelper openHelper;
    String Semail1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword_change);
        epassword = (EditText) findViewById(R.id.epassword);
        cpassword = (EditText) findViewById(R.id.cpassword);
        change = (Button) findViewById(R.id.change);
        openHelper = new DBHandler(this);
        db = openHelper.getReadableDatabase();
        db = openOrCreateDatabase("ATA_database", MODE_PRIVATE, null);
        Semail1 = getIntent().getExtras().getString("email");
        Toast.makeText(this, Semail1, Toast.LENGTH_LONG).show();
    }

    public void change(View view) {
        epassword = (EditText) findViewById(R.id.epassword);
        cpassword = (EditText) findViewById(R.id.cpassword);
        String np = Semail1;
        String Spassword = epassword.getText().toString();
        String Cpasswrod=cpassword.getText().toString();
        if(Cpasswrod.equals(Spassword)) {
            long n;
            ContentValues values = new ContentValues();
            values.put("password", Spassword);
            n = db.update("Register", values, "Email=?", new String[]{Semail1});

            if (n != 0) {
                Toast.makeText(this, "Password changed", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), ATA_homepage.class);
                startActivity(intent);
                finish();
                //n= db.update("Register", values, np,null );
            }
            else{
                Toast.makeText(this, "Password Not changed", Toast.LENGTH_LONG).show();
            }
        }else{
            cpassword.setError("Entre Same password");
        }
    }
    public void forgetpassword(View v)
    {

            Intent intent = new Intent(getApplicationContext(), Forget_password.class);
            startActivity(intent);


    }
}
