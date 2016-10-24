package com.example.siddhi.contactsapp.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.siddhi.contactsapp.AsyncTasks.RegisterUserAsyncTask;
import com.example.siddhi.contactsapp.R;
import com.example.siddhi.contactsapp.helper.Utility;
import com.example.siddhi.contactsapp.helper.Validation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtfullName;
    private EditText edtemail;
    private EditText edtmobile;
    private EditText edtconfirmPassword;
    private EditText edtPassword;
    private EditText edtuserName;
    private File mProfileImage = null;
    private ImageView profile_image;
    private String userChoosenTask;
    private ImageView imageViewBack;
    private SharedPreferences sharedpreferences;
    private boolean mResult;
    private Bitmap selectedBitmap;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 111;
 private boolean result;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String fileName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageViewBack = (ImageView)findViewById(R.id.imageViewBack) ;

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        profile_image = (ImageView) findViewById(R.id.thumbnail);

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        edtfullName = (EditText) findViewById(R.id.edtname);

        edtfullName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(edtfullName);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        edtuserName = (EditText) findViewById(R.id.edtusername);

        edtuserName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(edtuserName);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        edtmobile = (EditText) findViewById(R.id.edtmobileno);
        edtmobile.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.isPhoneNumber(edtmobile, true);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        edtemail = (EditText) findViewById(R.id.edtemail);

        edtemail.addTextChangedListener(new TextWatcher() {
            // after every change has been made to this
            // ditText, we would like to check validity
            public void afterTextChanged(Editable s) {

                Validation.isEmailAddress(edtemail, true);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        edtconfirmPassword = (EditText) findViewById(R.id.edtconpass);

        edtPassword = (EditText) findViewById(R.id.edtpass);
        edtfullName.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.yourColor1), PorterDuff.Mode.SRC_ATOP);
        edtuserName.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.yourColor1), PorterDuff.Mode.SRC_ATOP);
        edtmobile.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.yourColor1), PorterDuff.Mode.SRC_ATOP);
        edtemail.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.yourColor1), PorterDuff.Mode.SRC_ATOP);
        edtconfirmPassword.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.yourColor1), PorterDuff.Mode.SRC_ATOP);
        edtPassword.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.yourColor1), PorterDuff.Mode.SRC_ATOP);

        Button b1 = (Button) findViewById(R.id.btnregister);
        if (b1 != null) {

            b1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    if (checkValidation()) {

                        registerUser();

                    } else
                        Toast.makeText(RegisterActivity.this, "Form contains error", Toast.LENGTH_LONG).show();

                }
            });
        }


    }

    private boolean checkValidation() {
        boolean ret = true;

        if (!Validation.hasText(edtfullName)) ret = false;
        if (!Validation.hasText(edtuserName)) ret = false;
        if (!Validation.isEmailAddress(edtemail, true)) ret = false;
        if (!Validation.isPhoneNumber(edtmobile, false)) ret = false;
        if (!edtconfirmPassword.getText().toString().equals(edtPassword.getText().toString()))
            ret = false;

        return ret;
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                    if (items[item].equals("Take Photo")) {
                        userChoosenTask = "Take Photo";

                        result = Utility.checkAndRequestPermissions(RegisterActivity.this);

                        if (result) {
                            cameraIntent();
                        }

                    } else if (items[item].equals("Choose from Library")) {
                        userChoosenTask = "Choose from Library";

                        result = Utility.checkAndRequestPermissions(RegisterActivity.this);

                        if (result) {
                            galleryIntent();
                        }

                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }

            }
        });
        builder.show();

    }

    private void galleryIntent()
    {

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);//
            startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);

    }

    private void cameraIntent()
    {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
             //   UiUtils.showAlert(getString(R.string.error),NewGroupAcvitity.this);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
               intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(intent,REQUEST_CAMERA);
            }
        }

    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "image";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        fileName =  image.getAbsolutePath();
        return image;
    }

    public void loadImageFromFile(String imageFile){

        try {
            ExifInterface ei = new ExifInterface(imageFile);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            Bitmap bitmap = BitmapFactory.decodeFile(imageFile);

            Bitmap rotatedBitmap = null;

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;
                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
                    break;
            }

            if(rotatedBitmap != null)
            {
                profile_image.setImageBitmap(rotatedBitmap);
                selectedBitmap = rotatedBitmap;

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                selectedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); //replace 100 with desired quality percentage.
                byte[] byteArray = stream.toByteArray();

                    File tempFile = File.createTempFile("temp",null, getCacheDir());
                    FileOutputStream fos = new FileOutputStream(tempFile);
                    fos.write(byteArray);

                    mProfileImage = tempFile;
            }

        }
        catch (IOException ex) {
          //  UiUtils.showAlert(getString(R.string.error),NewGroupAcvitity.this);
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix,
                true);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {

        loadImageFromFile(fileName);

    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Uri uri = (Uri)data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri,filePathColumn, null, null, null);
        if(cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            loadImageFromFile(picturePath);
        }
    }

    private void registerUser() {
        String userName = edtuserName.getText().toString();
        String fullName = edtfullName.getText().toString();
        String password = edtPassword.getText().toString();
        String confirm = edtconfirmPassword.getText().toString();
        String mobileNo = edtmobile.getText().toString();
        String emailId = edtemail.getText().toString();
        String deviceId = "";

        new RegisterUserAsyncTask(RegisterActivity.this, fullName, userName, password, mobileNo, emailId, deviceId,mProfileImage).execute();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
            {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);

                //    Toast.makeText(RegisterActivity.this, "Some Permission are Denied", Toast.LENGTH_SHORT).show();

                if(userChoosenTask.equals("Take Photo"))
                {
                    cameraIntent();
                }
                else if(userChoosenTask.equals("Choose from Library"))
                {
                    galleryIntent();
                }

            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


}
