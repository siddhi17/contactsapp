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
public class CreateLinkedContactAsyncTask extends AsyncTask<String, Void, JSONObject> {

    String api;
    JSONObject jsonParams;
    String user_id,linkedContact_id;
    private Context mContext;
    private static String KEY_SUCCESS = "Success, Contact is linked.";


    public CreateLinkedContactAsyncTask(Context context, String user_id, String linkedContact_id) {
        this.mContext = context;
        this.user_id = user_id;
        this.linkedContact_id = linkedContact_id;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {

            //Url
            api = ServiceUrl.getBaseUrl() + "createLinkedContact.php";
            //build JsonObject
            jsonParams = new JSONObject();
            String user_id = this.user_id; // params[0] is username
            String linked_contact_id = this.linkedContact_id; // params[1] is password
            jsonParams.put("user_id", user_id);
            jsonParams.put("linked_contact_id", linked_contact_id);

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

                if (response.getString("message").equalsIgnoreCase(KEY_SUCCESS)) {
                    Toast.makeText(mContext, "Invitation Accepted.", Toast.LENGTH_LONG).show();


                } else {

                    Toast.makeText(mContext, "Could not accept Invitation.", Toast.LENGTH_LONG).show();

                }
            }
        catch(JSONException e){
            e.printStackTrace();
        }
    }
    }

}
