package com.weberz.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.weberz.Contact;
import com.weberz.User;
import com.weberz.database.ContactTableHelper;

import java.util.ArrayList;

/**
 * Created by Siddhi on 12/16/2016.
 */
public class GetContacts extends AsyncTask<Void,Void,ArrayList<Contact>> {

        private Context mContext;
        private ContactTableHelper dbConnector;
        private GetContactsFromDbCallback getContactsFromDbCallback;
    private ProgressDialog progressDialog;

        public GetContacts(Context context,GetContactsFromDbCallback getContactsFromDbCallback)
        {

            this.mContext = context;
            this.getContactsFromDbCallback = getContactsFromDbCallback;
            dbConnector = new ContactTableHelper(context);

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

    public interface GetContactsFromDbCallback{
        void doPostExecute(ArrayList<Contact> contacts,boolean b);
    }

        @Override
        public ArrayList<Contact> doInBackground(Void... params) {

            ArrayList<Contact> contacts = new ArrayList<>();

            contacts = dbConnector.getAllContacts();

            return contacts;
        }

        @Override
        public void onPostExecute(ArrayList<Contact> b) {
            if (b != null) {
                // set the adapter's Cursor

                getContactsFromDbCallback.doPostExecute(b,true);

                dbConnector.close();
                progressDialog.dismiss();
            }
        }
}
