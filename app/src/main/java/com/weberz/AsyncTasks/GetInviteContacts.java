package com.weberz.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import com.weberz.Contact;
import com.weberz.database.ContactTableHelper;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Siddhi on 12/17/2016.
 */
public class GetInviteContacts extends AsyncTask<Void,Void,ArrayList<Contact>> {

    private Context mContext;
    private GetInviteContactsCallBack getInviteContactsCallBack;
    private ProgressDialog progressDialog;

    public GetInviteContacts(Context context,GetInviteContactsCallBack getInviteContactsCallBack)
    {

        this.mContext = context;
        this.getInviteContactsCallBack = getInviteContactsCallBack;

    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();

        progressDialog=new ProgressDialog(mContext);
        progressDialog.setMessage("Please wait...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    public interface GetInviteContactsCallBack{
        void doPostExecute(ArrayList<Contact> contacts);
    }

    @Override
    public ArrayList<Contact> doInBackground(Void... params) {

        ArrayList<Contact> contactsList = new ArrayList<>();

        Uri contactUri = ContactsContract.Contacts.CONTENT_URI;
        String[] PROJECTION = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER,
                ContactsContract.Contacts.PHOTO_URI,

        };
        String SELECTION = ContactsContract.Contacts.HAS_PHONE_NUMBER + "='1'";
        Cursor contacts = mContext.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, PROJECTION, SELECTION, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");


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
                final Cursor phone = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, ContactsContract.Data.CONTACT_ID + "=?", new String[]{String.valueOf(contactId)}, null);
                if (phone.moveToFirst()) {
                    while (!phone.isAfterLast()) {
                        numberFieldColumnIndex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        if (numberFieldColumnIndex > -1) {

                            String number = phone.getString(numberFieldColumnIndex);
                            String num = number.trim().replaceAll(" ","");

                            aContact.setmMobileNo(num);
                            phone.moveToNext();

                            if(aContact.getmMobileNo() != null && !contactIds.contains(contactId)) {

                                contactsList.add(aContact);
                                contactIds.add(contactId);
                            }
                        }
                    }
                }
                phone.close();
            }

            contacts.close();
        }

        return contactsList;
    }

    @Override
    public void onPostExecute(ArrayList<Contact> b) {
        if (b != null) {
            // set the adapter's Cursor

            getInviteContactsCallBack.doPostExecute(b);

            progressDialog.dismiss();
        }
    }
}
