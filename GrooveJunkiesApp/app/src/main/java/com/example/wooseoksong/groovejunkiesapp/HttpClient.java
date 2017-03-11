package com.example.wooseoksong.groovejunkiesapp;
import android.content.Context;
import android.util.Log;



import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by wooseoksong on 2017. 3. 4..
 */

public class HttpClient {
    private URL url;
    private HttpURLConnection connection;
    private String serverUrl;


    private String crlf;
    private String twoHyphens;
    private String boundary;
    private int maxBufferSize;

    //getter setter
    public String getServerUrl() {return serverUrl;}
    public void setServerUrl(String serverUrl) {this.serverUrl = serverUrl;}


    public HttpClient(){

    }
    public HttpClient(String serverUrl){
        this.serverUrl = serverUrl;

        this.crlf = "\r\n";
        this. twoHyphens = "--";
        this.boundary =  "*****";
        this.maxBufferSize = 1* 1024*1024;
    }

    public int sendRegisterInfo(){

        try{
            url = new URL(serverUrl);
            connection = (HttpURLConnection)url.openConnection();

            connection.setDefaultUseCaches(false);
            connection.setDoInput(true);                 // 서버에서 읽기 모드 지정
            connection.setDoOutput(true);                // 서버로 쓰기 모드 지정
            connection.setRequestMethod("POST");         // 전송 방식은 POST
            Log.d("connection-test","success url : "+serverUrl);

            connection.setRequestProperty("content-type", "application/x-www-form-urlencoded");

            StringBuffer buffer = new StringBuffer();
            buffer.append("email").append("=").append("22").append("&");                 // php 변수에 값 대입
            buffer.append("password").append("=").append("ss").append("&");   // php 변수 앞에 '$' 붙이지 않는다
            buffer.append("stageName").append("=").append("kk").append("&");           // 변수 구분은 '&' 사용
            buffer.append("subject").append("=").append("ll");

            OutputStreamWriter outStream = new OutputStreamWriter(connection.getOutputStream(), "EUC-KR");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(buffer.toString());
            writer.flush();

            InputStreamReader tmp = new InputStreamReader(connection.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다
                builder.append(str + "\n");                     // View에 표시하기 위해 라인 구분자 추가
            }
            Log.d("result",builder.toString());
            return 0;


        }catch(Exception e){e.printStackTrace();}


        return -1;
    }

    public int sendImageFile(
            String email,
            String password,
            String name,
            String imagePath,
            Context mContext
    ){
        String fileName = imagePath.substring(imagePath.lastIndexOf('/')+1, imagePath.length());
        String fileType = fileName.substring(fileName.lastIndexOf('.'), fileName.length());
        String userName = email.substring(0, email.indexOf('@'));
        String attchName = userName + fileType;
        Log.d("fileName",fileName);
        Log.d("serverUrl",serverUrl);


        try{
            URL connectURL = new URL(serverUrl);
            FileInputStream mFileInputStream = new FileInputStream(imagePath);

            HttpURLConnection conn = (HttpURLConnection)connectURL.openConnection();

            conn.setConnectTimeout(5000);
            conn.setReadTimeout(15000);

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
//            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
//            conn.setRequestProperty("cache-control", "no-cache");
//            conn.setRequestProperty("cache-length", "length");
//            conn.setRequestProperty("picture", imagePath);
//            conn.setRequestProperty("user-agent", "test");

            conn.connect();

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes("\r\n--" + boundary + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"email\"\r\n\r\n" + email);
            dos.writeBytes("\r\n--" + boundary + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"password\"\r\n\r\n" + password);
            dos.writeBytes("\r\n--" + boundary + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"name\"\r\n\r\n" + name);
            dos.writeBytes("\r\n--" + boundary + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"fileType\"\r\n\r\n" + fileType);
            dos.writeBytes("\r\n--" + boundary + "\r\n");

            dos.writeBytes(twoHyphens + boundary + crlf);
            dos.writeBytes("Content-Disposition: form-data; " +
                    "name=\"picture\";" +
                    "filename=\"" + attchName + "\"" + crlf);
            dos.writeBytes(crlf);

            int bytesAvailable = mFileInputStream.available();
            Log.d("bytesAvailable",Integer.toString(bytesAvailable));
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            Log.d("bufferSize",Integer.toString(bufferSize));
            byte[] buffer = new byte[bufferSize];
            int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
            Log.d("bytesRead",Integer.toString(bytesRead));

            while(bytesRead > 0){
                Log.d("test","test");
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = mFileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
            }

            dos.writeBytes(crlf);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + crlf);
            dos.flush();

            dos.close();
            int code = conn.getResponseCode();
            if(code == HttpURLConnection.HTTP_OK){
                Log.d("result","success!");
            }else{
                Log.d("result","fail! code is "+Integer.toString(code));
            }

        }catch(Exception e){e.printStackTrace();}




        return -1;
//        //builder의 셋팅을 하는 부분
//
//        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//
//        //file과 parameter들을 한꺼번에 builder에 포함해서 보낸다
//        builder.addTextBody("email", email, ContentType.create("Multipart/related","UTF-8"));
//        builder.addTextBody("password", password, ContentType.create("Multipart/related","UTF-8"));
//        builder.addTextBody("stageName", stageName, ContentType.create("Multipart/related","UTF-8"));
//        builder.addPart("userPhoto", new FileBody(new File(imagePath)));
//
//        //send Request!!
//        InputStream inputStream = null;
//        try{
//
//            org.apache.http.client.HttpClient httpClient =
//                    AndroidHttpClient.newInstance("Android");
//            HttpPost httpPost = new HttpPost(serverUrl);
//
//            httpPost.setEntity(builder.build());
//
//
//            HttpResponse httpResponse = httpClient.execute(httpPost);
//            HttpEntity httpEntity = httpResponse.getEntity();
//            inputStream = httpEntity.getContent();
//
//
//            //response
//            BufferedReader bufferdReader =
//                    new BufferedReader(new InputStreamReader((inputStream), "UTF-8"));
//            StringBuilder stringBuilder = new StringBuilder();
//            String line = null;
//
//            while((line = bufferdReader.readLine()) != null){
//                stringBuilder.append(line+"\n");
//            }
//
//            inputStream.close();
//            return 0;
//
//        }catch(Exception e){e.printStackTrace();}
//        return -1;
    }
}
