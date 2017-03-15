package com.example.wooseoksong.groovejunkiesapp.HttpClient;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by wooseoksong on 2017. 3. 15..
 */

public class Connector {
    private HttpURLConnection connection;
    private URL url;
    private DataOutputStream dos;
    private InputStream is;

    public Connector(){};
    public Connector(URL url) {this.url = url;}

    public HttpURLConnection getConnection() {return connection;}
    public void setConnection(HttpURLConnection connection) {this.connection = connection;}

    public URL getUrl() {return url;}
    public void setUrl(URL url) {this.url = url;}

    public DataOutputStream getDos() {return dos;}
    public void setDos(DataOutputStream dos) {this.dos = dos;}

    public InputStream getIs() {return is;}
    public void setIs(InputStream is) {this.is = is;}
}
