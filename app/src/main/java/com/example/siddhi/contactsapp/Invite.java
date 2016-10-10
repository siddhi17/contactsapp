package com.example.siddhi.contactsapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Siddhi on 9/14/2016.
 */
public class Invite {

    @SerializedName("invitations")
    @Expose
    private List<Invitation> invitations = new ArrayList<Invitation>();
    public List<Invitation> getInvitations() {
        return invitations;
    }
    public void setInvitations(List<Invitation> invitations) {
        this.invitations = invitations;
    }

}