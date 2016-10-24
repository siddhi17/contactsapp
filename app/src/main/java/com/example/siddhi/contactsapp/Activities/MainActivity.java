package com.example.siddhi.contactsapp.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceActivity;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.siddhi.contactsapp.Adapter.ContactAdapter;
import com.example.siddhi.contactsapp.AsyncTasks.GetContactsAsyncTask;
import com.example.siddhi.contactsapp.AsyncTasks.ImageUserTask;
import com.example.siddhi.contactsapp.AsyncTasks.UpdateUserAsyncTask;
import com.example.siddhi.contactsapp.Contact;
import com.example.siddhi.contactsapp.R;
import com.example.siddhi.contactsapp.User;
import com.example.siddhi.contactsapp.database.ContactTableHelper;
import com.example.siddhi.contactsapp.database.UserTableHelper;
import com.example.siddhi.contactsapp.helper.DividerItemDecoration;
import com.example.siddhi.contactsapp.helper.Excpetion2JSON;
import com.example.siddhi.contactsapp.helper.NotificationUtils;
import com.example.siddhi.contactsapp.helper.ServerRequest;
import com.example.siddhi.contactsapp.helper.ServiceUrl;
import com.example.siddhi.contactsapp.helper.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements GetContactsAsyncTask.ContactGetCallBack,UpdateUserAsyncTask.UpdateUserCallBack {

    private TextView txtuserName, txtmobile;
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
    private Boolean firstTimeLogin, updateUser;
    private static final int MY_PERMISSIONS_REQUEST_CALL = 20;
    private ContactTableHelper contactDb;
    private File image;
    String refreshedToken;
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    public static final String ACTION_ACCEPT = "Accept";
    private static final String USER_TABLE = "userTable";
    private static final String CONTACT_TABLE = "contactTable";
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 111;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contactDb = new ContactTableHelper(MainActivity.this);
        mDb = new UserTableHelper(MainActivity.this);

     //   Intent intent=new Intent(MainActivity.this,MyFirebaseInstanceIDService.class);
      //  startService(intent);
        boolean result = Utility.checkAndRequestPermissions(MainActivity.this);

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());



        sharedpreferences = getSharedPreferences("UserProfile", Context.MODE_PRIVATE);

        mUserId = sharedpreferences.getString("userId", "");

        firstTimeLogin = sharedpreferences.getBoolean("login", false);

        refreshedToken = sharedpreferences.getString("deviceId","");

        setupView();

        mUser = new User();

        url = sharedpreferences.getString("url", "");

        contactList = new ArrayList<Contact>();

        //  new GetUserAsyncTask(mUserId).execute(mUserId);


        // get reference to the views
        txtuserName = (TextView) findViewById(R.id.txtusername);
        txtmobile = (TextView) findViewById(R.id.txtmobile);
        profileImage = (CircleImageView) findViewById(R.id.thumbnail);

        if (profileImage != null) {
            profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.closeDrawers();
                    Intent Intent = new Intent(MainActivity.this, ProfileActivity.class);
                    Intent.putExtra("user", mUser);
                    Intent.putExtra("url", url);
                    startActivity(Intent);
                }
            });
        }


        ImageView sync = (ImageView)findViewById(R.id.sync);

        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                contactList.clear();
                contactDb.deleteAllContacts();

                GetContactsAsyncTask getContactsAsyncTask = new GetContactsAsyncTask(MainActivity.this,MainActivity.this,mUserId);
                getContactsAsyncTask.execute(mUserId);

            }
        });

    }

    void setupView() {
        File sd = Environment.getExternalStorageDirectory();
        image = new File(sd+"/Profile","Profile_Image.png");

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

                        Intent i = new Intent(MainActivity.this, PreferenceActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);

                        break;

                    case R.id.nav_log_out:

                        SharedPreferences pref = getSharedPreferences("UserProfile",MODE_PRIVATE);

                        SharedPreferences.Editor editor = pref.edit();
                        editor.clear();
                        editor.commit();

                        mDb.deleteAllUsers();
                        contactDb.deleteAllContacts();


                        // db.delete(String tableName, String whereClause, String[] whereArgs);
                        // If whereClause is null, it will delete all rows.

                        finish();
                        i = new Intent(MainActivity.this, LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);

                        break;

                    case R.id.nav_invite:

                        i = new Intent(MainActivity.this,PendingInvitesActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);

                        break;



                    // TODO - Handle other items
                }
                return true;
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        TextView mTitle = (TextView) findViewById(R.id.toolbar_title);
      //  RelativeLayout params = new RelativeLayout(RelativeLayout.p.WRAP_CONTENT, Toolbar.LayoutParams.MATCH_PARENT);
      //  params.gravity = Gravity.CENTER_HORIZONTAL;



        final ImageView menu = (ImageView) findViewById(R.id.menu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(MainActivity.this, menu);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.main_pop_up_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getItemId() == R.id.notifications) {



                        }
                        else if(item.getItemId() == R.id.pendingInvites)
                        {

                            startActivity(new Intent(MainActivity.this,PendingInvitesActivity.class));

                        }


                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });//closing the setOnClickListener method


       // mTitle.setLayoutParams(params);
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

                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeFile(image.getPath(),bmOptions);

                    if(bitmap != null) {
                        profileImage.setImageBitmap(bitmap);
                    }
                    else {
                        profileImage.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_account_circle_white_48dp));
                    }
                }
            });
        }
    }

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
            String pass = subObject.getString("password");
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
            contact.setmPass(pass);

            contactList.add(contact);//adding string to arraylist

            contactDb.addContact(new Contact(contactId,contactName,pass,contactMobile,contactEmailId,contactProfile,fullName,jobTitle,workAddress,workPhone,homeAddress));

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

        contactList.clear();

        if(!firstTimeLogin)
        {
            contactList.clear();
            contactList = contactDb.getAllContacts();
            mUser = mDb.getUser(mUserId);

            txtuserName.setText(mUser.getmUserName());
            txtmobile.setText(mUser.getmMobileNo());
        }
        else {
            new GetUserAsyncTask(mUserId).execute(mUserId);
            new GetContactsAsyncTask(this, MainActivity.this, mUserId).execute();

            firstTimeLogin = false;

            SharedPreferences.Editor editor = getSharedPreferences("UserProfile",MODE_PRIVATE).edit();
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

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

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
                perms.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.SEND_SMS, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);

                if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted

                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "Some Permission are Denied", Toast.LENGTH_SHORT)
                            .show();
                }

//                Toast.makeText(RegisterActivity.this, "Some Permission are Denied", Toast.LENGTH_SHORT).show();

            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
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
                        String fullName = jsonUser.getString("full_name");
                        String jobTitle = jsonUser.getString("job_title");
                        String homeAddress = jsonUser.getString("home_address");
                        String workPhone = jsonUser.getString("work_phone");
                        String workAddress = jsonUser.getString("work_address");
                        String deviceId = jsonUser.getString("device_id");


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
                        mDb.addUser(new User(userId, userName, password, mobileNo, emailId, profileImage, fullName,
                               deviceId, jobTitle, homeAddress, workAddress, workPhone));

                        Log.e("userFromDatabase", String.valueOf(mDb.getUser(userId)));

                        String url = ServiceUrl.getBaseUrl() + ServiceUrl.getImageUserUrl() + profileImage;
                        Log.e("url", url);


                        new ImageUserTask(MainActivity.this, url).execute();

                        txtuserName.setText(mUser.getmUserName());
                        txtmobile.setText(mUser.getmMobileNo());

                     //   new UpdateUserAsyncTask(MainActivity.this, userId, fullName, userName, password, mobileNo, emailId,refreshedToken,image, workAddress, workPhone, homeAddress, jobTitle).execute();

                    } else {
                        Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }
    }
        // Add custom implementation, as needed.

        // To implement: Only if user is registered, i.e. UserId is available in preference, update token on server.

    @Override
    public void doPostExecute(JSONObject response, Boolean update) throws JSONException {


    }
    public class UpdateUserAsyncTask extends AsyncTask<String, Void, JSONObject> {
        String api;
        JSONObject jsonParams;
        String muserName;
        String mfullName;
        String mpassword;
        String mmobileNo;
        String memailId;
        String mdeviceId;
        String muserId;
        String mstatus;
        String mjobTitle ;
        String mworkAddress;
        String mworkPhone ;
        String mhomeAddress;
        File mprofileImage;
        private String KEY_SUCCESS1 = "User Updated Successfully.";
        Bitmap result;
        private Context mContext;

        public UpdateUserAsyncTask(Context context, String userId) {
            this.mContext = context;

            this.muserId = userId;

        }

        public UpdateUserAsyncTask(Context context, String userId, String fullName, String userName, String password, String mobileNo, String emailId, String deviceId, File profileImage, String workAddress, String workPhone, String homeAddress, String jobTitle) {
            this.mContext = context;
            this.muserName = userName;
            this.mpassword = password;
            this.mfullName = fullName;
            this.mmobileNo = mobileNo;
            this.memailId = emailId;
            this.mdeviceId = deviceId;
            this.mprofileImage = profileImage;
            this.mjobTitle = jobTitle;
            this.mworkAddress = workAddress;
            this.mworkPhone = workPhone ;
            this.mhomeAddress = homeAddress;
            this.muserId = userId;
            this.result = result;
        }

        private String convertFileToString(File profileImage) throws IOException {

            Bitmap bm = BitmapFactory.decodeFile(this.mprofileImage.getPath());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();

            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

            return encodedImage;}


        private boolean hasImage(@NonNull CircleImageView view) {
            Drawable drawable = view.getDrawable();
            boolean hasImage = (drawable != null);

            if (hasImage && (drawable instanceof BitmapDrawable)) {
                hasImage = ((BitmapDrawable)drawable).getBitmap() != null;
            }

            return hasImage;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                //url
                api = ServiceUrl.getBaseUrl() + ServiceUrl.getUpdateUserUrl();

                // build jsonObject
                jsonParams = new JSONObject();

                String userId = this.muserId;  // params[0] is userid
                String userName = this.muserName;  // params[1] is username
                String password = this.mpassword; // params[2] is password
                String deviceId = this.mdeviceId; // params[3] is deviceid
                String mobileNo = this.mmobileNo; // params[4] is mobile
                String emailId = this.memailId;  // params[5] is emailid
                String fullName = this.mfullName; // params[7] is fullname
                String jobTitle = this.mjobTitle;  // params[8] is jobtitle
                String workAddress = this.mworkAddress; // params[9] is workaddress
                String workPhone = this.mworkPhone ;  // params[10] is workphone
                String homeAddress = this.mhomeAddress; // params[11] is homeaddress

                jsonParams.put("user_id", userId);
                jsonParams.put("user_name", userName);
                jsonParams.put("password", password);
                jsonParams.put("mobile_no", mobileNo);
                jsonParams.put("email_id", emailId);
                jsonParams.put("device_id", deviceId);
                jsonParams.put("full_name", fullName);
                jsonParams.put("job_title", jobTitle);
                jsonParams.put("work_address", workAddress);
                jsonParams.put("work_phone", workPhone);
                jsonParams.put("home_address", homeAddress);

                try{
                    jsonParams.put("profile_image",convertFileToString(this.mprofileImage));
                    System.out.println("convertFileToString(profile_image)" + convertFileToString(this.mprofileImage));
                }
                catch (Exception e)
                {
                    System.out.println("convertFileToString(profile_image)");
                }

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



                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }
    }

}
