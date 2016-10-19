package com.example.siddhi.contactsapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.siddhi.contactsapp.Adapter.ContactAdapter;
import com.example.siddhi.contactsapp.Contact;
import com.example.siddhi.contactsapp.helper.Excpetion2JSON;
import com.example.siddhi.contactsapp.helper.ServerRequest;
import com.example.siddhi.contactsapp.helper.ServiceUrl;
import com.example.siddhi.contactsapp.helper.UnexpectedServerException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yogendra singh on 06-09-2016.
 */
public class GetContactsAsyncTask extends AsyncTask<String, Void, JSONObject> {
    String api;
    String userId;
    JSONObject jsonParams;
    private List<Contact> contactList = new ArrayList<Contact>();
    public JSONArray contactListArray;
    private RecyclerView recyclerView;
    ContactGetCallBack contactGetCallBack;
    private ContactAdapter adapter;
    private static String KEY_SUCCESS1 = "Success";
    private Context mContext;
    private ProgressDialog progressDialog;
    public GetContactsAsyncTask(ContactGetCallBack contactGetCallBack, Context context, String userId) {
        this.mContext = context;
        this.userId = userId;
        this.contactGetCallBack=contactGetCallBack;
        this.progressDialog = new ProgressDialog(mContext);
    }
    public interface ContactGetCallBack {
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
            api = ServiceUrl.getBaseUrl() + ServiceUrl.getContactsUrl();;
            jsonParams = new JSONObject();

            String user_id = this.userId;

            jsonParams.put("user_id", user_id); // params[0] is userid

            ServerRequest request = new ServerRequest(api, jsonParams);
            return request.sendRequest();

        } catch (JSONException je) {
            return Excpetion2JSON.getJSON(je);
        }
    }  //end of doInBackground

    @Override
    protected void onPostExecute(JSONObject response) {
        super.onPostExecute(response);

        if (response.has("message"))
        {
            try {
                if( response.getString("message").equalsIgnoreCase(KEY_SUCCESS1)){
                    Toast.makeText(mContext,"success", Toast.LENGTH_LONG).show();

                     contactListArray = response.getJSONArray("contacts");
                    contactGetCallBack.doPostExecute(contactListArray);

                    progressDialog.dismiss();
                }
                else{
                    Toast.makeText(mContext, "You do not have any contacts yet.", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();

            }

        }
    }

    //end of onPostExecute
}