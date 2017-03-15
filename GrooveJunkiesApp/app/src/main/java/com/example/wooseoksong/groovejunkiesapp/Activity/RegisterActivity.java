package com.example.wooseoksong.groovejunkiesapp.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wooseoksong.groovejunkiesapp.BackgroundTask.CheckidBT;
import com.example.wooseoksong.groovejunkiesapp.BackgroundTask.RegisterBT;
import com.example.wooseoksong.groovejunkiesapp.HttpClient.HttpClient;
import com.example.wooseoksong.groovejunkiesapp.R;

import org.json.JSONObject;

/**
 * Created by wooseoksong on 2017. 3. 3..
 */

public class RegisterActivity extends AppCompatActivity {

    private static final int SELECT_PICTURE = 1;
    private String imagePath;

    private String email;
    private String passwordFirst;
    private String passwordSecond;
    private String name;

    private static final int REQUEST_EXTERNAL_STORAGE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register_layout);

        ImageButton addPictureBtn = (ImageButton)findViewById(R.id.imageButton);

        //사진 불러오기 버튼 클릭시
        addPictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
            }
        });

        Button registerBtn = (Button)findViewById(R.id.register_confirm_btn);
        final Activity thisActivity = this;

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AutoCompleteTextView emailForm =
                        (AutoCompleteTextView)findViewById(R.id.register_email);
                AutoCompleteTextView passwordFirstForm =
                        (AutoCompleteTextView)findViewById(R.id.register_password_first);
                AutoCompleteTextView passwordSecondForm =
                        (AutoCompleteTextView)findViewById(R.id.register_password_second);
                AutoCompleteTextView stageNameForm =
                        (AutoCompleteTextView)findViewById(R.id.register_name);


                email = emailForm.getText().toString();
                passwordFirst = passwordFirstForm.getText().toString();
                passwordSecond = passwordSecondForm.getText().toString();
                name = stageNameForm.getText().toString();


                if(!passwordFirst.equals(passwordSecond)){
                    Toast.makeText(v.getContext(),
                            "패스워드가 일치하지 않습니다!", Toast.LENGTH_SHORT).show();
                    return;
                }

                int permissionReadStorage =
                            ContextCompat.checkSelfPermission(v.getContext(),
                                    Manifest.permission.READ_EXTERNAL_STORAGE);
                int permissionWriteStorage = ContextCompat.checkSelfPermission(v.getContext(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if(permissionReadStorage == PackageManager.PERMISSION_DENIED ||
                            permissionWriteStorage == PackageManager.PERMISSION_DENIED) {

                    ActivityCompat.requestPermissions(thisActivity,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
                    System.out.println("im in!");
                }else{
                    System.out.println("im in permission ok!");
                    Log.d("email check!","emailCheck!");

                    CheckidBT checkidBT =
                            new CheckidBT("http://192.168.0.107:8080/appServer/checkId", email);
                    try{
                        JSONObject jsonObj = checkidBT.execute().get();
                        if(jsonObj != null && jsonObj.get("result").equals("ok")){

                            RegisterBT registerBT =
                                    new RegisterBT(
                                            "http://192.168.0.107:8080/appServer/register",
                                            email, passwordFirst, name, imagePath);
                            jsonObj = registerBT.execute().get();

                            if(jsonObj.get("result").equals("success")){
                                Intent intent = new Intent(thisActivity, LoginActivity.class);
                                intent.putExtra("result", "registerOk");
                                startActivity(intent);
                                finish();
                            }

                        }else{
                            Toast.makeText(thisActivity,
                                    "아이디가 있습니다 다른 아이디로 시도하세요.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }catch(Exception e){e.printStackTrace();}
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){

            case REQUEST_EXTERNAL_STORAGE:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];
                    if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                        if(grantResult == PackageManager.PERMISSION_GRANTED) {
                            HttpClient client = new HttpClient("register");

                            RegisterBT rbt =
                                    new RegisterBT(
                                            "http://192.168.0.107:8080/appServer/checkId",
                                            email, passwordFirst, name, imagePath);
                        } else {
                            Toast.makeText(this, "what the...f..",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }



        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if(resultCode == RESULT_OK){
            if(requestCode == SELECT_PICTURE){
                ImageView imageView = (ImageView) findViewById(R.id.user_picture_view);

                Uri selectImageUri = data.getData();
                imagePath = getImagePath(selectImageUri);
                        //selectImageUri.getPath();

                System.out.println("selectImageUri -> "+selectImageUri.getPath());


                imageView.setImageURI(selectImageUri);
                Toast.makeText(this, "사진 로드 성공!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private String getImagePath(Uri uri){
        if(uri == null){
            return null;
        }

        String projection[] = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection,null, null,null);

        if(cursor != null){
            int column_index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }

        return uri.getPath();
    }
}
