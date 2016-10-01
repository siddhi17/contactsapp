package com.example.siddhi.contactsapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.Toast;

import com.example.siddhi.contactsapp.Activities.LoginActivity;
import com.example.siddhi.contactsapp.Activities.RegisterActivity;
import com.example.siddhi.contactsapp.helper.Excpetion2JSON;
import com.example.siddhi.contactsapp.helper.ServerRequest;
import com.example.siddhi.contactsapp.helper.ServiceUrl;
import com.example.siddhi.contactsapp.helper.UnexpectedServerException;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by Yogendra singh on 03-09-2016.
 */
public class RegisterUserAsyncTask extends AsyncTask<String, Void, JSONObject> {
    String api;
    JSONObject jsonParams;
    String muserName;
    String mfullName;
    String mpassword;
    String mmobileNo;
    String memailId;
    String mdeviceId;
    File mprofileImage;
    private ProgressDialog progressDialog = null;

    private static String KEY_SUCCESS = "Success";
    private Context mContext;

    public RegisterUserAsyncTask(Context context, String fullName, String userName, String password, String mobileNo, String emailId, String deviceId, File profileImage) {
        this.mContext = context;
        this.muserName = userName;
        this.mpassword = password;
        this.mfullName = fullName;
        this.mmobileNo = mobileNo;
        this.memailId = emailId;
        this.mdeviceId = deviceId;
        this.mprofileImage = profileImage;

    }
    @Override
    protected void onPreExecute(){
        super.onPreExecute();

        if(progressDialog == null) {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("Creating Account...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        else {
       //     progressDialog.dismiss();
        }
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {

                   //Url
            api = ServiceUrl.getBaseUrl() + ServiceUrl.getregister();
            //Build JsonObject
            jsonParams = new JSONObject();
            String userName = this.muserName;  // params[0] is username
            String fullName = this.mfullName;  // params[1] is fullname
            String password = this.mpassword;  // params[2] is password
            String mobileNo = this.mmobileNo;  // params[3] is mobile
            String emailId = this.memailId;    // params[4] is emailid
            String deviceId = this.mdeviceId;  // params[5] is deviceid

            jsonParams.put("full_name", fullName);
            jsonParams.put("user_name", userName);
            jsonParams.put("password", password);
            jsonParams.put("mobile_no", mobileNo);
            jsonParams.put("email_id", emailId);
            jsonParams.put("device_id", deviceId);

            try {
                if(convertFileToString(this.mprofileImage)!=null) {
                    jsonParams.put("profile_image", convertFileToString(this.mprofileImage));

                    System.out.println("convertFileToString(profile_image)" + convertFileToString(this.mprofileImage));
                }
                else
                { }

            } catch (Exception e) {

                System.out.println("convertFileToString(profile_image)");
                jsonParams.put("profile_image", "");
            }

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
                if (response.getString("message").equalsIgnoreCase(KEY_SUCCESS)) {
                    Toast.makeText(mContext, "success", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(mContext,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    progressDialog.dismiss();
                    progressDialog = null;
                    mContext.startActivity(intent);

                } else {
                    Toast.makeText(mContext, "Could not Register ", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(mContext, RegisterActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    progressDialog.dismiss();
                    progressDialog = null;
                    mContext.startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();

            }

        }
    }

    // onPostExecute displays the results of the AsyncTask.


    private String convertFileToString(File profileImage) throws IOException {

        Bitmap bm = BitmapFactory.decodeFile(this.mprofileImage.getPath());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte[] b = baos.toByteArray();

        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

        return new String(encodedImage);
    }
    //convertFileToString return image in  String formatted
}
