package com.example.siddhi.contactsapp.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
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


    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("CheckLists");
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
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(RegisterActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

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
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
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
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mProfileImage = destination;
        profile_image.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
     //   try {
          //  File outputDir = getCacheDir(); // Activity context
          //  File outputFile = File.createTempFile("temp.jpg",null, outputDir);

       //    mProfileImage = outputFile;
      //  }
     //   catch (IOException e)
    //    {

      //  }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, stream); //replace 100 with desired quality percentage.
        byte[] byteArray = stream.toByteArray();

        try {


            File tempFile = File.createTempFile("temp",null, getCacheDir());
            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.write(byteArray);

            mProfileImage = tempFile;
        }
        catch (IOException e)
        {

        }

        profile_image.setImageBitmap(bm);


    }


    public static Bitmap decodeSampledBitmapFromResource(String pathToFile,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathToFile, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        Log.e("inSampleSize", "inSampleSize______________in storage"
                + options.inSampleSize);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathToFile, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

        }

        return inSampleSize;
    }

    private void registerUser() {
        String userName = edtuserName.getText().toString();
        String fullName = edtfullName.getText().toString();
        String password = edtPassword.getText().toString();
        String confirm = edtconfirmPassword.getText().toString();
        String mobileNo = edtmobile.getText().toString();
        String emailId = edtemail.getText().toString();
        String deviceId = "233";

        new RegisterUserAsyncTask(RegisterActivity.this, fullName, userName, password, mobileNo, emailId, deviceId,mProfileImage).execute();

    }

}
