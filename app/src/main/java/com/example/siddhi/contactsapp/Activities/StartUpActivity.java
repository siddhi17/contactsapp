package com.example.siddhi.contactsapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.siddhi.contactsapp.AsyncTasks.LoginUserAsyncTask;
import com.example.siddhi.contactsapp.database.ContactTableHelper;
import com.example.siddhi.contactsapp.database.DatabaseHelper;
import com.example.siddhi.contactsapp.database.UserTableHelper;
import com.example.siddhi.contactsapp.helper.Constants;
import com.example.siddhi.contactsapp.helper.Utility;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by Siddhi on 9/18/2016.
 */
public class StartUpActivity extends AppCompatActivity {

    private SharedPreferences sharedpreferences;
    private String userUsername,refreshedToken;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedpreferences = getSharedPreferences("UserProfile", Context.MODE_PRIVATE);

        userUsername = sharedpreferences.getString("UserUsername","");

            if (userUsername.equals("")) {

               // getApplicationContext().deleteDatabase(Constants.DATABASE_NAME);

             /*   ContactTableHelper db = new ContactTableHelper(this);
                db.deleteAllContacts();
                db.close();

                UserTableHelper db1 = new UserTableHelper(this);
                db1.deleteAllUsers();
                db1.close();*/

                Intent i = (new Intent(StartUpActivity.this, LoginActivity.class));
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);

                finish();

            } else {

                Intent i = (new Intent(StartUpActivity.this,MainActivity.class));
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);

                finish();
            }

    }

}
