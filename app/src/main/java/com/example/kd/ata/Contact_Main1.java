package com.example.kd.ata;

import android.app.Dialog;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Contact_Main1 extends AppCompatActivity {

    List<String> l,l1;
    ListView lv;
    TextView viewClicked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact__main1);

        lv=(ListView)findViewById(R.id.lv);
        l=new ArrayList<>();
        lv.setChoiceMode(lv.CHOICE_MODE_MULTIPLE);
        l1=new ArrayList<>();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        int i=-1;
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        if (i == -1 || !l1.get(i).replaceAll("\\s+", "").equals("PhoneNumber:" + phoneNo.replaceAll("\\s+", ""))) {
                            l.add("Name : " + name);
                            if (i != -1) {
                                if (l1.get(i).startsWith("+") && l1.get(i).substring(17).replaceAll("\\s+", "").equals(phoneNo.replaceAll("\\s+", ""))) {
                                    l1.add("Phone Number :" + phoneNo.replaceAll("\\s+", ""));
                                } else if (phoneNo.startsWith("+") && l1.get(i).replaceAll("\\s+", "").equals("PhoneNumber:" + phoneNo.substring(2).replaceAll("\\s+", ""))) {
                                    Toast.makeText(this, "phone no", Toast.LENGTH_SHORT).show();
                                } else {
                                    l1.add("Phone Number :" + phoneNo.replaceAll("\\s+", ""));
                                    Log.e("data", phoneNo.startsWith("+") + " " + phoneNo.replaceAll("\\s+", "") + " " + l1.get(i).substring(15).replaceAll("\\s+", ""));
                                }
                            } else {
                                l1.add("Phone Number :" + phoneNo.replaceAll("\\s+", ""));
                            }
                            i++;
                        }
                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }
        CustomAdapter cs=new CustomAdapter(this,l,l1,l.toArray().length);
        lv.setAdapter(cs);
        registerForContextMenu(lv);
    }

    public void addContact(View view) {
        Intent i=new Intent(this,Contact_Main2.class);
        startActivity(i);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater mi=new MenuInflater(this);
        mi.inflate(R.menu.options,menu);
    }
    String name;
    String phone;
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        LinearLayout ll = (LinearLayout) info.targetView;
        TextView tv1=(TextView) ll.getChildAt(0);
        TextView tv2=(TextView) ll.getChildAt(1);
        name=tv1.getText().toString().substring(7);
        phone=tv2.getText().toString().substring(14);
        Toast.makeText(this, name+"\t"+phone, Toast.LENGTH_SHORT).show();
        if(R.id.i1 == item.getItemId()){
            Intent i =new Intent(this,Contact_Main2.class);
            startActivity(i);
            Toast.makeText(this,"Edit",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Delete",Toast.LENGTH_SHORT).show();
            DialogInterface.OnClickListener listener;

            listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    if(i == Dialog.BUTTON_POSITIVE){

                        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
                        Cursor cur = Contact_Main1.this.getContentResolver().query(contactUri, null, null, null, null);
                        try {
                            if (cur.moveToFirst()) {
                                do {
                                    if (cur.getString(cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)).equalsIgnoreCase(name)) {
                                        String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                                        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                                        lv.invalidateViews();
                                        Contact_Main1.this.getContentResolver().delete(uri, null, null);
                                        Toast.makeText(Contact_Main1.this, "Contact Deleted", Toast.LENGTH_SHORT).show();

                                    }
                                } while (cur.moveToNext());
                            }
                        } catch (Exception e) {
                            System.out.println(e.getStackTrace());
                        } finally {
                            cur.close();
                        }
                        addContact(name,phone);
                    }
                }
            };

            android.app.AlertDialog.Builder ab = new android.app.AlertDialog.Builder(this);
            ab.setTitle("Delete Contact");
            ab.setMessage("Are you sure you want to delete ?");
            ab.setPositiveButton("Yes",listener);

            ab.setNegativeButton("No",listener);

            android.app.AlertDialog ad= ab.create();
            ad.show();
        }
        return super.onContextItemSelected(item);
    }

    public void addContact(String username, String phone) {
        ArrayList<ContentProviderOperation> ops = new ArrayList < ContentProviderOperation > ();

        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        //------------------------------------------------------ Names
        String DisplayName= username;
        if (DisplayName != null) {
            ops.add(ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            DisplayName).build());
        }

        //------------------------------------------------------ Mobile Number
        String MobileNumber=phone;
        if (MobileNumber != null) {
            ops.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }
        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            Toast.makeText(this, "Contact Added", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}
