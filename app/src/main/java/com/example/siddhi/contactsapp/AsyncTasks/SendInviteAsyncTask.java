package com.example.siddhi.contactsapp.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.siddhi.contactsapp.helper.Excpetion2JSON;
import com.example.siddhi.contactsapp.helper.Map2JSON;
import com.example.siddhi.contactsapp.helper.ServerRequest;
import com.example.siddhi.contactsapp.helper.ServiceUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Siddhi on 10/11/2016.
 */
public class SendInviteAsyncTask  extends AsyncTask<Map<String, String>, Void, JSONObject> {

    String api;
    JSONObject jsonParams;
    private Context mContext;
    private static String KEY_SUCCESS = "Invitation sent.";


    public SendInviteAsyncTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected JSONObject doInBackground(Map<String, String>... params) {
        try {

            //Url
            api = ServiceUrl.getBaseUrl() + "sendInvite.php";
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

                if (response.getString("message").equalsIgnoreCase(KEY_SUCCESS)) {
                    Toast.makeText(mContext, "Invitation sent.", Toast.LENGTH_LONG).show();


                } else {

                    Toast.makeText(mContext, "Could not send Invitation.", Toast.LENGTH_LONG).show();

                }
            }
            catch(JSONException e){
                e.printStackTrace();
            }
        }
    }


}
