package com.example.siddhi.contactsapp;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Siddhi on 9/22/2016.
 */
public class User implements Serializable{
    String mUserName;
    String mProfileImage;
    String mMobileNo;
    String mEmailId;
    String mFullName;
    String mDeviceId;
    String mPassword;
    String mUserId;

    File mProfileImageFile;
    String mJobTitle;
    String mWorkAddress;
    String mWorkPhone;
    String mHomeAddress;

    public User(String userId, String userName, String password, String profileImage, String mobileNo, String emailId, String deviceId, String fullName) {
        this.mUserId = userId;
        this.mUserName = userName;
        this.mPassword = password;
        this.mProfileImage = profileImage;
        this.mMobileNo = mobileNo;
        this.mEmailId = emailId;
        this.mDeviceId = deviceId;
        this.mFullName = fullName;
    }

    public User(String userId,String userName,String password,String mobileNo,String emailId,File profileImage,String fullName,
                String deviceId,String jobTitle,String homeAddress,String workAddress,String workPhone)
    {
        this.mUserId = userId;
        this.mUserName = userName;
        this.mPassword = password;
        this.mProfileImageFile = profileImage;
        this.mMobileNo = mobileNo;
        this.mEmailId = emailId;
        this.mDeviceId = deviceId;
        this.mFullName = fullName;
        this.mJobTitle = jobTitle;
        this.mHomeAddress = homeAddress;
        this.mWorkPhone = workPhone;
        this.mWorkAddress = workAddress;

    }

    public User(String userId,String userName,String password,String mobileNo,String emailId,String profileImage,String fullName,
                String deviceId,String jobTitle,String homeAddress,String workAddress,String workPhone)
    {
        this.mUserId = userId;
        this.mUserName = userName;
        this.mPassword = password;
        this.mProfileImage = profileImage;
        this.mMobileNo = mobileNo;
        this.mEmailId = emailId;
        this.mDeviceId = deviceId;
        this.mFullName = fullName;
        this.mJobTitle = jobTitle;
        this.mHomeAddress = homeAddress;
        this.mWorkPhone = workPhone;
        this.mWorkAddress = workAddress;

    }

    public User(){}

    public void setmDeviceId(String mDeviceId) {
        this.mDeviceId = mDeviceId;
    }

    public void setmEmailId(String mEmailId) {
        this.mEmailId = mEmailId;
    }

    public void setmFullName(String mFullName) {
        this.mFullName = mFullName;
    }

    public void setmJobTitle(String mJobTitle) {
        this.mJobTitle = mJobTitle;
    }

    public void setmMobileNo(String mMobileNo) {
        this.mMobileNo = mMobileNo;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public void setmHomeAddress(String mHomeAddress) {
        this.mHomeAddress = mHomeAddress;
    }

    public void setmProfileImage(String mProfileImage) {
        this.mProfileImage = mProfileImage;
    }

    public void setmProfileImageFile(File mProfileImageFile) {
        this.mProfileImageFile = mProfileImageFile;
    }


    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public void setmWorkAddress(String mWorkAddress) {
        this.mWorkAddress = mWorkAddress;
    }

    public void setmWorkPhone(String mWorkPhone) {
        this.mWorkPhone = mWorkPhone;
    }

    public File getmProfileImageFile() {
        return mProfileImageFile;
    }

    public String getmDeviceId() {
        return mDeviceId;
    }

    public String getmEmailId() {
        return mEmailId;
    }

    public String getmFullName() {
        return mFullName;
    }

    public String getmHomeAddress() {
        return mHomeAddress;
    }

    public String getmJobTitle() {
        return mJobTitle;
    }

    public String getmMobileNo() {
        return mMobileNo;
    }

    public String getmPassword() {
        return mPassword;
    }

    public String getmProfileImage() {
        return mProfileImage;
    }

    public String getmUserId() {
        return mUserId;
    }

    public String getmUserName() {
        return mUserName;
    }

    public String getmWorkAddress() {
        return mWorkAddress;
    }

    public String getmWorkPhone() {
        return mWorkPhone;
    }

}

