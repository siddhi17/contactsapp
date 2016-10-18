/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.siddhi.contactsapp.helper;

import android.content.SharedPreferences;
import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService{

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.

        // To implement: Only if user is registered, i.e. UserId is available in preference, update token on server.

        if (!token.equals("")) {

            SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("UserProfile", getApplicationContext().MODE_PRIVATE).edit();

            editor.putString("deviceId", token);

            editor.commit();

        }


        //  if (!token.equals("")) {
        // Implement code to update registration token to server

       /*     SharedPreferences sharedPreferences1 = getBaseContext().getSharedPreferences("UserProfile",getApplicationContext().MODE_PRIVATE);

            String userName = sharedPreferences1.getString("UserUsername","");
            String userId = sharedPreferences1.getString("userId","");
            String url = sharedPreferences1.getString("url","");
            boolean login = sharedPreferences1.getBoolean("login",false);
            String mobileNo = sharedPreferences1.getString("mobileno","");
            String jobTitle = sharedPreferences1.getString("jobTitle","");
            String password = sharedPreferences1.getString("pass","");
            String workPhone = sharedPreferences1.getString("workPhone","");
            String workAddress = sharedPreferences1.getString("workAddress","");
            String deviceId = sharedPreferences1.getString("deviceId","");
            String homeAddress = sharedPreferences1.getString("homeAddress","");
            String fullName = sharedPreferences1.getString("fullName","");
            String profileImage = sharedPreferences1.getString("profileImage","");
            String emailId = sharedPreferences1.getString("emailId","");


            SharedPreferences.Editor editor = getBaseContext().getSharedPreferences("UserProfile",getApplicationContext().MODE_PRIVATE).edit();
            editor.putString("UserUsername", userName);
            editor.putString("userId", userId);
            editor.putString("url", url);
            editor.putBoolean("login",login);
            editor.putString("mobileno",mobileNo);
            editor.putString("jobTitle",jobTitle);
            editor.putString("pass",password);
            editor.putString("workPhone",workPhone);
            editor.putString("workAddress",workAddress);
            editor.putString("deviceId",token);
            editor.putString("homeAddress",homeAddress);
            editor.putString("fullName",fullName);
            editor.putString("profileImage",profileImage);
            editor.putString("emailId",emailId);
            editor.commit();

            File file = new File(profileImage);

            new UpdateUserAsyncTask(this,getApplicationContext(), userId, fullName, userName, password, mobileNo, emailId, deviceId, file, workAddress, workPhone, homeAddress, jobTitle).execute();

        }*/
    }

}
