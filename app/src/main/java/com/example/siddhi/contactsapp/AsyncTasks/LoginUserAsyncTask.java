package com.example.siddhi.contactsapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.example.siddhi.contactsapp.Activities.LoginActivity;
import com.example.siddhi.contactsapp.Activities.MainActivity;
import com.squareup.picasso.Target;
import com.example.siddhi.contactsapp.User;
import com.example.siddhi.contactsapp.helper.Excpetion2JSON;
import com.example.siddhi.contactsapp.helper.ServerRequest;
import com.example.siddhi.contactsapp.helper.ServiceUrl;
import com.example.siddhi.contactsapp.helper.UnexpectedServerException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Siddhi on 9/21/2016.
 */
public class LoginUserAsyncTask extends AsyncTask<String, Void, JSONObject> {
    String api;
    JSONObject jsonParams;
    String muserName;
    String mpassword;
    int count;
    private Context mContext;
    private ProgressDialog progressDialog;
    private static String KEY_SUCCESS = "user authenticated successfully";
    private Boolean firstTimeLogin = false;
    private SharedPreferences sharedpreferences;

    public LoginUserAsyncTask(Context context, String userName, String password) {
        this.mContext = context;
        this.muserName = userName;
        this.mpassword = password;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {

            //Url
            api = ServiceUrl.getBaseUrl() + ServiceUrl.getLoginUrl();
            //build JsonObject
            jsonParams = new JSONObject();
            String userName = this.muserName; // params[0] is username
            String password = this.mpassword; // params[1] is password
            jsonParams.put("user_name", userName);
            jsonParams.put("password", password);

            ServerRequest request = new ServerRequest(api, jsonParams);
            return request.sendRequest();

        } catch (JSONException je) {
            return Excpetion2JSON.getJSON(je);
        }
    }  //end of doInBackground

    @Override
    protected void onPostExecute(JSONObject response) {
        super.onPostExecute(response);
        count = 0;
        if (response.has("message")) {
            JSONObject userJson = null;
            String message = null;
            count++;
            try {

                if (response.getString("message").equalsIgnoreCase(KEY_SUCCESS)) {
                    Toast.makeText(mContext, "user authenticated successfully", Toast.LENGTH_LONG).show();
                    userJson = response.getJSONObject("user");


                    String userId = userJson.getString("user_id");
                    String userName = userJson.getString("user_name");
                    String profileImage = userJson.getString("profile_image");
                    String mobileNo = userJson.getString("mobile_no");
                    String homeAddress = userJson.getString("home_address");
                    String workAddress = userJson.getString("work_address");
                    String work_phone = userJson.getString("work_phone");
                    String email_id = userJson.getString("email_id");
                    String pass = userJson.getString("password");
                    String full_name = userJson.getString("full_name");
                    String job_title = userJson.getString("job_title");

                    String url = ServiceUrl.getBaseUrl() + ServiceUrl.getImageUserUrl() + profileImage;
                    Log.e("url", url);

                    new ImageUserTask(mContext,url).execute();

                    /*Target target = new Target() {
                        @Override
                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    File file = new File(
                                            Environment.getExternalStorageDirectory().getPath()
                                                    + "/saved.jpg");
                                    try {
                                        file.createNewFile();
                                        FileOutputStream ostream = new FileOutputStream(file);
                                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,ostream);
                                        ostream.close();
                                    }
                                    catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {}

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {}
                    };
                    */

                    User user = new User();

                    user.setmUserId(userId);
                    user.setmUserName(userName);
                    user.setmProfileImage(profileImage);
                    user.setmMobileNo(mobileNo);
                    user.setmFullName(full_name);
                    user.setmHomeAddress(homeAddress);
                    user.setmPassword(pass);
                    user.setmEmailId(email_id);
                    user.setmWorkPhone(work_phone);
                    user.setmWorkAddress(workAddress);
                    user.setmJobTitle(job_title);

                    firstTimeLogin = true;

                    SharedPreferences.Editor editor = mContext.getSharedPreferences("UserProfile", mContext.MODE_PRIVATE).edit();
                    editor.putString("UserUsername", userName);
                    editor.putString("userId", userId);
                    editor.putString("url", url);
                    editor.putBoolean("login",firstTimeLogin);
                    editor.putString("mobileno",mobileNo);
                    editor.putString("jobTitle",job_title);
                    editor.putString("pass",pass);
                    editor.putString("workPhone",work_phone);
                    editor.putString("workAddress",workAddress);
                    editor.putString("homeAddress",homeAddress);
                    editor.putString("fullName",full_name);
                    editor.putString("profileImage",profileImage);
                    editor.putString("emailId",email_id);
                    editor.commit();

                    //       new ImageUserTask(mContext, url, profileImage).execute();

                    Toast.makeText(mContext, "user authenticated successfully", Toast.LENGTH_LONG).show();

                   /* User user1 = new User(userId);
                    Log.e("user1", "" + user1);*/
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    /*intent.putExtra("sampleObject", user1);*/
                    intent.putExtra("url", url);
                    progressDialog.dismiss();
                    mContext.startActivity(intent);


                } else {

                    Toast.makeText(mContext, "Unable to login.", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    if (count >= 3) {
                        Toast.makeText(mContext, "Sorry you have exceeded login limit.Please try again later…..!!!!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(mContext, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(intent);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(mContext,"Unable to login.", Toast.LENGTH_LONG).show();

            }
        }
    }

}
