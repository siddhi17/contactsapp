package com.example.siddhi.contactsapp.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.siddhi.contactsapp.Adapter.ContactAdapter;
import com.example.siddhi.contactsapp.AsyncTasks.GetContactsAsyncTask;
import com.example.siddhi.contactsapp.AsyncTasks.GetUserAsyncTask;
import com.example.siddhi.contactsapp.AsyncTasks.ImageUserTask;
import com.example.siddhi.contactsapp.AsyncTasks.UpdateUserAsyncTask;
import com.example.siddhi.contactsapp.Contact;
import com.example.siddhi.contactsapp.R;
import com.example.siddhi.contactsapp.User;
import com.example.siddhi.contactsapp.database.ContactTableHelper;
import com.example.siddhi.contactsapp.database.UserTableHelper;
import com.example.siddhi.contactsapp.helper.DividerItemDecoration;
import com.example.siddhi.contactsapp.helper.Excpetion2JSON;
import com.example.siddhi.contactsapp.helper.RoundedImageView;
import com.example.siddhi.contactsapp.helper.ServerRequest;
import com.example.siddhi.contactsapp.helper.ServiceUrl;
import com.example.siddhi.contactsapp.helper.SquareImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements GetContactsAsyncTask.ContactGetCallBack{

    private TextView txtuserName,txtmobile;
    private static final String TAG = "RecyclerViewExample";
    private static String KEY_SUCCESS1 = "Success";
    private List<Contact> contactList = new ArrayList<>();
    private CircleImageView profileImage;
    private RecyclerView recyclerView;
    JSONArray contactListArray;
    private ContactAdapter adapter;
    Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private SharedPreferences sharedpreferences;
    private String mUserId;
    private User mUser;
    private String url;
    private Intent mIntent;
    private UserTableHelper mDb;
    private Boolean firstTimeLogin,updateUser;
    private static final int MY_PERMISSIONS_REQUEST_CALL = 20;
    private ContactTableHelper contactDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactDb = new ContactTableHelper(MainActivity.this);
        mDb = new UserTableHelper(MainActivity.this);

        sharedpreferences = getSharedPreferences("UserId", Context.MODE_PRIVATE);

        mUserId = sharedpreferences.getString("userId","");

        sharedpreferences = getSharedPreferences("InitialLogin", Context.MODE_PRIVATE);

        firstTimeLogin = sharedpreferences.getBoolean("login",false);

        setupView();

        mUser = new User();
        sharedpreferences = getSharedPreferences("Url", Context.MODE_PRIVATE);

        url= sharedpreferences.getString("url","");

        contactList = new ArrayList<Contact>();

      //  new GetUserAsyncTask(mUserId).execute(mUserId);


            // get reference to the views
        txtuserName = (TextView) findViewById(R.id.txtusername);
        txtmobile = (TextView) findViewById(R.id.txtmobile);
        profileImage = (CircleImageView) findViewById(R.id.thumbnail);
   //     Picasso.with(MainActivity.this).load(url).into(profileImage);

      /*  Target target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        File file = new File(
                                Environment.getExternalStorageDirectory().getPath()
                                        + "profile");
                        try {
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG,100,ostream);
                            ostream.close();

                            profileImage.setImageBitmap(bitmap);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {}

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {}
        };


        Picasso.with(MainActivity.this).load(url).into(target);
*/



     /*   onBackPressed();*/
            //get userId from login screen using getIntent

                            /*SharedPreferences settings =
                                    Main.this.getSharedPreferences("deep", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("profile",imageprofile);
                            editor.apply();
                            ComplexPreferences complexPreferences12 = ComplexPreferences.getComplexPreferences(this, "mypref12", MODE_PRIVATE);
                            ImageFilePath imageFilePath= complexPreferences12.getObject("imageFilePath", ImageFilePath.class);
                            File mypath=imageFilePath.getprofile();
                            Log.e("mypath",""+imageFilePath);

                            roundedImageView.setImageURI(Uri.fromFile(mypath));*/




                if (profileImage != null) {
                    profileImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            drawerLayout.closeDrawers();
                            Intent Intent = new Intent(MainActivity.this, ProfileActivity.class);
                            Intent.putExtra("url",url);
                            startActivity(Intent);
                        }
                    });
            }
        }

/*
    */

        /**
         * Logout button click event
         *//*
    btnLogout.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            // Clear the session data
            // This will clear all session data and
            // redirect user to LoginActivity
            session.logoutUser();
        }
    });
}*/

    void setupView() {


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


       navigationView = (NavigationView) findViewById(R.id.navigation_view);


       navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawerLayout.closeDrawers();
                menuItem.setChecked(true);

                FragmentManager fragmentManager = getSupportFragmentManager();

                switch (menuItem.getItemId()) {

                    case R.id.nav_menu_contacts:
                        // TODO - Do something

                       // ContactFragment fragment = new ContactFragment();
                     //   fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

                        break;

                    case R.id.nav_menu_settings:

                        break;

                    case R.id.nav_log_out:

                        sharedpreferences = getSharedPreferences("UserId", Context.MODE_PRIVATE);

                        mUserId = sharedpreferences.getString("userId","");

                        finish();
                        Intent i = new Intent(MainActivity.this,LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);

                        break;

                    // TODO - Handle other items
                }
                return true;
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        TextView mTitle = (TextView) findViewById(R.id.toolbar_title);
        Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;

        mTitle.setLayoutParams(params);
        if (toolbar != null) {
            toolbar.setTitle("");
            setSupportActionBar(toolbar);

        }

        if (toolbar != null) {

            toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(GravityCompat.START);
                    File sd = Environment.getExternalStorageDirectory();
                    File image = new File(sd+"/Profile","Profile_Image.jpg");
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeFile(image.getPath(),bmOptions);

                    if(bitmap != null) {
                        profileImage.setImageBitmap(bitmap);
                    }
                    else {
                        profileImage.setImageDrawable(ContextCompat.getDrawable(MainActivity.this,R.drawable.ic_account_circle_white_48dp));
                    }
                }
            });
        }
    }

   /* @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
        }

    }*/

    @Override
    public void doPostExecute(JSONArray response) throws JSONException {

        contactListArray = response;

        contactDb = new ContactTableHelper(MainActivity.this);

        if (null == contactList) {
            contactList = new ArrayList<Contact>();
        }

        for (int i = 0; i < contactListArray.length(); i++) {
            JSONObject subObject1 = contactListArray.getJSONObject(i);

                Contact contact = new Contact();
                JSONObject subObject = subObject1;
                String contactName = subObject.getString("user_name");
                //name of the attribute in response
                String contactId = subObject.getString("user_id");
                String contactMobile = subObject.getString("mobile_no");
                String contactEmailId = subObject.getString("email_id");
                String contactProfile = subObject.getString("profile_image");
                String fullName = subObject.getString("full_name");
                String jobTitle = subObject.getString("job_title");
                String homeAddress = subObject.getString("home_address");
                String workPhone = subObject.getString("work_phone");
                String workAddress = subObject.getString("work_address");

                contact.setmThumbnail(contactProfile);
                contact.setmUserName(contactName);
                contact.setmMobileNo(contactMobile);
                contact.setmEmailId(contactEmailId);
                contact.setmProfileImage(contactProfile);
                contact.setContactId(contactId);
                contact.setmHomeAddress(homeAddress);
                contact.setmFullName(fullName);
                contact.setmJobTitle(jobTitle);
                contact.setmWorkAddress(workAddress);
                contact.setmWorkPhone(workPhone);

                contactList.add(contact);//adding string to arraylist

            contactDb.addContact(new Contact(contactId,contactName,contactMobile,contactEmailId,contactProfile,fullName,jobTitle,workAddress,workPhone,homeAddress));
        }

        adapter = new ContactAdapter(MainActivity.this, contactList);
        recyclerView.setAdapter(adapter);
    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MainActivity.ClickListener mclickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final MainActivity.ClickListener clickListener)
        {
            this.mclickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener()
            {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e)
                {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null)
                    {
                        mclickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && mclickListener != null && gestureDetector.onTouchEvent(e))
            {
                mclickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e)
        {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept)
        {

        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if(!firstTimeLogin)
        {
            contactList.clear();
            contactList = contactDb.getAllContacts();
            mUser = mDb.getUser(mUserId);

        }
        else {
            new GetUserAsyncTask(mUserId).execute(mUserId);
            new GetContactsAsyncTask(this, MainActivity.this, mUserId).execute();

            firstTimeLogin = false;

            SharedPreferences.Editor editor = getSharedPreferences("InitialLogin",MODE_PRIVATE).edit();
            editor.putBoolean("login",firstTimeLogin);
            editor.commit();
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new ContactAdapter(MainActivity.this, contactList);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(MainActivity.this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                final Contact contact = contactList.get(position);

                // custom dialog
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setCanceledOnTouchOutside(true);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom);
                final Window window = dialog.getWindow();

                WindowManager.LayoutParams wlp =window.getAttributes();
                wlp.gravity = Gravity.CENTER_HORIZONTAL|Gravity.TOP;
                wlp.y=320;
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                window.setAttributes(wlp);

                // set the custom dialog components - text, image and button
                TextView txtusername = (TextView) dialog.findViewById(R.id.txtusername);
                TextView txtmobile = (TextView) dialog.findViewById(R.id.txtmobile);
                TextView txtemail = (TextView) dialog.findViewById(R.id.txtemail);

                txtusername.setText(contact.getmUserName());
                txtemail.setText(contact.getmEmailId());
                txtmobile.setText(contact.getmMobileNo());

                SquareImageView image = (SquareImageView) dialog.findViewById(R.id.image);
                ImageView image1 = (ImageView) dialog.findViewById(R.id.image1);
                ImageView image2 = (ImageView) dialog.findViewById(R.id.image2);
                ImageView image3 = (ImageView) dialog.findViewById(R.id.image3);

                if(contact.getmProfileImage().equals(""))

                {
                    image.setImageDrawable(ContextCompat.getDrawable(MainActivity.this,R.drawable.profile_icon));
                }
                else {
                    File file = new File(Environment.getExternalStorageDirectory() + "/ContactProfileImages/" + contact.getmProfileImage());
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), bmOptions);

                    image.setImageBitmap(bitmap);
                }

                image1.setImageResource(R.drawable.ic_call_black_24dp);
                image2.setImageResource(R.drawable.ic_textsms_black_24dp);
                image3.setImageResource(R.drawable.ic_email_black_24dp);

                image2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Uri sms_uri = Uri.parse("smsto:" + contact.getmMobileNo());
                        Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
                        startActivity(sms_intent);
                        dialog.dismiss();

                    }
                });

                image1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (ContextCompat.checkSelfPermission(MainActivity.this,
                                Manifest.permission.CALL_PHONE)
                                != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.CALL_PHONE},
                                        MY_PERMISSIONS_REQUEST_CALL);

                            }
                        else {

                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact.getmMobileNo()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            dialog.dismiss();
                        }

                    }
                });

                image3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                        intent.putExtra(Intent.EXTRA_EMAIL,contact.getmEmailId());
                        intent.putExtra(Intent.EXTRA_SUBJECT,"");
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    }
                });

                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                // if button is clicked, view all information custom dialog

                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(MainActivity.this,DetailViewActivity.class);
                        intent.putExtra("contact",contact);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }
                });
                dialog.show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    public class GetUserAsyncTask extends AsyncTask<String, Void, JSONObject> {
        String api;
        String muserId;
        JSONObject jsonParams;
        public JSONObject jsonUser;
        private ProgressDialog progressDialog;
        private String KEY_SUCCESS1 = "Success";

        public GetUserAsyncTask(String userId) {

            this.muserId = userId;

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                api = ServiceUrl.getBaseUrl() + ServiceUrl.getUser();
                jsonParams = new JSONObject();

                String userId = this.muserId; // params[0] is userid

                jsonParams.put("user_id", userId);

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


                      //  Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();
                        jsonUser = response.getJSONObject("user");

                        String userId = jsonUser.getString("user_id");
                        String userName = jsonUser.getString("user_name");
                        String password = jsonUser.getString("password");
                        String profileImage = jsonUser.getString("profile_image");
                        String mobileNo = jsonUser.getString("mobile_no");
                        String emailId = jsonUser.getString("email_id");
                        String deviceId = jsonUser.getString("device_id");
                        String fullName = jsonUser.getString("full_name");
                        String jobTitle = jsonUser.getString("job_title");
                        String homeAddress = jsonUser.getString("home_address");
                        String workPhone = jsonUser.getString("work_phone");
                        String workAddress = jsonUser.getString("work_address");


                        mUser.setmUserId(userId);
                        mUser.setmMobileNo(mobileNo);
                        mUser.setmProfileImage(profileImage);
                        mUser.setmUserName(userName);
                        mUser.setmDeviceId(deviceId);
                        mUser.setmPassword(password);
                        mUser.setmEmailId(emailId);
                        mUser.setmFullName(fullName);
                        mUser.setmJobTitle(jobTitle);
                        mUser.setmHomeAddress(homeAddress);
                        mUser.setmWorkPhone(workPhone);
                        mUser.setmWorkAddress(workAddress);



                        mDb = new UserTableHelper(MainActivity.this);
                        mDb.addUser(new User(userId,userName,password,mobileNo,emailId,profileImage,fullName,
                                deviceId,jobTitle,homeAddress,workAddress,workPhone));

                        Log.e("userFromDatabase",String.valueOf(mDb.getUser(userId)));

                        String url = ServiceUrl.getBaseUrl() + ServiceUrl.getImageUserUrl() + profileImage;
                        Log.e("url", url);


                        new ImageUserTask(MainActivity.this,url).execute();

                        txtuserName.setText(mUser.getmUserName());
                        txtmobile.setText(mUser.getmMobileNo());


                    } else {
                        Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }
        // onPostExecute displays the results of the AsyncTask.
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    try {

                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"));
                        startActivity(intent);
                    }
                    catch (SecurityException e)
                    {

                    }
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
