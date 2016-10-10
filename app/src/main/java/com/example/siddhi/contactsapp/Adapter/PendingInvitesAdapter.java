package com.example.siddhi.contactsapp.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.siddhi.contactsapp.AsyncTasks.CreateLinkedContactAsyncTask;
import com.example.siddhi.contactsapp.AsyncTasks.DeleteInvitationAsyncTask;
import com.example.siddhi.contactsapp.AsyncTasks.UpdateUserAsyncTask;
import com.example.siddhi.contactsapp.AsyncTasks.updateInvitationAsyncTask;
import com.example.siddhi.contactsapp.Invitation;
import com.example.siddhi.contactsapp.R;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Siddhi on 10/6/2016.
 */
public class PendingInvitesAdapter extends RecyclerView.Adapter<PendingInvitesAdapter.MyViewHolder>{

    private Context mContext;
    public ArrayList<Invitation> invitationArrayList = new ArrayList<>();
    private String mUserId;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        private Button accept,reject;
        private CircleImageView imageView;
        private SharedPreferences sharedpreferences;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.title);
            accept = (Button) view.findViewById(R.id.accept);
            reject = (Button) view.findViewById(R.id.reject);
            imageView = (CircleImageView) view.findViewById(R.id.thumbnail);

            sharedpreferences = mContext.getSharedPreferences("UserId", Context.MODE_PRIVATE);

            mUserId = sharedpreferences.getString("userId", "");
        }
    }

    public PendingInvitesAdapter(Context context, ArrayList<Invitation> invitationArrayList) {
        this.invitationArrayList = invitationArrayList;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pending_invite_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Invitation invitation = invitationArrayList.get(holder.getAdapterPosition());
        holder.name.setText(invitation.getUser_name());

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CreateLinkedContactAsyncTask createLinkedContactAsyncTask = new CreateLinkedContactAsyncTask(mContext,mUserId,invitation.getContact_id());
                createLinkedContactAsyncTask.execute(mUserId,invitation.getContact_id());

                HashMap<String, String> params = new HashMap<String, String>();

                params.put("invitation_id", invitation.getInvitation_id());
                params.put("status","1");

                updateInvitationAsyncTask updateInvitationAsyncTask = new updateInvitationAsyncTask();
                updateInvitationAsyncTask.execute(params);

                invitationArrayList.remove(holder.getAdapterPosition());
                notifyDataSetChanged();

            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DeleteInvitationAsyncTask deleteInvitationAsyncTask = new DeleteInvitationAsyncTask(mContext,invitation.getInvitation_id());
                deleteInvitationAsyncTask.execute(invitation.getContact_id());

                invitationArrayList.remove(holder.getAdapterPosition());
                notifyDataSetChanged();

            }
        });

    }

    @Override
    public int getItemCount() {
        return invitationArrayList.size();
    }

}


