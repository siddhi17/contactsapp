package com.example.siddhi.contactsapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.siddhi.contactsapp.AsyncTasks.LoginUserAsyncTask;
import com.example.siddhi.contactsapp.R;
import com.example.siddhi.contactsapp.helper.MyFirebaseInstanceIDService;
import com.example.siddhi.contactsapp.helper.ReadContactsPrmission;
import com.example.siddhi.contactsapp.helper.ReadPhoneStatePermission;
import com.example.siddhi.contactsapp.helper.Utility;
import com.example.siddhi.contactsapp.helper.Validation;

public class LoginActivity extends AppCompatActivity {

    private EditText txtuserName;
    private EditText txtpasswordName;
    private static String KEY_SUCCESS = "user authenticated successfully";
    private static String KEY_SUCCESS1 = "Success";
    //Declaring the Button object
    private Button btnsignUp;
    private Button btnsignIn;
    private Button btnforgotPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        boolean result= Utility.checkPermission(LoginActivity.this);

        boolean re = ReadContactsPrmission.checkPermission(LoginActivity.this);

        boolean r = ReadPhoneStatePermission.checkPermission(LoginActivity.this);

        txtuserName = (EditText) findViewById(R.id.edtusername);
        txtpasswordName = (EditText) findViewById(R.id.edtpass);
        //
        txtpasswordName.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.yourColor), PorterDuff.Mode.SRC_ATOP);
        txtuserName.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.yourColor), PorterDuff.Mode.SRC_ATOP);
        // get reference to the views
        btnsignIn = (Button) findViewById(R.id.btnsignin);
        btnsignUp = (Button) findViewById(R.id.btnsignup);
        btnforgotPassword= (Button) findViewById(R.id.btnforgotpassword);
        if (btnsignIn != null) {
            btnsignIn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View arg0)
                {
                    if (checkValidation())
                    {
                        String userName = txtuserName.getText().toString();
                        String password = txtpasswordName.getText().toString();
                        //Asynctask for Login screen
                        new LoginUserAsyncTask(LoginActivity.this,userName, password).execute();

                    }
                    else
                        Toast.makeText(LoginActivity.this, "Form contains error", Toast.LENGTH_LONG).show();

                }
            });
        }
        if (btnsignUp != null) {
            btnsignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                    startActivity(intent);
                }
            });
        }
        if (btnforgotPassword != null) {
            btnforgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                    startActivity(intent);
                }
            });
        }


    }

    private boolean checkValidation() {
        boolean ret = true;

        if (!Validation.hasText(txtuserName)) ret = false;
        if (!Validation.hasText(txtpasswordName)) ret = false;


        return ret;
    }
    private void LoginUser() {




    }

}
