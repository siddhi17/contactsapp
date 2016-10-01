package com.example.siddhi.contactsapp.AsyncTasks;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Yogendra singh on 09-09-2016.
 */
public class ImageFilePath implements Serializable {

    File mprofile;



    public ImageFilePath(File profile) {
        this.mprofile = profile;
    }


    public File getprofile() {
        return mprofile;
    }

    public void setprofile(File profile) {
        this.mprofile = profile;
    }


}