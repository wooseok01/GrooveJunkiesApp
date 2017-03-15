package com.example.wooseoksong.groovejunkiesapp.BackgroundTask;

import android.os.AsyncTask;

import com.example.wooseoksong.groovejunkiesapp.HttpClient.HttpClient;

import org.json.JSONObject;

/**
 * Created by wooseoksong on 2017. 3. 15..
 */

public class CheckidBT extends AsyncTask<String, Integer, JSONObject> {

    private String email;
    private HttpClient client;
    private String url;

    public CheckidBT(String url, String email) {
        this.url = url;
        this.email = email;
        this.client = new HttpClient(this.url);
    }

    public String getUrl() {return url;}
    public void setUrl(String url) {this.url = url;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject jsonObject = client.checkId(email);
        return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
    }
}
