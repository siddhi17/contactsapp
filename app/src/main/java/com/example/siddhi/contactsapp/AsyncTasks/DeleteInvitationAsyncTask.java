package com.example.siddhi.contactsapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.siddhi.contactsapp.Activities.LoginActivity;
import com.example.siddhi.contactsapp.Activities.MainActivity;
import com.example.siddhi.contactsapp.User;
import com.example.siddhi.contactsapp.helper.Excpetion2JSON;
import com.example.siddhi.contactsapp.helper.ServerRequest;
import com.example.siddhi.contactsapp.helper.ServiceUrl;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Siddhi on 10/6/2016.
 */
public class DeleteInvitationAsyncTask extends AsyncTask<String, Void, JSONObject> {

    String api;
    JSONObject jsonParams;
    String mInvitation_id;
    private Context mContext;
    private ProgressDialog progressDialog;

    public DeleteInvitationAsyncTask(Context context,String invitation_id) {
        this.mContext = context;
        this.mInvitation_id = invitation_id;
        this.progressDialog = new ProgressDialog(mContext);
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // progressDialog=new ProgressDialog(mContext);
        progressDialog.setMessage("Deleting invitation Please wait...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    @Override
    protected JSONObject doInBackground(String... params) {
        try {

            //Url
            api = ServiceUrl.getBaseUrl() + "deleteInvitation.php";
            //build JsonObject
            jsonParams = new JSONObject();
            String invitation_id = mInvitation_id;

            jsonParams.put("invitation_id",invitation_id);

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

                if (response.getString("message").equalsIgnoreCase("Invitation Deleted Successfully.")) {
                    Toast.makeText(mContext, "Invitation Rejected.", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                } else {

                    Toast.makeText(mContext, "Could not reject Invitation.", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
