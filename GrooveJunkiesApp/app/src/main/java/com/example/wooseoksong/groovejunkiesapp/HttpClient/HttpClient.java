package com.example.wooseoksong.groovejunkiesapp.HttpClient;
import android.util.Log;


import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


/**
 * Created by wooseoksong on 2017. 3. 4..
 */

public class HttpClient {
    private String serverUrl;


    private String crlf;
    private String twoHyphens;
    private String boundary;
    private int maxBufferSize;
    private Connector conn;

    //getter setter
    public String getServerUrl() {return serverUrl;}
    public void setServerUrl(String serverUrl) {this.serverUrl = serverUrl;}


    public HttpClient(){}
    public HttpClient(String serverUrl){
        this.serverUrl = serverUrl;

        this.crlf = "\r\n";
        this. twoHyphens = "--";
        this.boundary =  "*****";
        this.maxBufferSize = 1* 1024*1024;
        this.conn = new Connector();
    }

    private JSONObject getFromServer(HttpURLConnection urlConn){
        try{

            conn.setIs(urlConn.getInputStream());
            InputStream is = conn.getIs();

            ByteArrayOutputStream byteArrayOutputStream =
                    new ByteArrayOutputStream();

            byte[] byteBuffer = new byte[1024];
            byte[] byteData = null;
            int nLength = 0;

            while((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1){
                byteArrayOutputStream.write(byteBuffer, 0, nLength);
            }

            byteData = byteArrayOutputStream.toByteArray();
            String result = new String(byteData);

            JSONObject jsonObj = new JSONObject(result);
            Log.d("result json Obj",jsonObj.toString());
            return jsonObj;
        }catch(Exception e){e.printStackTrace();}

        return null;
    }
    private HttpURLConnection connectionInitialize(){
        try{
            Log.d("url",serverUrl);
            conn.setUrl(new URL(serverUrl));
            URL connectURL = conn.getUrl();

            conn.setConnection((HttpURLConnection)connectURL.openConnection());
            HttpURLConnection urlConn = conn.getConnection();

            urlConn.setConnectTimeout(5000);
            urlConn.setReadTimeout(15000);

            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);
            urlConn.setUseCaches(false);

            urlConn.setRequestMethod("POST");
            urlConn.setRequestProperty("Connection", "Keep-Alive");
            return urlConn;
        }catch(Exception e){e.printStackTrace();}

        return null;
    }

    public JSONObject loginAction(String email, String password){
        Log.d("email",email);
        Log.d("password",password);
        try{
            HttpURLConnection urlConn = connectionInitialize();
            urlConn.setRequestProperty("content-type", "application/x-www-form-urlencoded");

            urlConn.connect();

            conn.setDos(new DataOutputStream(conn.getConnection().getOutputStream()));
            DataOutputStream dos = conn.getDos();
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("email").append("=").append(email).append("&");
            stringBuffer.append("password").append("=").append(password);

            OutputStreamWriter writer = new OutputStreamWriter(urlConn.getOutputStream(),"UTF-8");
            writer.write(stringBuffer.toString());
            writer.flush();

            int code = urlConn.getResponseCode();

            if(code == HttpURLConnection.HTTP_OK){
                Log.d("code","Login success!");
                JSONObject jsonObj = getFromServer(urlConn);
                return jsonObj;
            }else{
                Log.d("code","Login fail! code is "+Integer.toString(code));
            }


        }catch(Exception e){e.printStackTrace();}

        return null;
    }

    public JSONObject checkId(String email){

        try{
            HttpURLConnection urlConn = connectionInitialize();
            urlConn.setRequestProperty("content-type", "application/x-www-form-urlencoded");

            urlConn.connect();

            conn.setDos(new DataOutputStream(conn.getConnection().getOutputStream()));
            DataOutputStream dos = conn.getDos();
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("email").append("=").append(email);


            OutputStreamWriter writer = new OutputStreamWriter(urlConn.getOutputStream(),"UTF-8");
            writer.write(stringBuffer.toString());
            writer.flush();

            int code = urlConn.getResponseCode();

            if(code == HttpURLConnection.HTTP_OK){
                Log.d("result","success!");

                JSONObject jsonObj = getFromServer(urlConn);

                return jsonObj;

            }else{
                Log.d("result","fail! code is "+Integer.toString(code));
            }


        }catch(Exception e){e.printStackTrace();}


        return null;
    }


    public JSONObject sendRegisterInfo(
            String email,
            String password,
            String name,
            String imagePath
    ){
        String fileName = imagePath.substring(imagePath.lastIndexOf('/')+1, imagePath.length());
        String fileType = fileName.substring(fileName.lastIndexOf('.'), fileName.length());
        String userName = email.substring(0, email.indexOf('@'));
        String attchName = userName + fileType;
        Log.d("fileName",fileName);
        Log.d("serverUrl",serverUrl);
        Log.d("name", name);

        try{

            HttpURLConnection urlConn = connectionInitialize();
            urlConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            FileInputStream mFileInputStream = new FileInputStream(imagePath);
            urlConn.connect();

            conn.setDos(new DataOutputStream(conn.getConnection().getOutputStream()));
            DataOutputStream dos = conn.getDos();


            dos.writeBytes("\r\n--" + boundary + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"email\"\r\n\r\n" + email);
            dos.writeBytes("\r\n--" + boundary + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"password\"\r\n\r\n" + password);
            dos.writeBytes("\r\n--" + boundary + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"name\"\r\n\r\n" + URLEncoder.encode(name,"utf-8"));
            dos.writeBytes("\r\n--" + boundary + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"fileType\"\r\n\r\n" + fileType);
            dos.writeBytes("\r\n--" + boundary + "\r\n");

            dos.writeBytes(twoHyphens + boundary + crlf);
            dos.writeBytes("Content-Disposition: form-data; " +
                    "name=\"picture\";" +
                    "filename=\"" + attchName + "\"" + crlf);
            dos.writeBytes(crlf);

            int bytesAvailable = mFileInputStream.available();
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];
            int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);

            while(bytesRead > 0){
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = mFileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
            }

            dos.writeBytes(crlf);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + crlf);
            dos.flush();

            dos.close();
            int code = urlConn.getResponseCode();
            if(code == HttpURLConnection.HTTP_OK){
                JSONObject jsonObj = getFromServer(urlConn);
                return jsonObj;
            }else{
                Log.d("result","fail! code is "+Integer.toString(code));
                return null;
            }

        }catch(Exception e){e.printStackTrace();}

        return null;
    }
}
