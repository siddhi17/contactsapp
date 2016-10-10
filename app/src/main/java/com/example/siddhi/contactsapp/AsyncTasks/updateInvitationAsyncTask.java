package com.example.siddhi.contactsapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.example.siddhi.contactsapp.Activities.ProfileActivity;
import com.example.siddhi.contactsapp.R;
import com.example.siddhi.contactsapp.helper.Excpetion2JSON;
import com.example.siddhi.contactsapp.helper.Map2JSON;
import com.example.siddhi.contactsapp.helper.ServerRequest;
import com.example.siddhi.contactsapp.helper.ServiceUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Siddhi on 10/6/2016.
 */
public class updateInvitationAsyncTask  extends AsyncTask<Map<String, String>, Void, JSONObject> {

    private Context context;
    private ProgressDialog progressDialog;
    private boolean updateFailed;


    @Override
    protected JSONObject doInBackground(Map<String, String>... params) {

        try {
            String api = ServiceUrl.getBaseUrl() + "updateInvite.php";

            Map2JSON mjs = new Map2JSON();

            JSONObject jsonParams = mjs.getJSON(params[0]);

            ServerRequest request = new ServerRequest(api, jsonParams);
            return request.sendRequest();

        } catch(JSONException je) {
            return Excpetion2JSON.getJSON(je);
        }
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);

        Log.d("ServerResponse", jsonObject.toString());
        try {
            int result = jsonObject.getInt("status");
            String message = jsonObject.getString("message");
            if ( result == 1 ) {

               // Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                //Code for having successful result for register api goes here

                //  checkListActivity.refreshAdapter((CheckListActivity)context);

            } else {

                 // Toast.makeText(context, message, Toast.LENGTH_LONG).show();

            }

        } catch(JSONException je) {
            je.printStackTrace();
            Toast.makeText(context, je.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
