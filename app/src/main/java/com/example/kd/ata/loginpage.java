package com.example.kd.ata;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.security.DigestException;
public class loginpage extends AppCompatActivity {

    EditText Email;
    EditText pwd;
   public static SQLiteDatabase db;
    SQLiteOpenHelper openHelper;
    Cursor cursor;
    ATA_homepage h;
    public String SimID1;
    String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);
        SharedPreferences settings = getSharedPreferences("logged_users", 0);
        if (settings.getString("logged", "").toString().equals("logged")) {
            Intent intent = new Intent(loginpage.this, ATA_homepage.class);
            startActivity(intent);
        }
        final Button button1 = (Button) findViewById(R.id.button1);
        Email = (EditText) findViewById(R.id.editText1);
        pwd = (EditText) findViewById(R.id.editText2);
        openHelper = new DBHandler(this);
        db = openHelper.getReadableDatabase();
        db = openOrCreateDatabase("ATA_database", MODE_PRIVATE, null);

        String q = "create table if not exists Register" +
                "(Email varchar(15) NOT NULL," +
                "Mobile_no varchar(30) ," +
                "imsi varchar(30) ,"+
                "imei varchar(30) ,"+
                "FSimID text ,"+
                "SSimID varchar(30) ,"+
                "Alt_no varchar(20) NOT NULL," +
                "password varchar(15) NOT NULL," +
                "PRIMARY KEY(Email))";
        db.execSQL(q);

        TextView textView1 = (TextView) findViewById(R.id.textView2);
        textView1.setOnClickListener(new View.OnClickListener()

        {

            @Override
            public void onClick (View v){

                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });
        TextView textView2 = (TextView) findViewById(R.id.textView3);
        textView2.setOnClickListener(new View.OnClickListener()

        {

            @Override
            public void onClick (View v){

                Intent intent = new Intent(getApplicationContext(), Forget_password.class);
                startActivity(intent);
            }
        });
    }

    public void logIn(View view) throws DigestException {
        final String emailValidate = Email.getText().toString();

        Email = (EditText) findViewById(R.id.editText1);
        pwd = (EditText) findViewById(R.id.editText2);

        String SEMAIL = Email.getText().toString();
        String SPWD = pwd.getText().toString();
        if (emailValidate.length() == 0) {
            Email.requestFocus();
            Email.setError("Email cannot be left blank");
        }
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (!emailValidate.matches(emailPattern)) {
            Email.requestFocus();
            Email.setError("Invalid email address");
        }

        if (view != null) {

            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        Cursor cr1, cr2;
        Boolean l = false;
        if (Email.length() < 5) {
            Email.setError("Enter Valid Username");
        } else if (pwd.length() < 2) {
            pwd.setError("Enter Valid Password");
        } else {
            cr1 = db.query("Register", new String[]{"password"}, "Email=?", new String[]{SEMAIL}, null, null, null);

            String d_password1;
            if (cr1.moveToNext()) {
                d_password1 = cr1.getString(0);
                if (SPWD.equals(d_password1))
                    l = true;
            } else {
                Toast.makeText(this, "User doesn't exists.", Toast.LENGTH_LONG).show();
            }

            if (l == true) {
                SharedPreferences sp = getSharedPreferences("logged_users", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed = sp.edit();
                ed.putString("logged", "logged");
                //ed.apply();
                ed.commit();
                Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show();
                SharedPreferences sp1 = getSharedPreferences("authorized", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed1 = sp1.edit();
                ed1.putString("authorized", "authorized");
                //ed.apply();
                ed1.commit();
                finish();
                Intent intent = new Intent(this, ATA_homepage.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Incorrect Password", Toast.LENGTH_LONG).show();
            }
        }

    }




}
