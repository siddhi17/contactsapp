package com.example.siddhi.contactsapp;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Siddhi on 9/23/2016.
 */
public class Contact implements Serializable{

    private String mThumbnail;
    String mUserName;
    String mPass;
    String mProfileImage;
    String mMobileNo;
    String mEmailId;
    String contactId;
    String mFullName;
    String mStatus;
    String mJobTitle;
    String mWorkAddress;
    String mWorkPhone;
    String mHomeAddress;

    Boolean isSelected = false;

    public Contact() {
    }

    public Contact(String contactId,String userName,String pass,String mobileNo,String emailId,String profileImage,
                   String fullName,String jobTitle,String workAddress,String workPhone,String homeAddress) {

        this.contactId = contactId;
        this.mUserName = userName;
        this.mPass = pass;
        this.mProfileImage = profileImage;
        this.mMobileNo = mobileNo;
        this.mEmailId = emailId;
        this.mFullName = fullName;
        this.mJobTitle = jobTitle;
        this.mWorkAddress = workAddress;
        this.mWorkPhone = workPhone;
        this.mHomeAddress = homeAddress;
    }

    public String getContactId() {
        return contactId;
    }

    public String getmUserName() {
        return mUserName;
    }

    public String getmProfileImage() {
        return mProfileImage;
    }

    public String getmEmailId() {
        return mEmailId;
    }

    public String getmMobileNo() {
        return mMobileNo;
    }

    public String getmThumbnail() {
        return mThumbnail;
    }


    public void setmPass(String mPass) {
        this.mPass = mPass;
    }

    public String getmPass() {
        return mPass;
    }

    public void setmWorkPhone(String mWorkPhone) {
        this.mWorkPhone = mWorkPhone;
    }

    public void setmFullName(String mFullName) {
        this.mFullName = mFullName;
    }

    public void setmHomeAddress(String mHomeAddress) {
        this.mHomeAddress = mHomeAddress;
    }

    public void setmJobTitle(String mJobTitle) {
        this.mJobTitle = mJobTitle;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public void setmWorkAddress(String mWorkAddress) {
        this.mWorkAddress = mWorkAddress;
    }

    public String getmWorkPhone() {
        return mWorkPhone;
    }

    public String getmWorkAddress() {
        return mWorkAddress;
    }

    public String getmStatus() {
        return mStatus;
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

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public void setmProfileImage(String mProfileImage) {
        this.mProfileImage = mProfileImage;
    }

    public void setmEmailId(String mEmailId) {
        this.mEmailId = mEmailId;
    }

    public void setmMobileNo(String mMobileNo) {
        this.mMobileNo = mMobileNo;
    }

    public void setmThumbnail(String mThumbnail) {
        this.mThumbnail = mThumbnail;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public Boolean getSelected() {
        return isSelected;
    }
}
