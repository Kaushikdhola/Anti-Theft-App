package com.example.kd.ata;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by KD on 3/28/2018.
 */

public class forgetpassword_change_manu extends Activity {
    EditText epassword,cpassword,email;
    Button change;
    SQLiteDatabase db;
    SQLiteOpenHelper openHelper;
    String Semail1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword_change_manu);
        epassword = (EditText) findViewById(R.id.epassword);
        cpassword= (EditText) findViewById(R.id.cpassword);
        change = (Button) findViewById(R.id.change);
        openHelper = new DBHandler(this);
        db = openHelper.getReadableDatabase();
        db = openOrCreateDatabase("ATA_database", MODE_PRIVATE, null);

        Toast.makeText(this, Semail1, Toast.LENGTH_LONG).show();
    }

    public void change(View view) {
        epassword = (EditText) findViewById(R.id.epassword);
        email= (EditText) findViewById(R.id.Email);
        String Eemail =  email.getText().toString();
        String Spassword = epassword.getText().toString();
        String Cpasswrod=cpassword.getText().toString();
        if(Cpasswrod.equals(Spassword)) {
            long n;
            ContentValues values = new ContentValues();
            values.put("password", Spassword);
            n = db.update("Register", values, "Email=?", new String[]{Eemail});

            if (n != 0) {
                Toast.makeText(this, "Password changed", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), ATA_homepage.class);
                startActivity(intent);
                finish();
                //n= db.update("Register", values, np,null );
            }
        }
        else{
            cpassword.setError("Entre Same password");
        }
    }
}
