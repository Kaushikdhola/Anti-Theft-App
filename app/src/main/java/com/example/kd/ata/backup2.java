package com.example.kd.ata;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class backup2 extends AppCompatActivity {

    FileOutputStream mFileOutputStream = null;
    Button btnBackupCts = null;
    Button btnRestorects = null;
    Cursor cursor;
    Integer count =1;
    ArrayList<String> vCard;
    ProgressBar progressBar;
    String vfile;
    static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup2);
        btnRestorects = (Button) findViewById(R.id.button_restore);
        btnBackupCts = (Button) findViewById(R.id.button_backup);
        progressBar=(ProgressBar) findViewById(R.id.load);


        btnBackupCts.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                mContext = backup2.this;

                new MyTask().execute(10);

                getVCF();

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
                Toast.makeText(backup2.this, "your contacts is Restored", Toast.LENGTH_SHORT).show();
            }
        });

    }
    class MyTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... params) {

            for (; count <= params[0]; count++) {
                try {
                    Thread.sleep(1000);
                    publishProgress(count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "Task Completed.";
        }
        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
        }
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onProgressUpdate(Integer... values) {

           progressBar.setVisibility(View.VISIBLE);
        }
    }
    public static void getVCF() {
        final String vfile = "Contacts.vcf";
        Cursor phones = mContext.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                null, null, null);
        phones.moveToFirst();
        for (int i = 0; i < phones.getCount(); i++) {
            String lookupKey = phones.getString(phones
                    .getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
            Uri uri = Uri.withAppendedPath(
                    ContactsContract.Contacts.CONTENT_VCARD_URI,
                    lookupKey);
            AssetFileDescriptor fd;
            try {
                fd = mContext.getContentResolver().openAssetFileDescriptor(uri, "r");
                FileInputStream fis = fd.createInputStream();
                byte[] buf = new byte[(int) fd.getDeclaredLength()];
                fis.read(buf);
                String VCard = new String(buf);
                String path = Environment.getExternalStorageDirectory()
                        .toString() + File.separator + vfile;
                FileOutputStream mFileOutputStream = new FileOutputStream(path,
                        true);
                mFileOutputStream.write(VCard.toString().getBytes());
                phones.moveToNext();
                Log.d("Vcard", VCard);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }   public void getb(Cursor cursor)
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
}


