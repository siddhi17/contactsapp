package com.example.siddhi.contactsapp.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.siddhi.contactsapp.AsyncTasks.LoginUserAsyncTask;
import com.example.siddhi.contactsapp.AsyncTasks.SendMultipleInvitesAsyncTask;
import com.example.siddhi.contactsapp.R;
import com.example.siddhi.contactsapp.helper.MyFirebaseInstanceIDService;
import com.example.siddhi.contactsapp.helper.ReadContactsPrmission;
import com.example.siddhi.contactsapp.helper.ReadPhoneStatePermission;
import com.example.siddhi.contactsapp.helper.SendSmsPermission;
import com.example.siddhi.contactsapp.helper.Utility;
import com.example.siddhi.contactsapp.helper.Validation;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText txtuserName;
    private EditText txtpasswordName;
    private static String KEY_SUCCESS = "user authenticated successfully";
    private static String KEY_SUCCESS1 = "Success";
    //Declaring the Button object
    private Button btnsignUp;
    private Button btnsignIn;
    private Button btnforgotPassword;
    private String refreshedToken;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 111;
    public static final int READ_CONTACTS = 11;
    public static final int SEND_SMS = 21;
    public static final int WRITE_EXTERNAL_STORAGE = 41;
    public static final int CAMERA = 31;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        boolean result = Utility.checkAndRequestPermissions(LoginActivity.this);


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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
            {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.SEND_SMS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted

                } else {
                    // Permission Denied
                    Toast.makeText(LoginActivity.this, "Some Permission are Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
