package com.example.siddhi.contactsapp.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.siddhi.contactsapp.Contact;
import com.example.siddhi.contactsapp.Invitation;
import com.example.siddhi.contactsapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Siddhi on 10/3/2016.
 */
public class InviteAdapter extends RecyclerView.Adapter<InviteAdapter.MyViewHolder> {

    private ArrayList<Contact> contactArrayList;
    private Context mContext;
    public ArrayList<Invitation>  invitationArrayList = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        private CheckBox checkBox;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.textContactName);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);

        }
    }

    public InviteAdapter(Context context, ArrayList<Contact> contactArrayList) {
        this.contactArrayList = contactArrayList;
        this.mContext = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.invite_contact_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Contact contact = contactArrayList.get(holder.getAdapterPosition());
        holder.name.setText(contact.getmFullName());

        holder.checkBox.setChecked(contact.getSelected());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            /*    if(b)
                {
                    invite(contact);

                    Log.e("inviteList",String.valueOf(invitationArrayList.size()));
                }
                else {

                    contactArrayList.get(position).setSelected(false);
                 //   holder.checkBox.setChecked(false);
                  // updateInvites();
                    Log.e("inviteList",String.valueOf(invitationArrayList.size()));
                }*/
                contactArrayList.get(holder.getAdapterPosition()).setSelected(b);
                Log.e("inviteList",String.valueOf(invitationArrayList.size()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactArrayList.size();

    }
    public void setChecked()
    {
        for( Contact contact : contactArrayList ) {


        }
    }


    public void toggleContactsSelection( boolean isSelected ) {
        for( Contact contact : contactArrayList ) {
            contact.setSelected(isSelected);

                invite(contact);

        }
        notifyDataSetChanged(); // OR you can use notifyItemRangeChanged - which ever suits your needs
    }

    public void invite(Contact contact)
    {

        Invitation invitation = new Invitation();

            SharedPreferences sharedpreferences = mContext.getSharedPreferences("UserId", Context.MODE_PRIVATE);

            String mUserId = sharedpreferences.getString("userId", "");

            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm", Locale.ENGLISH);
            String date = df.format(Calendar.getInstance().getTime());

            invitation.setSender_id(mUserId);
            invitation.setDate(date);
            invitation.setInvitee_no(contact.getmMobileNo());
            invitation.setStatus("0");
            invitation.setUser_name(contact.getmFullName());
            invitation.setContact_id(contact.getContactId());

            invitationArrayList.add(invitation);

    }

    public void removeInvite(Contact contact,boolean isSelected)
    {

        if(!isSelected) {

            invitationArrayList.remove(contact);
            notifyDataSetChanged();
        }
    }

    public ArrayList<Invitation> getArrayList(){
        return invitationArrayList;
    }


    public void updateInvites(){
        invitationArrayList.clear();
        for(Contact contact : contactArrayList){
            if(contact.getSelected()){

                invite(contact);
            }
        }
    }
}
