package com.example.siddhi.contactsapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.siddhi.contactsapp.AsyncTasks.LoginUserAsyncTask;
import com.example.siddhi.contactsapp.database.DatabaseHelper;
import com.example.siddhi.contactsapp.helper.Utility;

/**
 * Created by Siddhi on 9/18/2016.
 */
public class StartUpActivity extends AppCompatActivity {

    private SharedPreferences sharedpreferences;
    private String userUsername;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseHelper db = new DatabaseHelper(this);
        db.createDatabase();


        sharedpreferences = getSharedPreferences("username", Context.MODE_PRIVATE);

        userUsername = sharedpreferences.getString("UserUsername","");


            if (userUsername.equals("")) {

                Intent i = (new Intent(StartUpActivity.this, LoginActivity.class));
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

                finish();

            } else {

                Intent i = (new Intent(StartUpActivity.this,MainActivity.class));
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

                finish();
            }

    }

}
