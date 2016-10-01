package com.example.siddhi.contactsapp.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.siddhi.contactsapp.Contact;
import com.example.siddhi.contactsapp.R;

import java.io.File;

/**
 * Created by Siddhi on 9/30/2016.
 */
public class DetailViewActivity extends AppCompatActivity {

    private ImageView profileImage;
    private EditText edtName,edtFullName,edtMobile,edtEmailId,edtPhone,edtJobTitle,edtWorkAddress,edtHomeAddress;

    private Intent mIntent;
    private Contact mContact;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private AppBarLayout mAppBarLayout;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewcontact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        mContact = new Contact();

        mIntent = getIntent();

        mContact = (Contact)mIntent.getSerializableExtra("contact");

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
    //    mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar);

        if(mContact.getmFullName().equals("")) {
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
        toolbarTextAppernce();
//        mAppBarLayout.setExpanded(true);

        profileImage = (ImageView)findViewById(R.id.viewImage);

        edtEmailId = (EditText)findViewById(R.id.edtemail);
        edtFullName = (EditText)findViewById(R.id.edtname);
        edtName = (EditText)findViewById(R.id.edtusername);
        edtMobile = (EditText)findViewById(R.id.edtmobileno);
        edtHomeAddress = (EditText)findViewById(R.id.edthomeaddress);
        edtWorkAddress = (EditText)findViewById(R.id.edtworkaddress);
        edtJobTitle = (EditText)findViewById(R.id.edtjodtitle);
        edtPhone = (EditText)findViewById(R.id.edttelephoneno);

        edtPhone.setText(mContact.getmWorkPhone());
        edtEmailId.setText(mContact.getmEmailId());
        edtHomeAddress.setText(mContact.getmHomeAddress());
        edtFullName.setText(mContact.getmFullName());
        edtJobTitle.setText(mContact.getmJobTitle());
        edtName.setText(mContact.getmUserName());
        edtWorkAddress.setText(mContact.getmWorkAddress());
        edtMobile.setText(mContact.getmMobileNo());

        if(mContact.getmProfileImage().equals(""))

        {
            profileImage.setImageDrawable(ContextCompat.getDrawable(DetailViewActivity.this,R.drawable.profile_icon));
        }
        else {
            File file = new File(Environment.getExternalStorageDirectory() + "/ContactProfileImages/" + mContact.getmProfileImage());
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), bmOptions);

            profileImage.setImageBitmap(bitmap);
        }

    }


    private void toolbarTextAppernce() {
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
    }
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i = new Intent(DetailViewActivity.this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
