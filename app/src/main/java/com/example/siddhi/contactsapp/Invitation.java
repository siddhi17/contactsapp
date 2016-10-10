package com.example.siddhi.contactsapp;

/**
 * Created by Siddhi on 9/12/2016.
 */
public class Invitation {

    private String sender_id,date,invitee_no,status,user_name,profile_image,invitation_id,contact_id;

    public Invitation(){}

    public Invitation(String sender_id, String date, String invitee_no, String status, String user_name,String invitation_id,String profile_image,String contact_id)
    {
        this.sender_id = sender_id;
        this.date = date;
        this.invitee_no = invitee_no;
        this.status = status;
        this.user_name = user_name;
        this.invitation_id = invitation_id;
        this.profile_image = profile_image;
        this.contact_id = contact_id;
    }

    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }

    public String getContact_id() {
        return contact_id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setInvitee_no(String invitee_no) {
        this.invitee_no = invitee_no;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSender_id() {
        return sender_id;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public String getInvitee_no() {
        return invitee_no;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public void setInvitation_id(String invitation_id) {
        this.invitation_id = invitation_id;
    }

    public String getInvitation_id() {
        return invitation_id;
    }
}
