package com.example.siddhi.contactsapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.siddhi.contactsapp.Activities.MainActivity;
import com.example.siddhi.contactsapp.helper.Excpetion2JSON;
import com.example.siddhi.contactsapp.helper.ServerRequest;
import com.example.siddhi.contactsapp.helper.ServiceUrl;
import com.example.siddhi.contactsapp.helper.UnexpectedServerException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Yogendra singh on 03-09-2016.
 */
public class GetUserAsyncTask extends AsyncTask<String, Void, JSONObject> {
    String api;
    String muserId;
    JSONObject jsonParams;
    public JSONObject userJs;
    private ProgressDialog progressDialog;
    private static String KEY_SUCCESS1 = "Success";
    private Context mContext;
    GetUserCallBack getUserCallBack;


    public GetUserAsyncTask(GetUserCallBack getUserCallBack, Context context, String userId) {
        this.mContext = context;
        this.getUserCallBack = getUserCallBack;
        this.muserId = userId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog=new ProgressDialog(mContext);
        progressDialog.setMessage("Get information Please wait...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            api = ServiceUrl.getBaseUrl() + ServiceUrl.getUser();
            jsonParams = new JSONObject();

            String userId = this.muserId; // params[0] is userid

            jsonParams.put("user_id", userId);

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

            String message = null;
            try {

                if (response.getString("message").equalsIgnoreCase(KEY_SUCCESS1)) {

                     userJs = response.getJSONObject("user");
                    getUserCallBack.doPostExecute(userJs);

                    Toast.makeText(mContext, "Success", Toast.LENGTH_LONG).show();

                    progressDialog.dismiss();


                } else {
                    Toast.makeText(mContext, "Success", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    Intent intent = new Intent(mContext, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }

    public interface GetUserCallBack{
        void doPostExecute(JSONObject response) throws JSONException;
    }

    // onPostExecute displays the results of the AsyncTask.
}

