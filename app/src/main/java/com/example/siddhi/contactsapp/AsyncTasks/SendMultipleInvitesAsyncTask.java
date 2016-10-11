package com.example.siddhi.contactsapp.AsyncTasks;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.example.siddhi.contactsapp.Invitation;
import com.example.siddhi.contactsapp.R;
import com.example.siddhi.contactsapp.helper.Excpetion2JSON;
import com.example.siddhi.contactsapp.helper.Map2JSON;
import com.example.siddhi.contactsapp.helper.MessageService;
import com.example.siddhi.contactsapp.helper.ServerRequest;
import com.example.siddhi.contactsapp.helper.ServiceUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Siddhi on 10/3/2016.
 */
public class SendMultipleInvitesAsyncTask extends AsyncTask<String, Void, JSONObject> {


    private Context context;
    JSONArray array = new JSONArray();
    SendMultipleInvitesCallBack sendMultipleInvitesCallBack;

    public SendMultipleInvitesAsyncTask(Context context,SendMultipleInvitesCallBack callBack)
    {

        this.context = context;
        this.sendMultipleInvitesCallBack = callBack;

    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            String api = ServiceUrl.getBaseUrl()  + "sendMultipleInvites.php";

            JSONObject obj = new JSONObject(params[0]);

            ServerRequest request = new ServerRequest(api,obj);
            return request.sendRequest();

        } catch(JSONException je) {
            return Excpetion2JSON.getJSON(je);
        }
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);


        JSONArray invitesArray;

        try {
            int result = jsonObject.getInt("result");
//                String message = jsonObject.getString("message");
            if (result == 1) {
               //    Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                invitesArray = jsonObject.getJSONArray("invitations");

                sendMultipleInvitesCallBack.doPostExecute(invitesArray);

            }
            else{
                //  Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                //Code when api fails goes here
            }
        }

        catch(JSONException je) {
            je.printStackTrace();
            Toast.makeText(context, je.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    public interface SendMultipleInvitesCallBack{
        void doPostExecute(JSONArray response) throws JSONException;
    }
}
