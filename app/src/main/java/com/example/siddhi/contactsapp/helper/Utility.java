package com.example.siddhi.contactsapp.helper;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shreya Kotak on 12/05/16.
 */
public class Utility {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 111;

    public static boolean checkAndRequestPermissions(final Activity activity) {


        int permissionSendMessage = ContextCompat.checkSelfPermission(activity,Manifest.permission.SEND_SMS);
        int storagePermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        int contactsPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS);
        int callPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (contactsPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }

        if (callPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
}

