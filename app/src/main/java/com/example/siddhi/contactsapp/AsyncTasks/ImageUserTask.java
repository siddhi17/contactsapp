package com.example.siddhi.contactsapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.siddhi.contactsapp.Activities.MainActivity;
import com.example.siddhi.contactsapp.Activities.ProfileActivity;
import com.example.siddhi.contactsapp.User;
import com.example.siddhi.contactsapp.database.DatabaseHelper;
import com.example.siddhi.contactsapp.database.UserTableHelper;
import com.example.siddhi.contactsapp.helper.ImageServer;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by Yogendra singh on 12-09-2016.
 */
public class ImageUserTask extends AsyncTask<Void,Void,Bitmap> {
    String strURL, imageprofile;
    Bitmap mBitmap = null;
    Context mContext;
    private File profileFile;
    private Boolean mProfile;
    private ImageServerCallBack imageServerCallBack;

    public interface ImageServerCallBack {
        void doPostExecute(Bitmap bitmap) throws JSONException;
    }

    public ImageUserTask(Context context, String url,ImageServerCallBack imageServerCallBack) {
        this.strURL = url;
        this.imageprofile = imageprofile;
        this.mContext = context;
        this.imageServerCallBack = imageServerCallBack;
    }
    public ImageUserTask(Context context, String url) {
        this.strURL = url;
        this.imageprofile = imageprofile;
        this.mContext = context;

    }
    @Override
    protected Bitmap doInBackground(Void... params) {

        try {

        /*    URL url = new URL(strURL);
            InputStream in = url.openConnection().getInputStream();
            BufferedInputStream bis = new BufferedInputStream(in,1024*8);
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            int len=0;
            byte[] buffer = new byte[1024];
            while((len = bis.read(buffer)) != -1){
                out.write(buffer, 0, len);
            }
            out.close();
            bis.close();

            byte[] data = out.toByteArray();
            mBitmap = BitmapFactory.decodeByteArray(data,0, data.length);*/

            URL urlConnection = new URL(strURL);
            //Conntecting httpUrlConnection
            HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
            connection.setDoInput(true);
            //Connected to server
            connection.connect();
            //downloading  image
            InputStream input = connection.getInputStream();
            //converting image to bitmap
            mBitmap = BitmapFactory.decodeStream(input);


            storeImage(mBitmap);

          // imageView.setImageBitmap(bitmap);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return mBitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);

        if (result != null) {
            try {
                if(imageServerCallBack != null)
                imageServerCallBack.doPostExecute(result);
            }
            catch (JSONException exception)
            {}
        }
    }private void storeImage(Bitmap image) {

        if(image != null) {

            image = Bitmap.createScaledBitmap(image,512,512, true);

            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/Profile");
            if (!myDir.exists()) {
                myDir.mkdirs();
            }
            SharedPreferences sharedpreferences = mContext.getSharedPreferences("UserProfile", Context.MODE_PRIVATE);

            String userId = sharedpreferences.getString("userId", "");
            String fname = "Profile_Image" + ".png";

            File file = new File(myDir, fname);
            Log.i("file", "" + file);

        //    UserTableHelper db = new UserTableHelper(mContext);

        //    db.updateProfileImage(userId,fname);

            try {
                FileOutputStream out = new FileOutputStream(file);
                image.compress(Bitmap.CompressFormat.PNG,100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    private  File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + mContext.getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="Profile_Image" +".png";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }
}