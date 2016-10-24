package com.example.siddhi.contactsapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.example.siddhi.contactsapp.Activities.ProfileActivity;
import com.example.siddhi.contactsapp.User;
import com.example.siddhi.contactsapp.database.UserTableHelper;
import com.example.siddhi.contactsapp.helper.Excpetion2JSON;
import com.example.siddhi.contactsapp.helper.ServerRequest;
import com.example.siddhi.contactsapp.helper.ServiceUrl;
import com.example.siddhi.contactsapp.helper.UnexpectedServerException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Yogendra singh on 03-09-2016.
 */
public class UpdateUserAsyncTask extends AsyncTask<String, Void, JSONObject> {
    String api;
    JSONObject jsonParams;
    String muserName;
    String mfullName;
    String mpassword;
    String mmobileNo;
    String memailId;
    String mdeviceId;
    String muserId;
    String mstatus;
    String mjobTitle ;
    String mworkAddress;
    String mworkPhone ;
    String mhomeAddress;
    File mprofileImage;
    private ProgressDialog progressDialog;
    private static String KEY_SUCCESS1 = "User Updated Successfully.";
    Bitmap result;
    private Context mContext;
    private User mUser;

    private static Boolean updateUser = false;
    UpdateUserCallBack updateUserCallBck;

    public UpdateUserAsyncTask(UpdateUserCallBack updateUserCallBack, Context context, String userId) {
        this.mContext = context;
        this.updateUserCallBck =updateUserCallBack;
        this.muserId = userId;
        this.progressDialog = new ProgressDialog(mContext);
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("Updating user information...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    public UpdateUserAsyncTask(UpdateUserCallBack updateUserCallBack,Context context, String userId, String fullName, String userName, String password, String mobileNo, String emailId, String deviceId, File profileImage, String workAddress, String workPhone, String homeAddress, String jobTitle) {
        this.mContext = context;
        progressDialog=new ProgressDialog(mContext);
        this.updateUserCallBck = updateUserCallBack;
        this.muserName = userName;
        this.mpassword = password;
        this.mfullName = fullName;
        this.mmobileNo = mobileNo;
        this.memailId = emailId;
        this.mdeviceId = deviceId;
        this.mprofileImage = profileImage;
        this.mjobTitle = jobTitle;
        this.mworkAddress = workAddress;
        this.mworkPhone = workPhone ;
        this.mhomeAddress = homeAddress;
        this.muserId = userId;
        this.result = result;
    }

    private String convertFileToString(File profileImage) throws IOException {

        Bitmap bm = BitmapFactory.decodeFile(this.mprofileImage.getPath());
        bm = Bitmap.createScaledBitmap(bm,512,512,true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();

        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encodedImage;}


    private boolean hasImage(@NonNull CircleImageView view) {
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable != null);

        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable)drawable).getBitmap() != null;
        }

        return hasImage;
    }
  /*  @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //progressDialog
        progressDialog=new ProgressDialog(mContext);
        progressDialog.setMessage("Updating information Please wait...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }*/
    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            //url
            api = ServiceUrl.getBaseUrl() + ServiceUrl.getUpdateUserUrl();

            // build jsonObject
            jsonParams = new JSONObject();

            String userId = this.muserId;  // params[0] is userid
            String userName = this.muserName;  // params[1] is username
            String password = this.mpassword; // params[2] is password
            String deviceId = this.mdeviceId; // params[3] is deviceid
            String mobileNo = this.mmobileNo; // params[4] is mobile
            String emailId = this.memailId;  // params[5] is emailid
            String fullName = this.mfullName; // params[7] is fullname
            String jobTitle = this.mjobTitle;  // params[8] is jobtitle
            String workAddress = this.mworkAddress; // params[9] is workaddress
            String workPhone = this.mworkPhone ;  // params[10] is workphone
            String homeAddress = this.mhomeAddress; // params[11] is homeaddress

            mUser = new User();

            mUser.setmDeviceId(deviceId);
            mUser.setmFullName(fullName);
            mUser.setmMobileNo(mobileNo);
            mUser.setmEmailId(emailId);
            mUser.setmPassword(password);
            mUser.setmJobTitle(jobTitle);
            mUser.setmWorkAddress(workAddress);
            mUser.setmHomeAddress(homeAddress);
            mUser.setmWorkPhone(workPhone);
            mUser.setmUserId(userId);
            mUser.setmUserName(userName);

            jsonParams.put("user_id", userId);
            jsonParams.put("user_name", userName);
            jsonParams.put("password", password);
            jsonParams.put("mobile_no", mobileNo);
            jsonParams.put("email_id", emailId);
            jsonParams.put("device_id", deviceId);
            jsonParams.put("full_name", fullName);
            jsonParams.put("job_title", jobTitle);
            jsonParams.put("work_address", workAddress);
            jsonParams.put("work_phone", workPhone);
            jsonParams.put("home_address", homeAddress);

            try{
                if(mprofileImage != null) {
                    jsonParams.put("profile_image", convertFileToString(this.mprofileImage));
                    System.out.println("convertFileToString(profile_image)" + convertFileToString(this.mprofileImage));
                }
                else {
                    jsonParams.put("profile_image", "");
                }
            }
            catch (Exception e)
            {
                System.out.println("convertFileToString(profile_image)");
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
                if (response.getString("message").equalsIgnoreCase(KEY_SUCCESS1)) {

                    Toast.makeText(mContext, "User information updated successfully.", Toast.LENGTH_LONG).show();
                 //   progressDialog.dismiss();
                    updateUser = true;

                    String fname = "";

                    try {

                        if(mprofileImage != null) {
                            FileInputStream in = new FileInputStream(mprofileImage);
                            BufferedInputStream buf = new BufferedInputStream(in);
                            byte[] bMapArray = new byte[buf.available()];
                            buf.read(bMapArray);
                            Bitmap bMap = BitmapFactory.decodeByteArray(bMapArray, 0, bMapArray.length);

                            Bitmap image = Bitmap.createScaledBitmap(bMap, 512, 512, true);

                            String root = Environment.getExternalStorageDirectory().toString();
                            File myDir = new File(root + "/Profile");
                            if (!myDir.exists()) {
                                myDir.mkdirs();
                            }
                            fname = "Profile_Image" + ".png";

                            File file = new File(myDir, fname);
                            Log.i("file", "" + file);

                            FileOutputStream out = new FileOutputStream(file);
                            image.compress(Bitmap.CompressFormat.PNG, 100, out);
                            out.flush();
                            out.close();

                            UserTableHelper db = new UserTableHelper(mContext);

                            db.updateProfileImage(muserId, fname);
                        }



                        UserTableHelper mDb = new UserTableHelper(mContext);
                        mDb.updateUser(mUser);
                        mDb.close();

                        SharedPreferences.Editor editor = mContext.getSharedPreferences("UserProfile", mContext.MODE_PRIVATE).edit();
                        editor.putString("UserUsername", muserName);
                        editor.putString("userId", muserId);
                        editor.putString("mobileno",mmobileNo);
                        editor.putString("jobTitle",mjobTitle);
                        editor.putString("pass",mpassword);
                        editor.putString("workPhone",mworkPhone);
                        editor.putString("workAddress",mworkAddress);
                        editor.putString("deviceId",mdeviceId);
                        editor.putString("homeAddress",mhomeAddress);
                        editor.putString("fullName",mfullName);
                        editor.putString("profileImage",fname);
                        editor.putString("emailId",memailId);
                        editor.commit();


                    }
                    catch (IOException e)
                    {

                    }


                    updateUserCallBck.doPostExecute(response,updateUser);
                    progressDialog.dismiss();

                } else {
                    Toast.makeText(mContext, "Could not update user information.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(mContext, ProfileActivity.class);
                    progressDialog.dismiss();
                }
            } catch (JSONException e) {
                e.printStackTrace();

            }

        }
    }

    public interface UpdateUserCallBack{
        void doPostExecute(JSONObject response,Boolean update) throws JSONException;
    }
    // onPostExecute displays the results of the AsyncTask.

}