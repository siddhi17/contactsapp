package com.example.siddhi.contactsapp.helper;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Admin on 5/13/2016.
 */
public class ServerRequest {
    String api;
    JSONObject jsonParams;

    public ServerRequest(String api, JSONObject jsonParams) {
        this.api = api;
        this.jsonParams = jsonParams;
    }

    public JSONObject sendRequest() {
        try {
            URL url = new URL(api);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            con.setDoInput(true);
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write(jsonParams.toString());
            writer.close();

            int responseCode = con.getResponseCode();
            if  (responseCode == HttpURLConnection.HTTP_OK) {
                StringBuilder sb = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line = "";
                while ( (line = reader.readLine()) != null ){
                    sb.append(line);
                }
                reader.close();
                Log.d("ServerResponse", new String(sb));
                return new JSONObject(new String(sb));
            } else {
                throw new UnexpectedServerException("Unexpected server exception with status code : "+responseCode);
            }
        } catch (MalformedURLException me) {
            me.printStackTrace();
            return Excpetion2JSON.getJSON(me);
        } catch(IOException ioe) {
            ioe.printStackTrace();
            return Excpetion2JSON.getJSON(ioe);
        } catch(UnexpectedServerException ue) {
            ue.printStackTrace();
            return Excpetion2JSON.getJSON(ue);
        } catch (JSONException je) {
            je.printStackTrace();
            return Excpetion2JSON.getJSON(je);
        }
    }

    public ServerRequest(String api) {
        this.api = api;
    }


    public JSONObject sendGetRequest() {
        try {
            URL url = new URL(api);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);

            int responseCode = con.getResponseCode();
            if  (responseCode == HttpURLConnection.HTTP_OK) {
                StringBuilder sb = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line = "";
                while ( (line = reader.readLine()) != null ){
                    sb.append(line);
                }
                reader.close();
                Log.d("ServerResponse", new String(sb));
                return new JSONObject(new String(sb));
            } else {
                throw new UnexpectedServerException("Unexpected server exception with status code : "+responseCode);
            }
        } catch (MalformedURLException me) {
            me.printStackTrace();
            return Excpetion2JSON.getJSON(me);
        } catch(IOException ioe) {
            ioe.printStackTrace();
            return Excpetion2JSON.getJSON(ioe);
        } catch(UnexpectedServerException ue) {
            ue.printStackTrace();
            return Excpetion2JSON.getJSON(ue);
        } catch (JSONException je) {
            je.printStackTrace();
            return Excpetion2JSON.getJSON(je);
        }
    }
}
