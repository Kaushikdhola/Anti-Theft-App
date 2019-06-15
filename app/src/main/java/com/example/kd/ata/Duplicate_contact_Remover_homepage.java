package com.example.kd.ata;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static com.example.kd.ata.R.id.hideapp;

public class Duplicate_contact_Remover_homepage extends AppCompatActivity {

    ProgressBar load;
    Cursor cursor;
    ArrayList<String> vCard ;
    String vfile;
    int status = 1;
    private Handler handler = new Handler();
    FileOutputStream mFileOutputStream = null;
    ImageButton btnBackupCts = null;
    ImageButton btnRestorects = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duplicate_contact__remover_homepage);
        btnRestorects = (ImageButton) findViewById(R.id.imageButton5);
        btnBackupCts = (ImageButton) findViewById(R.id.imageButton4);

        vfile = "ATAcontacts.vcf";
        final String storage_path = Environment.getExternalStorageDirectory().toString() +"/"+ vfile;

        final File f = new File(storage_path);

        btnBackupCts.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                try
                {
                    if (!f.exists())
                        f.createNewFile();
                    mFileOutputStream = new FileOutputStream(storage_path, false);

                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                }

                getVcardString();

            }
        });

        btnRestorects.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Intent intent = new Intent();

                final MimeTypeMap mime = MimeTypeMap.getSingleton();
                String tmptype = mime.getMimeTypeFromExtension("vcf");
                final File file = new File(Environment.getExternalStorageDirectory().toString() +"/contacts.vcf");

                intent.setDataAndType(Uri.fromFile(file),tmptype);
                startActivity(intent);
                Toast.makeText(Duplicate_contact_Remover_homepage.this, "your contacts is Restored", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void getVcardString()
    {
        // TODO Auto-generated method stub
        vCard = new ArrayList<String>();
        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if(cursor!=null&&cursor.getCount()>0)
        {
            cursor.moveToFirst();
            for(int i =0;i<cursor.getCount();i++)
            {

                getb(cursor);
                Log.d("TAG", "Contact "+(i+1)+"VcF String is"+vCard.get(i));

                cursor.moveToNext();

            }

        }
        else
        {
            Log.d("TAG", "No Contacts in Your Phone");
        }
        try
        {
            Toast.makeText(Duplicate_contact_Remover_homepage.this, "your contacts are backuped", Toast.LENGTH_SHORT).show();
            mFileOutputStream.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void getb(Cursor cursor)
    {
        //cursor.moveToFirst();
        String lookupKey = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);
        AssetFileDescriptor fd;
        try
        {
            fd = this.getContentResolver().openAssetFileDescriptor(uri, "r");
            FileInputStream fis = fd.createInputStream();
            byte[] buf = new byte[(int) fd.getDeclaredLength()];
            fis.read(buf);
            String vcardstring= new String(buf);
            vCard.add(vcardstring);

            mFileOutputStream.write(vcardstring.toString().getBytes());
            //  Toast.makeText(backup2.this, "your contacts is restore", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
    public void duplicateclick(View v)
    {
        load=(ProgressBar) findViewById(R.id.load);
        load.setVisibility(View.VISIBLE);
        Toast.makeText(this, "visible", Toast.LENGTH_SHORT).show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (status <= 10) {
                    status = status + 1;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            load.setProgress(status);

                        }
                    });
                    try {
                        Thread.sleep(100);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                Intent intent = new Intent(getApplicationContext(), Contact_Main1.class);
                startActivity(intent);

            }
        }).start();

    }

}
