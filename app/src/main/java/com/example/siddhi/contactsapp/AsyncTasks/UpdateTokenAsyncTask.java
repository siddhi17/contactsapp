package com.example.siddhi.contactsapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.siddhi.contactsapp.helper.Excpetion2JSON;
import com.example.siddhi.contactsapp.helper.ServerRequest;
import com.example.siddhi.contactsapp.helper.ServiceUrl;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Siddhi on 10/22/2016.
 */
public class UpdateTokenAsyncTask extends AsyncTask<String, Void, JSONObject> {

    String api;
    JSONObject jsonParams;
    String mUser_id,mToken;
    private Context mContext;

    public UpdateTokenAsyncTask(Context context,String userId,String token) {
        this.mContext = context;
        this.mUser_id = userId;
        this.mToken = token;

    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {

            //Url
            api = ServiceUrl.getBaseUrl() + "updateToken.php";
            //build JsonObject
            jsonParams = new JSONObject();

            jsonParams.put("user_id",mUser_id);
            jsonParams.put("token",mToken);

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

                if (response.getString("message").equalsIgnoreCase("Token Updated Successfully.")) {

                    Log.d("token updated",mToken);

                } else {

                    Log.d("token update failed",mToken);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
