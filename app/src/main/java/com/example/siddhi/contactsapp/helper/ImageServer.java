package com.example.siddhi.contactsapp.helper;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Yogendra singh on 06-09-2016.
 */
public class ImageServer  {

    private String directoryName = "/Profile";
    private String fileName = "imageprofile";
    private Context mcontext;
    private boolean external;

    public ImageServer(Context context) {
        this.mcontext = context;
    }

    public ImageServer setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public ImageServer setExternal(boolean external) {
        this.external = external;
        return this;
    }

    public ImageServer setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
        return this;
    }

    public String save(Bitmap bitmapImage) {


        File directory = new File(Environment.getExternalStorageDirectory().getPath());
        if ( ! directory.exists()) {
            directory.mkdirs();
        }
        File mypath = new File(directory,"ProfileImage");

        //class used for get and set ImagePath value
        // ImageFilePath imageFilePath=new ImageFilePath(mypath);
        // ComplexPreferences complexPreferences12 = ComplexPreferences.getComplexPreferences(mcontext, "mypref12", Context.MODE_PRIVATE);
        //  complexPreferences12.putObject("imageFilePath", imageFilePath);
        Log.e("user2", "" + mypath);
        //   complexPreferences12.commit();
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    @NonNull
    private File createFile() {
        File directory;
        if(external){
            directory = getAlbumStorageDir(directoryName);
        }
        else {
            directory = mcontext.getDir(directoryName, Context.MODE_PRIVATE);
        }

        return new File(directory, fileName);
    }

    private File getAlbumStorageDir(String albumName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("ImageServer", "Directory not created");
        }
        return file;
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }


    public Bitmap load() {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(createFile());
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}