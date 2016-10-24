package com.example.siddhi.contactsapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.siddhi.contactsapp.Activities.DetailViewActivity;
import com.example.siddhi.contactsapp.Activities.MainActivity;
import com.example.siddhi.contactsapp.database.ContactTableHelper;
import com.example.siddhi.contactsapp.helper.Excpetion2JSON;
import com.example.siddhi.contactsapp.helper.Map2JSON;
import com.example.siddhi.contactsapp.helper.ServerRequest;
import com.example.siddhi.contactsapp.helper.ServiceUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Siddhi on 10/20/2016.
 */
public class DeleteContactAsyncTask  extends AsyncTask<Map<String, String>, Void, JSONObject> {

    String api;
    JSONObject jsonParams;
    String mUserId,mLinkdContactId;
    private Context mContext;
    private ProgressDialog progressDialog;

    public DeleteContactAsyncTask(Context context,String linkedContactId) {
        this.mContext = context;
        this.mLinkdContactId = linkedContactId;
        this.progressDialog = new ProgressDialog(mContext);
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // progressDialog=new ProgressDialog(mContext);
        progressDialog.setMessage("Deleting contact please wait...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    @Override
    protected JSONObject doInBackground(Map<String, String>... params) {
        try {

            //Url
            api = ServiceUrl.getBaseUrl() + "deleteContact.php";
            //build JsonObject
            jsonParams = new JSONObject();
            Map2JSON mjs = new Map2JSON();

            JSONObject jsonParams = mjs.getJSON(params[0]);

            ServerRequest request = new ServerRequest(api, jsonParams);
            return request.sendRequest();

        } catch (JSONException je) {
            return Excpetion2JSON.getJSON(je);
        }
    }  //end of doInBackground

    @Override
    protected void onPostExecute(JSONObject response) {
        super.onPostExecute(response);

        if (response.has("message")) {
            JSONObject userJson = null;
            String message = null;
            try {

                if (response.getString("message").equalsIgnoreCase("Contact Deleted Successfully.")) {
                    Toast.makeText(mContext, "Contact Deleted.", Toast.LENGTH_LONG).show();
                    ContactTableHelper contactTableHelper = new ContactTableHelper(mContext);
                    contactTableHelper.deleteContact(mLinkdContactId);

                    contactTableHelper.close();

                    progressDialog.dismiss();

                    ((DetailViewActivity)mContext).finish();

                    mContext.startActivity(new Intent(mContext, MainActivity.class));

                } else {

                    Toast.makeText(mContext, "Could not delete contact.", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
