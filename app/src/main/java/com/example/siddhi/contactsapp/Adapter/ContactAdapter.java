package com.example.siddhi.contactsapp.Adapter;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.siddhi.contactsapp.Activities.DetailViewActivity;
import com.example.siddhi.contactsapp.Contact;
import com.example.siddhi.contactsapp.R;
import com.example.siddhi.contactsapp.helper.ServiceUrl;
import com.example.siddhi.contactsapp.helper.SquareImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Yogendra singh on 31-08-2016.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {


    private List<Contact> contactList;
    File myDir1;
    private Activity mContext;

    private Boolean fileExists;
    private File file;
    private static final int MY_PERMISSIONS_REQUEST_CALL= 20;

    public ContactAdapter(Activity context, List<Contact> contactList) {
        this.contactList = contactList;
        this.mContext = context;
    }

    @Override
    public ContactHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout,null);
        ContactHolder mh = new ContactHolder(v);

        return mh;
    }

    @Override
    public void onBindViewHolder(final ContactHolder contactHolder, int i) {

        final Contact contact = contactList.get(i);
        //  Log.e("Imagename",""+"http://xesoftwares.co.in/contactsapi/profile_images/85368a5bbd6cffba8a3aa202a80563a2.jpg");//+feedItem.getThumbnail());

        Target target = new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                // your code here ...
                contactHolder.thumbnail.setImageBitmap(bitmap);

                Log.e("ProfileImage", contact.getmProfileImage());

                SaveImages(bitmap, contact.getmProfileImage());

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                contactHolder.thumbnail.setImageDrawable(errorDrawable);
                // do error handling as required
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                contactHolder.thumbnail.setImageDrawable(placeHolderDrawable);
            }
        };

        contactHolder.thumbnail.setTag(target);

        String url = ServiceUrl.getBaseUrl() + ServiceUrl.getImageUserUrl() + contact.getmProfileImage();

        Log.e("url",url);

        if(contact.getmProfileImage().equals(""))

        {

            file = new File("");

            fileExists = file.exists();

            contactHolder.thumbnail.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_account_circle_black_48dp));
        }
        else {

            file = new File(Environment.getExternalStorageDirectory() + "/ContactProfileImages/" + contact.getmProfileImage());

            fileExists = file.exists();
        }

        if(fileExists)
        {

            Log.e("fileExists",file.getAbsolutePath());

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), bmOptions);
            contactHolder.thumbnail.setImageBitmap(bitmap);

        }

        else {

            Log.e("Picasso",file.getAbsolutePath());

            Picasso.with(mContext).load(url)
                    .error(R.drawable.ic_account_circle_black_24dp)
                    .placeholder(R.drawable.ic_account_circle_black_24dp)
                    .into(target);
        }

        contactHolder.title.setText(contact.getmUserName());
        //feedListRowHolder.genre.setText(Html.fromHtml(feedItem.getGenre()));

    }

    @Override
    public int getItemCount() {
        return (null != contactList ? contactList.size() : 0);
    }

    public void SaveImages(Bitmap bitmap,String profileName)
    {

        try {
            String root = Environment.getExternalStorageDirectory().getPath();
            File myDir = new File(root +"/ContactProfileImages");

            if (!myDir.exists()) {
                myDir.mkdirs();
            }

            // String name = new Date().toString();=
            String name = profileName;
            File myDir1 = new File(myDir, name);
            if(!myDir1.exists()) {
                FileOutputStream out = new FileOutputStream(myDir1);
                bitmap.compress(Bitmap.CompressFormat.PNG,100,out);

                out.flush();
                out.close();
            }


        } catch(Exception e){
            // some action
        }

        //myDir1= imageFilePath1.getprofile();

    }


    public class ContactHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected CircleImageView thumbnail;
        protected TextView title;

        public ContactHolder(View view) {
            super(view);
            this.thumbnail = (CircleImageView) view.findViewById(R.id.thumbnail);
            this.title = (TextView) view.findViewById(R.id.title);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            final Contact contact = contactList.get(getAdapterPosition());

            final Dialog dialog = new Dialog(mContext);
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
                image.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.profile_icon));
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
                    mContext.startActivity(sms_intent);
                    dialog.dismiss();

                }
            });

            image1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (ContextCompat.checkSelfPermission(mContext,
                            Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(mContext,
                                new String[]{Manifest.permission.CALL_PHONE},
                                MY_PERMISSIONS_REQUEST_CALL);

                    }
                    else {

                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact.getmMobileNo()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        mContext.startActivity(intent);
                        dialog.dismiss();
                    }

                }
            });

            image3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{contact.getmEmailId()});
                    email.setType("message/rfc822");
                    mContext.startActivity(Intent.createChooser(email, "Choose an Email client :"));

                }
            });

            Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
            // if button is clicked, view all information custom dialog

            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(mContext,DetailViewActivity.class);
                    intent.putExtra("contact",contact);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mContext.startActivity(intent);

                }
            });
            dialog.show();

        }
    }
}