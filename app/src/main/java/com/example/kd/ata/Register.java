package com.example.kd.ata;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.w3c.dom.Text;
import java.security.DigestException;
import java.util.List;
import static com.example.kd.ata.R.id.editText1;

public class Register extends AppCompatActivity  implements View.OnClickListener {

    public static Register context = null;

    public final AppCompatActivity activity = Register.this;
    Button register;
    public static EditText Email;
    public static EditText Mobile_no;
    public static EditText Alt_no;
    public static EditText pawd;
    public static EditText cpwd;
    String SEmail, SMobile_no, SAlt_no, Spawd, Scpwd;
    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    private homepage_main user;
    TextView loginlink;
    String Simei, Simsi;
    String SimID1,SimID2;
    Button bimsi;
    homepage_main h;
    int Slot;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        openHelper = new DBHandler(this);
        db = openOrCreateDatabase("ATA_database", MODE_PRIVATE, null);
        register = (Button) findViewById(R.id.button);
        h = new homepage_main();
        Email = (EditText) findViewById(editText1);
        Mobile_no = (EditText) findViewById(R.id.editText);
        Alt_no = (EditText) findViewById(R.id.editText4);
        pawd = (EditText) findViewById(R.id.editText2);
        cpwd = (EditText) findViewById(R.id.editText3);
        bimsi=(Button) findViewById(R.id.imsi);

    }
    public void getimsi(View v)
    {
        TelephonyManager tel;
        Text t;


        tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        Simsi=tel.getSubscriberId().toString();
        Simei=tel.getDeviceId().toString();

        Toast.makeText(this, " "+Simsi+" ", Toast.LENGTH_LONG).show();
        Toast.makeText(this, ""+Simei+"", Toast.LENGTH_LONG).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
        SubscriptionManager subscriptionManager = SubscriptionManager.from(getApplicationContext());
        List<SubscriptionInfo> subsInfoList = subscriptionManager.getActiveSubscriptionInfoList();

        Log.d("Test", "Current list = " + subsInfoList);


        for (SubscriptionInfo subscriptionInfo : subsInfoList) {
            Slot = subscriptionInfo.getSimSlotIndex();

            Toast.makeText(this, " "+Slot+"", Toast.LENGTH_LONG).show();

            if(Slot == 0)
            {

                SimID1 = subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(0).getIccId().toString();
                Toast.makeText(this,+Slot+ " ="+SimID1+" ", Toast.LENGTH_LONG).show();
            }
            if(Slot==1)
            {
                SimID2 = subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(1).getIccId().toString();
                Toast.makeText(this, +Slot +" ="+SimID2+" ", Toast.LENGTH_LONG).show();
            }

        }

        }




    }

    public void signup(View view) throws DigestException {
        SEmail = Email.getText().toString();
        SMobile_no = Mobile_no.getText().toString();
        SAlt_no = Alt_no.getText().toString();
        Spawd = pawd.getText().toString();
        Scpwd = cpwd.getText().toString();

        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (SEmail.length() == 0) {
            Email.requestFocus();
            Email.setError("Email cannot be left blank");
        }
        if (!SEmail.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            Email.requestFocus();
            Email.setError("Invalid email address");
        }
        if (SMobile_no.length() == 0) {
            Mobile_no.requestFocus();
            Mobile_no.setError("  Mobile_no Number cannot be left blank");
        }
        if (SAlt_no.length() == 0) {
            Alt_no.requestFocus();
            Alt_no.setError("Alternate Number cannot be left blank");
            if (SAlt_no.length() < 10) {
                Alt_no.requestFocus();
                Alt_no.setError("Please entre legal Alternate Number");

            }
        }
        if (Mobile_no == Alt_no) {
            Alt_no.requestFocus();
            Alt_no.setError("Alternate Number and mobile_No should be differant ");
        }
        if (Spawd.length() == 0) {
            pawd.requestFocus();
            pawd.setError("Password cannot be left blank");
        }
        if (Spawd.length() == 0) {
            cpwd.requestFocus();
            cpwd.setError("Password cannot be left blank");
        }
        bimsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

        Email = (EditText) findViewById(editText1);
        Mobile_no = (EditText) findViewById(R.id.editText);
        Alt_no = (EditText) findViewById(R.id.editText4);
        pawd = (EditText) findViewById(R.id.editText2);
        cpwd = (EditText) findViewById(R.id.editText3);

        SEmail = Email.getText().toString();
        SMobile_no = Mobile_no.getText().toString();
        SAlt_no = Alt_no.getText().toString();
        Spawd = pawd.getText().toString();
        Toast.makeText(this, SEmail+"", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, SMobile_no+"", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, SAlt_no+"", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, Spawd+"", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, SimID1+"", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, SimID2+"", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, Simei+"", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, Simsi+"", Toast.LENGTH_SHORT).show();

        Cursor cr1, cr2, cr3;
        int counter=0;

        cr1 = db.query("Register", new String[]{"Email"}, "Email=?", new String[]{SEmail}, null, null, null);
        cr2 = db.query("Register", new String[]{"Mobile_no"}, "Mobile_no=?", new String[]{SMobile_no}, null, null, null);
        cr3 = db.query("Register", new String[]{"Alt_no"}, "Alt_no=?", new String[]{SAlt_no}, null, null, null);


    if (cr3.moveToNext()) {
        Alt_no.setError("Alternate number already exists");
        counter++;

    }
    if (cr2.moveToNext()) {
        Mobile_no.setError("mobileno already exists");
        counter++;
    }
    if (cr1.moveToNext()) {
        Email.setError("Email already exists");
        counter++;
    }

if(counter==0) {
            ContentValues values = new ContentValues();
            long n;
            values.put("Email", SEmail);
            values.put("Mobile_no", SMobile_no);
            values.put("imsi", Simsi);
            values.put("imei", Simei);
            values.put("FSimID",SimID1);
            values.put("SSimID",SimID2);
            values.put("Alt_no", SAlt_no);
            values.put("password", Spawd);
            n = db.insert("Register", null, values);

            if (n != 0) {
                Toast.makeText(this, "Account created Successfull", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
    @Override
    public void onClick(View view) {

    }
}

