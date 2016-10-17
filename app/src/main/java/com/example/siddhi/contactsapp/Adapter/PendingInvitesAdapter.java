package com.example.siddhi.contactsapp.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.siddhi.contactsapp.AsyncTasks.CreateLinkedContactAsyncTask;
import com.example.siddhi.contactsapp.AsyncTasks.DeleteInvitationAsyncTask;
import com.example.siddhi.contactsapp.Contact;
import com.example.siddhi.contactsapp.Invitation;
import com.example.siddhi.contactsapp.R;
import com.example.siddhi.contactsapp.database.ContactTableHelper;
import com.example.siddhi.contactsapp.helper.ServiceUrl;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.util.ArrayList;

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

            sharedpreferences = mContext.getSharedPreferences("UserProfile", Context.MODE_PRIVATE);

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


        Target target = new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                holder.imageView.setImageBitmap(bitmap);

                Log.e("ProfileImage", invitation.getProfile_image());

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
              holder.imageView.setImageDrawable(errorDrawable);
                // do error handling as required
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
               holder.imageView.setImageDrawable(placeHolderDrawable);
            }
        };

        holder.imageView.setTag(target);

        String url = ServiceUrl.getBaseUrl() + ServiceUrl.getImageUserUrl() + invitation.getProfile_image();

        Log.e("url",url);

        Picasso.with(mContext).load(url)
                .error(R.drawable.ic_account_circle_black_24dp)
                .placeholder(R.drawable.ic_account_circle_black_24dp)
                .into(target);


        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CreateLinkedContactAsyncTask createLinkedContactAsyncTask = new CreateLinkedContactAsyncTask(mContext,mUserId,invitation.getSender_id());
                createLinkedContactAsyncTask.execute(mUserId,invitation.getSender_id());

                DeleteInvitationAsyncTask deleteInvitationAsyncTask = new DeleteInvitationAsyncTask(mContext,invitation.getInvitation_id());
                deleteInvitationAsyncTask.execute(invitation.getInvitation_id());

                invitationArrayList.remove(holder.getAdapterPosition());
                notifyDataSetChanged();

                Contact contact = new Contact();

                contact.setmFullName(invitation.getFullName());
                contact.setmProfileImage(invitation.getProfile_image());
                contact.setContactId(invitation.getSender_id());
                contact.setmMobileNo(invitation.getMobileNo());
                contact.setmEmailId(invitation.getEmailId());
                contact.setmHomeAddress(invitation.getHomeAddress());
                contact.setmWorkAddress(invitation.getWorkAddress());
                contact.setmWorkPhone(invitation.getWorkPhone());
                contact.setmJobTitle(invitation.getJobTitle());
                contact.setmStatus(invitation.getStatus());
                contact.setmUserName(invitation.getUser_name());

                addContact(contact);

            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new MaterialDialog.Builder(mContext)
                        .title("Confirm Rejection")
                        .positiveText("YES")
                        .content("Are you sure, you want to reject this invitation?")
                        .negativeText("NO")
                        .canceledOnTouchOutside(true)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                DeleteInvitationAsyncTask deleteInvitationAsyncTask = new DeleteInvitationAsyncTask(mContext,invitation.getInvitation_id());
                                deleteInvitationAsyncTask.execute(invitation.getInvitation_id());

                                invitationArrayList.remove(holder.getAdapterPosition());
                                notifyDataSetChanged();
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                dialog.dismiss();
                            }
                        })
                        .show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return invitationArrayList.size();
    }


    public void addContact(Contact contact)
    {

        ContactTableHelper db = new ContactTableHelper(mContext);
        db.addContact(contact);

    }


}


