package com.example.siddhi.contactsapp.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.siddhi.contactsapp.AsyncTasks.DeleteContactAsyncTask;
import com.example.siddhi.contactsapp.AsyncTasks.GetUserAsyncTask;
import com.example.siddhi.contactsapp.AsyncTasks.SendInviteAsyncTask;
import com.example.siddhi.contactsapp.Contact;
import com.example.siddhi.contactsapp.R;
import com.example.siddhi.contactsapp.database.ContactTableHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;


/**
 * Created by Siddhi on 9/30/2016.
 */
public class DetailViewActivity extends AppCompatActivity implements GetUserAsyncTask.GetUserCallBack{

    private ImageView profileImage;
    private EditText edtName,edtFullName,edtMobile,edtEmailId,edtPhone,edtJobTitle,edtWorkAddress,edtHomeAddress,edtPass;

    private Intent mIntent;
    private Contact mContact;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private CoordinatorLayout coordinatorLayout;
    final int version = android.os.Build.VERSION.SDK_INT;
    private AppBarLayout appBarLayout;
    private int width;
    private String mUserId,mLoggedInUserId;
    private Boolean mUpdateNotification= false;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private Button buttonDelete;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewcontact);

        mContact = new Contact();

        setUI();

        SharedPreferences sharedpreferences = getSharedPreferences("UserProfile", Context.MODE_PRIVATE);

        mLoggedInUserId = sharedpreferences.getString("userId", "");

        mIntent = getIntent();

        Bundle data = getIntent().getExtras();

        mUserId = data.getString("user_id");

        if(mUserId == null)
        {

            mUserId = getIntent().getStringExtra("userId");
            mUpdateNotification = getIntent().getBooleanExtra("updateNotification",false);

        }

        String temp =  data.getString("updateNotification");

        if(temp == null)
        {
            temp = "false";
        }

        if(temp.equals("true")) {
            mUpdateNotification = true;
        }
        if(!mUpdateNotification) {

            mContact = (Contact) mIntent.getSerializableExtra("contact");

            setContactDetails();
        }

        else {

            GetUserAsyncTask getUserAsyncTask = new GetUserAsyncTask(DetailViewActivity.this,DetailViewActivity.this,mUserId);
            getUserAsyncTask.execute(mUserId);

        }

    }

    public void setContactDetails()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        final Display dWidth = getWindowManager().getDefaultDisplay();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cordinator_layout);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);

        buttonDelete = (Button)findViewById(R.id.buttonDelete);

        appBarLayout.post(new Runnable() {
            @Override
            public void run() {

                if (version >= 13)
                {
                    Point size = new Point();
                    dWidth.getSize(size);
                    width = size.x;
                }
                else {
                    int heightPx = dWidth.getWidth() * 1 / 3;
                    setAppBarOffset(heightPx);
                }
            }
        });

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        ImageView toolbarImage = (ImageView) findViewById(R.id.view_image);
        toolbarImage.getLayoutParams().height = dWidth.getWidth();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.square_image);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int mutedColor = palette.getMutedColor(getResources().getColor(R.color.colorPrimary));
                collapsingToolbarLayout.setContentScrimColor(mutedColor);
            }
        });

        if(mContact.getmFullName().equals("") && mContact.getmFullName() != null) {
            collapsingToolbarLayout.setTitle("Contact");
        }
        else {

            collapsingToolbarLayout.setTitle(mContact.getmFullName());
        }
        if (toolbar != null) {


            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                    finish();

                }


            });
        }
        // toolbarTextAppernce();

        // ScrollableAppBar appBarLayout = (ScrollableAppBar) findViewById(R.id.appbar);

//To give the effect "in the middle" of the image (like gif)
        //  appBarLayout.collapseToolbar();


        if(mContact.getmWorkPhone().equals(""))
        {
            edtPhone.setText("Empty");
        }
        else {
            edtPhone.setText(mContact.getmWorkPhone());
        }

        if(mContact.getmEmailId().equals(""))
        {
            edtEmailId.setText("Empty");
        }
        else {
        edtEmailId.setText(mContact.getmEmailId());}

        if(mContact.getmHomeAddress().equals(""))
        {
            edtHomeAddress.setText("Empty");
        }
        else {
            edtHomeAddress.setText(mContact.getmHomeAddress());
        }

        if(mContact.getmFullName().equals(""))
        {
            edtFullName.setText("Empty");
        }
        else {
            edtFullName.setText(mContact.getmFullName());
        }

        if(mContact.getmJobTitle().equals(""))
        {
            edtJobTitle.setText("Empty");
        }
        else {
            edtJobTitle.setText(mContact.getmJobTitle());
        }

        edtName.setText(mContact.getmUserName());

        if(mContact.getmWorkAddress().equals(""))
        {
            edtWorkAddress.setText("Empty");
        }
        else {
            edtWorkAddress.setText(mContact.getmWorkAddress());
        }

        if(mContact.getmMobileNo().equals(""))
        {
            edtMobile.setText("Empty");
        }
        else {
            edtMobile.setText(mContact.getmMobileNo());
        }
        if(mContact.getmPass().equals(""))
        {
            edtMobile.setText("Empty");
        }
        else {
            edtPass.setText(mContact.getmPass());
        }


        if(mContact.getmProfileImage().equals("") && mContact.getmProfileImage() != null)
        {
            toolbarImage.setImageDrawable(ContextCompat.getDrawable(DetailViewActivity.this,R.drawable.profile_icon));
        }
        else {
            File file = new File(Environment.getExternalStorageDirectory() + "/ContactProfileImages/" + mContact.getmProfileImage());
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap1 = BitmapFactory.decodeFile(file.getPath(), bmOptions);
            toolbarImage.setImageBitmap(bitmap1);
        }

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, String> params = new HashMap<String, String>();

                params.put("user_id", mLoggedInUserId);
                params.put("linked_contact_id",mContact.getContactId());


                DeleteContactAsyncTask deleteContactAsyncTask = new DeleteContactAsyncTask(DetailViewActivity.this,mContact.getContactId());
                deleteContactAsyncTask.execute(params);

            }
        });


    }

   // private void toolbarTextAppernce() {
    //    collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
    //    collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
  //  }
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i = new Intent(DetailViewActivity.this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }
    private void setAppBarOffset(int offsetPx) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.onNestedPreScroll(coordinatorLayout, appBarLayout, null, 0, offsetPx, new int[]{0, 0});
    }

    public void setUI()
    {

        edtEmailId = (EditText)findViewById(R.id.edtemail);
        edtFullName = (EditText)findViewById(R.id.edtname);
        edtName = (EditText)findViewById(R.id.edtusername);
        edtMobile = (EditText)findViewById(R.id.edtmobileno);
        edtHomeAddress = (EditText)findViewById(R.id.edthomeaddress);
        edtWorkAddress = (EditText)findViewById(R.id.edtworkaddress);
        edtJobTitle = (EditText)findViewById(R.id.edtjodtitle);
        edtPhone = (EditText)findViewById(R.id.edttelephoneno);
        edtPass = (EditText)findViewById(R.id.editTextPass);

    }

    @Override
    public void doPostExecute(JSONObject response) throws JSONException {
        JSONObject userJs = response;

        String userId = userJs.getString("user_id");
        String userName = userJs.getString("user_name");
        String profileImage = userJs.getString("profile_image");
        String mobileNo = userJs.getString("mobile_no");
        String emailId = userJs.getString("email_id");
        String fullName = userJs.getString("full_name");
        String jobTitle = userJs.getString("job_title");
        String homeAddress = userJs.getString("home_address");
        String workPhone = userJs.getString("work_phone");
        String workAddress = userJs.getString("work_address");

        mContact.setmEmailId(emailId);
        mContact.setmMobileNo(mobileNo);
        mContact.setContactId(userId);
        mContact.setmProfileImage(profileImage);
        mContact.setmFullName(fullName);
        mContact.setmUserName(userName);
        mContact.setmHomeAddress(homeAddress);
        mContact.setmJobTitle(jobTitle);
        mContact.setmWorkAddress(workAddress);
        mContact.setmWorkPhone(workPhone);

        setContactDetails();

        ContactTableHelper db = new ContactTableHelper(DetailViewActivity.this);
        db.updateContact(mContact);
        db.close();

    }

}
