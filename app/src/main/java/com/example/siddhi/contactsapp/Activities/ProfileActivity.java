package com.example.siddhi.contactsapp.Activities;

import android.app.Activity;
import android.content.Context;
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
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.siddhi.contactsapp.AsyncTasks.GetUserAsyncTask;
import com.example.siddhi.contactsapp.AsyncTasks.ImageUserTask;
import com.example.siddhi.contactsapp.AsyncTasks.UpdateUserAsyncTask;
import com.example.siddhi.contactsapp.R;
import com.example.siddhi.contactsapp.User;
import com.example.siddhi.contactsapp.database.DatabaseHelper;
import com.example.siddhi.contactsapp.database.UserTableHelper;
import com.example.siddhi.contactsapp.helper.ServiceUrl;
import com.example.siddhi.contactsapp.helper.Utility;
import com.example.siddhi.contactsapp.helper.Validation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileActivity extends AppCompatActivity implements GetUserAsyncTask.GetUserCallBack,UpdateUserAsyncTask.UpdateUserCallBack{
    //Declaring the EditText object
    EditText edtfullName;
    EditText edtemail;
    EditText edtmobile;
    EditText edtPassword;
    EditText edtuserName;
    EditText userId;

    EditText edtworkPhone;
    EditText edtworkAdd;
    EditText edthomeAdd;

    private User mUser = new User();
    EditText edtjobTitles;
    //Declaring the ImageView object
    private CircleImageView profileImageView;
    // Declaring the Toolbar Object
    private Toolbar toolbar;
    //Declaring the CoordinatorLayout Object
    private RelativeLayout coordinatorLayout;
    //Message of server response
    private static String KEY_SUCCESS = "Success";
    private static String KEY_SUCCESS1 = "User Updated Successfully.";
    //selected image in file
    File profileImage = null,mSavedProfileImage;
    private SharedPreferences sharedpreferences;
    private String mUserId,url;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Boolean mProfile,mUpdateUser;
    private Intent mIntent;
    private UserTableHelper mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);
        sharedpreferences = getSharedPreferences("UserId", Context.MODE_PRIVATE);

        mUserId = sharedpreferences.getString("userId","");

        mDb = new UserTableHelper(ProfileActivity.this);

        mUser = mDb.getUser(mUserId);

        // get reference to the views

        edtfullName = (EditText) findViewById(R.id.edtname);
        edtuserName = (EditText) findViewById(R.id.edtusername);
        edtuserName.setEnabled(false);
        edtuserName.setKeyListener(null);
        edtmobile = (EditText) findViewById(R.id.edtmobileno);
        edtemail = (EditText) findViewById(R.id.edtemail);
        edtPassword = (EditText) findViewById(R.id.edtpass);
        edthomeAdd = (EditText) findViewById(R.id.edthomeaddress);
        edtworkAdd = (EditText) findViewById(R.id.edtworkaddress);
        edtworkPhone = (EditText) findViewById(R.id.edtwork_phone);
        edtjobTitles = (EditText) findViewById(R.id.edtjodtitle);


        profileImageView = (CircleImageView)findViewById(R.id.thumbnail);

        File sd = Environment.getExternalStorageDirectory();
        File image = new File(sd+"/Profile","Profile_Image.jpg");
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getPath(),bmOptions);

        if(bitmap != null) {

            profileImageView.setImageBitmap(bitmap);
        }
        else {
            profileImageView.setImageDrawable(ContextCompat.getDrawable(ProfileActivity.this,R.drawable.profile_icon));
        }

        sharedpreferences = getSharedPreferences("Url", Context.MODE_PRIVATE);

        url = sharedpreferences.getString("url","");

     //   Picasso.with(ProfileActivity.this).load(url).into(profileImageView);
        //Changing EditText bottom line color
        edtfullName.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.yourColor1), PorterDuff.Mode.SRC_ATOP);
        edtuserName.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.yourColor1), PorterDuff.Mode.SRC_ATOP);
        edtmobile.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.yourColor1), PorterDuff.Mode.SRC_ATOP);
        edtemail.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.yourColor1), PorterDuff.Mode.SRC_ATOP);
        edtPassword.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.yourColor1), PorterDuff.Mode.SRC_ATOP);
        edtworkPhone.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.yourColor1), PorterDuff.Mode.SRC_ATOP);
        edtworkAdd.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.yourColor1), PorterDuff.Mode.SRC_ATOP);
        edthomeAdd.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.yourColor1), PorterDuff.Mode.SRC_ATOP);
        edtjobTitles.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.yourColor1), PorterDuff.Mode.SRC_ATOP);

        setUI();
      //validation
        edtfullName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(edtfullName);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        edtuserName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(edtuserName);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        edtmobile.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.isPhoneNumber(edtmobile, true);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

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
        edthomeAdd.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(edthomeAdd);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        edtworkAdd.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                Validation.hasText(edtworkAdd);
            }

            public void beforeTextChanged(CharSequence s,
                                          int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        edtjobTitles.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                Validation.hasText(edtjobTitles);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

         // get reference to the views
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) findViewById(R.id.toolbar_title);
        Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;

        // get reference to the views
        coordinatorLayout = (RelativeLayout) findViewById(R.id.coordinatorLayout);
        mTitle.setLayoutParams(params);

        if (toolbar != null) {
            toolbar.setTitle(" ");
            setSupportActionBar(toolbar);
        }

        if (toolbar != null) {
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                    finish();

                }


            });
        }

        profileImageView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                        selectImage();
                    }
                });

            //String imageprofile = loginData.getprofileImage();

        }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();

        finish();

        Intent i = new Intent(ProfileActivity.this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);

    }

    private boolean checkValidation() {
        boolean ret = true;

        if (!Validation.hasText(edtfullName)) ret = false;
        //if (!Validation.hasText(edthomeAdd)) ret = false;
       // if (!Validation.hasText(edtworkAdd)) ret = false;
        //if (!Validation.hasText(edtjobTitles)) ret = false;
        if (!Validation.hasText(edtuserName)) ret = false;
        if (!Validation.isEmailAddress(edtemail, true)) ret = false;
        if (!Validation.isPhoneNumber(edtmobile, false)) ret = false;
        //if (!Validation.isPhoneNumber(edtworkPhone, false)) ret = false;


        return ret;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_save_ec:
                if (checkValidation()) {
                    updateuser();
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Profile information was saved", Snackbar.LENGTH_LONG);
                    snackbar.show();

                } else
                    Toast.makeText(ProfileActivity.this, "Form contains error", Toast.LENGTH_LONG).show();

                break;

            default:
                break;
        }

        return true;
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

        profileImage = destination;
        profileImageView.setImageBitmap(thumbnail);
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

            profileImage = tempFile;
        }
        catch (IOException e)
        {


        }

        profileImageView.setImageBitmap(bm);


    }
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(ProfileActivity.this);

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

    public static Bitmap decodeSampledBitmapFromResource(String pathToFile,
                                                         int reqWidth, int reqHeight)
    {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathToFile, options);

        // Calculate inSampleSize
      //  options.inSampleSize = calculateInSampleSize(options, reqWidth,
        //        reqHeight);

        Log.e("inSampleSize", "inSampleSize______________in storage"
                + options.inSampleSize);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathToFile, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight)
    {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth)
        {

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


    private void updateuser() {

        String userName = edtuserName.getText().toString();

        String fullName = edtfullName.getText().toString();
        String mobileNo = edtmobile.getText().toString();

        String emailId = edtemail.getText().toString();
        String deviceId = mUser.getmDeviceId();
        String password = edtPassword.getText().toString();
        String userId = mUser.getmUserId();
        String status = mUser.getmStatus();
        String imageprofile = mUser.getmProfileImage();

        String jobTitle = edtjobTitles.getText().toString();

        String workAddress = edtworkAdd.getText().toString();

        String workPhone = edtworkPhone.getText().toString();
        if (!isValidTextBox(workPhone)) {
            edtworkPhone.setError("please enter ten digit mobile number");
        }

        String homeAddress = edthomeAdd.getText().toString();


        if (profileImage == null) {
            new UpdateUserAsyncTask(this,ProfileActivity.this, userId, fullName, userName, password, mobileNo, emailId, status, deviceId, mSavedProfileImage, workAddress, workPhone, homeAddress, jobTitle).execute();

        } else {

            new UpdateUserAsyncTask(this,ProfileActivity.this, userId, fullName, userName, password, mobileNo, emailId, status, deviceId, profileImage, workAddress, workPhone, homeAddress, jobTitle).execute();

            //  String profile = convertFileToString(profileImage);
        }
    }

    private boolean isValidTextBox(String workPhone) {
        if (edtworkPhone.length() < 11) {
            return true;
        }
        return false;
    }
    @Override
    public void doPostExecute(JSONObject response) throws JSONException {
        JSONObject userJs = response;

        String userId = userJs.getString("user_id");
        String userName = userJs.getString("user_name");
        String password = userJs.getString("password");
        String profileImage = userJs.getString("profile_image");
        String mobileNo = userJs.getString("mobile_no");
        String emailId = userJs.getString("email_id");
        String deviceId = userJs.getString("device_id");
        String fullName = userJs.getString("full_name");
        String status = "1";
        String jobTitle = userJs.getString("job_title");
        String homeAddress = userJs.getString("home_address");
        String workPhone = userJs.getString("work_phone");
        String workAddress = userJs.getString("work_address");


        mUser.setmFullName(fullName);
        mUser.setmWorkAddress(workAddress);
        mUser.setmDeviceId(deviceId);
        mUser.setmWorkPhone(workPhone);
        mUser.setmUserId(userId);
        mUser.setmJobTitle(jobTitle);
        mUser.setmEmailId(emailId);
        mUser.setmHomeAddress(homeAddress);
        mUser.setmPassword(password);
        mUser.setmMobileNo(mobileNo);
        mUser.setmStatus(status);
        mUser.setmProfileImage(profileImage);
        mUser.setmUserName(userName);


            edtuserName.setText(mUser.getmUserName());
            edtfullName.setText(mUser.getmFullName());
            edtmobile.setText(mUser.getmMobileNo());
            edtemail.setText(mUser.getmEmailId());
            edtPassword.setText(mUser.getmPassword());
            edthomeAdd.setText(mUser.getmHomeAddress());
            edtworkAdd.setText(mUser.getmWorkAddress());
            edtworkPhone.setText(mUser.getmWorkPhone());
            edtjobTitles.setText(mUser.getmJobTitle());
            String imageprofile = mUser.getmProfileImage();

        mDb = new UserTableHelper(ProfileActivity.this);
        mDb.addUser(new User(userId,userName,password,mobileNo,emailId,imageprofile,fullName,
                deviceId,jobTitle,homeAddress,workAddress,workPhone));

        Log.e("userFromDatabase",String.valueOf(mDb.getUser(userId)));
        String url = ServiceUrl.getBaseUrl() + ServiceUrl.getImageUserUrl() + imageprofile;
        Log.e("url", url);

        new ImageUserTask(ProfileActivity.this,url).execute();

        Intent i = new Intent(ProfileActivity.this,ProfileActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }
    private String convertFileToString(File profileImage) throws IOException {

            Bitmap bm = BitmapFactory.decodeFile(profileImage.getPath());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 10, baos);
            byte[] b = baos.toByteArray();


            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encodedImage;

    }
    public void setUI()
    {


        edtuserName.setText(mUser.getmUserName());
        edtfullName.setText(mUser.getmFullName());
        edtmobile.setText(mUser.getmMobileNo());
        edtemail.setText(mUser.getmEmailId());
        edtPassword.setText(mUser.getmPassword());
        edthomeAdd.setText(mUser.getmHomeAddress());
        edtworkAdd.setText(mUser.getmWorkAddress());
        edtworkPhone.setText(mUser.getmWorkPhone());
        edtjobTitles.setText(mUser.getmJobTitle());


    }
    @Override
    public void doPostExecute(JSONObject response,Boolean update) throws JSONException {

        String status = response.getString("status");

        mUpdateUser = update;

        new GetUserAsyncTask(this, ProfileActivity.this, mUserId).execute(mUserId);

    }

}
