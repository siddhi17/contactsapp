package com.example.siddhi.contactsapp.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.siddhi.contactsapp.Contact;
import com.example.siddhi.contactsapp.R;
import com.example.siddhi.contactsapp.helper.ServiceUrl;
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
    private Context mContext;

    private Boolean fileExists;
    private File file;

    public ContactAdapter(Context context, List<Contact> contactList) {
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
       /* Picasso.with(mContext).load("http://xesoftwares.co.in/contactsapi/profile_images/85368a5bbd6cffba8a3aa202a80563a2.jpg")
                .error(R.drawable.ic_account_circle_black_24dp)
                .placeholder(R.drawable.ic_account_circle_black_24dp)
                .into(feedListRowHolder.thumbnail)
                ;*/

        Target target = new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                // your code here ...
                    contactHolder.thumbnail.setImageBitmap(bitmap);

                Log.e("ProfileImage",contact.getmProfileImage());

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

            contactHolder.thumbnail.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_account_circle_black_48dp));
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
                    .resize(100, 100)
                    .into(target);


        }

    //    Picasso.with(mContext).load().into(target);

   /*   Picasso.with(mContext).load(feedItem.getprofileImage())
                .error(R.drawable.ic_account_circle_black_24dp)
                .placeholder(R.drawable.ic_account_circle_black_24dp)
                .resize(100,100)
                .into(new Target() {

                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });*/

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
                bitmap.compress(Bitmap.CompressFormat.JPEG,100, out);

                out.flush();
                out.close();
            }


        } catch(Exception e){
            // some action
        }

         //myDir1= imageFilePath1.getprofile();

    }


    public class ContactHolder extends RecyclerView.ViewHolder {
        protected CircleImageView thumbnail;
        protected TextView title;

        public ContactHolder(View view) {
            super(view);
            this.thumbnail = (CircleImageView) view.findViewById(R.id.thumbnail);
            this.title = (TextView) view.findViewById(R.id.title);


        }

    }
}
