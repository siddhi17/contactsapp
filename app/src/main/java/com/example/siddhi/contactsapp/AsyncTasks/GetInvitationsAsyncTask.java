package com.example.siddhi.contactsapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import com.example.siddhi.contactsapp.Invitation;
import com.example.siddhi.contactsapp.helper.Excpetion2JSON;
import com.example.siddhi.contactsapp.helper.ServerRequest;
import com.example.siddhi.contactsapp.helper.ServiceUrl;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Siddhi on 10/6/2016.
 */
public class GetInvitationsAsyncTask  extends AsyncTask<String, Void, JSONObject> {
    String api;
    String mMobileNo,mUsername;
    JSONObject jsonParams;
    private List<Invitation> invitationList = new ArrayList<Invitation>();
    public JSONArray invitationListArray;
    GetInvitationsCallBack getInvitationsCallBack;
    private static String KEY_SUCCESS1 = "Success";
    private Context mContext;
    private ProgressDialog progressDialog;

    public GetInvitationsAsyncTask(GetInvitationsCallBack getInvitationsCallBack, Context context,String mobileno,String username) {
        this.mContext = context;
        this.getInvitationsCallBack=getInvitationsCallBack;
        this.progressDialog = new ProgressDialog(mContext);
        this.mMobileNo = mobileno;
        this.mUsername = username;
    }
    public interface GetInvitationsCallBack {
        void doPostExecute(JSONArray response) throws JSONException;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // progressDialog=new ProgressDialog(mContext);
        progressDialog.setMessage("Getting information Please wait...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    @Override
    protected JSONObject doInBackground(String...params) {
        try {
            api = ServiceUrl.getBaseUrl() + "getInvitations.php";
            jsonParams = new JSONObject();

            jsonParams.put("mobile_no",mMobileNo);
            jsonParams.put("username",mUsername);

            ServerRequest request = new ServerRequest(api, jsonParams);
            return request.sendRequest();

        } catch (JSONException je) {
            return Excpetion2JSON.getJSON(je);
        }
    }  //end of doInBackground

    @Override
    protected void onPostExecute(JSONObject response) {
        super.onPostExecute(response);

        if (response.has("message")) {
            try {
                if (response.getString("message").equalsIgnoreCase(KEY_SUCCESS1)) {
                //    Toast.makeText(mContext, "success", Toast.LENGTH_LONG).show();

                    invitationListArray = response.getJSONArray("Invitations");
                    getInvitationsCallBack.doPostExecute(invitationListArray);

                    progressDialog.dismiss();
                } else {
                    Toast.makeText(mContext, "Unable to Fetch Invitations", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }
}
