package com.example.siddhi.contactsapp.Activities;

import android.Manifest;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.siddhi.contactsapp.Adapter.ContactAdapter;
import com.example.siddhi.contactsapp.Adapter.InviteAdapter;
import com.example.siddhi.contactsapp.AsyncTasks.SendMultipleInvitesAsyncTask;
import com.example.siddhi.contactsapp.Contact;
import com.example.siddhi.contactsapp.Invitation;
import com.example.siddhi.contactsapp.Invite;
import com.example.siddhi.contactsapp.R;
import com.example.siddhi.contactsapp.User;
import com.example.siddhi.contactsapp.database.ContactTableHelper;
import com.example.siddhi.contactsapp.helper.DividerItemDecoration;
import com.example.siddhi.contactsapp.helper.MessageService;
import com.example.siddhi.contactsapp.helper.SendSmsPermission;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class InviteContactsActivity extends AppCompatActivity implements SendMultipleInvitesAsyncTask.SendMultipleInvitesCallBack {

    private RecyclerView mRecyclerView;
    private InviteAdapter mAdapter;
    private ArrayList<Contact> mContactList;
    private ContactTableHelper mContactDb;
    private ArrayList<Invitation> invitationArrayList;
    private MessageService.SmsDeliveredReceiver smsDeliveredReceiver;
    private MessageService.SmsSentReceiver smsSentReceiver;
    private final static int SEND_SMS = 90, RECEIVE_SMS = 111;
    private static final String CONTACT_ID = ContactsContract.Contacts._ID;
    private static final String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
    private static final String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

    private static final String PHONE_NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
    private static final String PHONE_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
    private boolean selectAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_contacts);


        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        final TextView title = (TextView) findViewById(R.id.toolbar_title);

        title.setText("Invite Contacts");


        if (toolbar != null) {

            toolbar.setTitle("");
            setSupportActionBar(toolbar);

        }
        final ImageView back = (ImageView) findViewById(R.id.imageBack);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        final ImageView menu = (ImageView) findViewById(R.id.imageMenu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(InviteContactsActivity.this, menu);

                popup.getMenuInflater().inflate(R.menu.pop_up_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.selectAll:
                                mAdapter.toggleContactsSelection(true);
                                return true;
                            case R.id.inviteByUserName:

                                startActivity(new Intent(InviteContactsActivity.this,InviteByUsername.class));

                                return true;

                        }
                        return true;
                    }
                });

                popup.show();
            }
        });


        ImageView sendInvites = (ImageView) findViewById(R.id.imageSelect);

        sendInvites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAdapter.updateInvites();

                invitationArrayList = mAdapter.getArrayList();

                Log.e("inviteList", String.valueOf(invitationArrayList.size()));

                Gson gson = new Gson();
                String toServer = gson.toJson(
                        Collections.singletonMap("invitations", invitationArrayList)
                );

                new SendMultipleInvitesAsyncTask(InviteContactsActivity.this, InviteContactsActivity.this).execute(toServer);

                finish();
                Intent i = new Intent(InviteContactsActivity.this, InviteContactsActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
            }
        });

        //   mContactDb = new ContactTableHelper(InviteContactsActivity.this);

        //   mContactList = mContactDb.getAllContacts();

        mContactList = getContactList();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(InviteContactsActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new InviteAdapter(InviteContactsActivity.this, mContactList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void doPostExecute(JSONArray response) throws JSONException {

        ArrayList<String> numbers = new ArrayList<>();

        for (int i = 0; i < response.length(); i++) {

            //    Invitation invitation = new Invitation();
            JSONObject subObject1 = response.getJSONObject(i);

            String status = subObject1.getString("status");

            if (status.equals("1")) {

                JSONObject invitation = subObject1.getJSONObject("invitation");

                String number = invitation.getString("invitee_no");

                numbers.add(number);

             /*  try {
                    SmsManager sms = SmsManager.getDefault();
                    PendingIntent sentPI;
                    String SENT = "SMS_SENT";

                    sentPI = PendingIntent.getBroadcast(InviteContactsActivity.this, 0,new Intent(SENT), 0);

                    //  sms.sendTextMessage(phoneNumber, null, message, sentPI, null);
                    sms.sendTextMessage("8655864341", null, "Hi,add me to your unique contact list.",sentPI, null);

                    Toast.makeText(getApplicationContext(), "SMS Sent!" +" " + number,
                            Toast.LENGTH_LONG).show();
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again later!" + " " + number,
                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }*/


                if(SendSmsPermission.checkPermission(InviteContactsActivity.this)) {

                    Intent serviceIntent = new Intent(this, MessageService.class);
                    serviceIntent.putStringArrayListExtra("numbers", numbers);
                    startService(serviceIntent);
                    IntentFilter filter = new IntentFilter();
                    this.registerReceiver(smsDeliveredReceiver, filter);
                    filter = new IntentFilter();
                    this.registerReceiver(smsSentReceiver, filter);
                }
                else {
                    boolean r = SendSmsPermission.checkPermission(InviteContactsActivity.this);
                }

            } else {
                subObject1 = response.getJSONObject(i);

                String number = subObject1.getString("invitee_no");
                Toast.makeText(InviteContactsActivity.this, "Invitation has been sent before to number:" + number, Toast.LENGTH_SHORT).show();
            }
        }

       // requestSmsPermission();

    }

    private boolean isSendSmsAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    private boolean isReceiveSmsAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    private void requestSmsPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        } else {
            //And finally ask for the permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS);
        }


        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        } else {
            //And finally ask for the permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, RECEIVE_SMS);
        }
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == SEND_SMS) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast

            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == RECEIVE_SMS) {
            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast

            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    public ArrayList<Contact> getContactList() {
        ArrayList<Contact> contactList = new ArrayList<Contact>();

        Uri contactUri = ContactsContract.Contacts.CONTENT_URI;
        String[] PROJECTION = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER,
                ContactsContract.Contacts.PHOTO_URI,

        };
        String SELECTION = ContactsContract.Contacts.HAS_PHONE_NUMBER + "='1'";
        Cursor contacts = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, PROJECTION, SELECTION, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");


        if (contacts.getCount() > 0) {
            while (contacts.moveToNext()) {
                Contact aContact = new Contact();
                int idFieldColumnIndex = 0;
                int nameFieldColumnIndex = 0;
                int numberFieldColumnIndex = 0;
                int photoFieldColumnIndex = 0;

                String contactId = contacts.getString(contacts.getColumnIndex(ContactsContract.Contacts._ID));

                aContact.setContactId(contactId);
                HashSet<String> contactIds = new HashSet<String>();

                nameFieldColumnIndex = contacts.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
                if (nameFieldColumnIndex > -1) {
                    aContact.setmFullName(contacts.getString(nameFieldColumnIndex));
                }


                photoFieldColumnIndex = contacts.getColumnIndex(ContactsContract.PhoneLookup.PHOTO_URI);
                if (photoFieldColumnIndex > -1) {
                    aContact.setmProfileImage(contacts.getString(photoFieldColumnIndex));
                }

                PROJECTION = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
                final Cursor phone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, ContactsContract.Data.CONTACT_ID + "=?", new String[]{String.valueOf(contactId)}, null);
                if (phone.moveToFirst()) {
                    while (!phone.isAfterLast()) {
                        numberFieldColumnIndex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        if (numberFieldColumnIndex > -1) {
                            aContact.setmMobileNo(phone.getString(numberFieldColumnIndex));
                            phone.moveToNext();

                            if(aContact.getmMobileNo() != null && !contactIds.contains(contactId)) {

                                contactList.add(aContact);
                                contactIds.add(contactId);
                            }
                        }
                    }
                }
                phone.close();
            }

            contacts.close();
        }

        return contactList;
    }

          /*  ContentResolver cr = InviteContactsActivity.this.getContentResolver();

            Cursor pCur = cr.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{PHONE_NUMBER, PHONE_CONTACT_ID},
                    null,
                    null,
                    null
            );
            if(pCur != null){
                if(pCur.getCount() > 0) {
                    HashMap<Integer, ArrayList<String>> phones = new HashMap<>();
                    while (pCur.moveToNext()) {
                        Integer contactId = pCur.getInt(pCur.getColumnIndex(PHONE_CONTACT_ID));
                        ArrayList<String> curPhones = new ArrayList<>();
                        if (phones.containsKey(contactId)) {
                            curPhones = phones.get(contactId);
                        }
                        curPhones.add(pCur.getString(pCur.getColumnIndex(PHONE_CONTACT_ID)));
                        phones.put(contactId, curPhones);
                    }
                    Cursor cur = cr.query(
                            ContactsContract.Contacts.CONTENT_URI,
                            new String[]{CONTACT_ID, DISPLAY_NAME, HAS_PHONE_NUMBER},
                            HAS_PHONE_NUMBER + " > 0",
                            null,
                            DISPLAY_NAME + " ASC");
                    if (cur != null) {
                        if (cur.getCount() > 0) {
                            ArrayList<Contact> contacts = new ArrayList<>();
                            while (cur.moveToNext()) {
                                int id = cur.getInt(cur.getColumnIndex(CONTACT_ID));
                                if(phones.containsKey(id)) {
                                    Contact con = new Contact();
                                    con.setContactId(String.valueOf(id));
                                    con.setmFullName(cur.getString(cur.getColumnIndex(DISPLAY_NAME)));
                                    con.setmMobileNo();
                                    contacts.add(con);
                                }
                            }
                            return contacts;
                        }
                        cur.close();
                    }
                }
                pCur.close();
            }
            return null;*/

}
