package com.example.anumehaagrawal.facerec;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;

import android.content.Intent;

import android.content.pm.PackageManager;

import android.provider.MediaStore;

import android.support.annotation.NonNull;

import android.support.v4.app.ActivityCompat;

import android.support.v4.content.ContextCompat;

import android.app.Activity;

import android.view.View;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;

import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kairos.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


import java.io.UnsupportedEncodingException;



public class MainActivity extends Activity {

    private TextView info;
    private Button verify, enroll;
    private KairosListener enrolllistener, verifylistener;
    private Kairos kairos;
    private String multipleFaces, galleryID, selector, subject;
    private static final int RC_CAMERA_PERMISSION = 100;
    private static final int RC_CAMERA_ENROLL = 101;
    private static final int RC_CAMERA_VERIFY = 102;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kairos = new Kairos();
        kairos.setAuthentication(this, getString(R.string.app_id), getString(R.string.api_key));

        verify = (Button) findViewById(R.id.verify);
        enroll = (Button) findViewById(R.id.enroll);
        info = (TextView) findViewById(R.id.info);

        multipleFaces = "false";
        galleryID = "ChangePay";
        selector = "FULL";
        subject = "Human";

        enrolllistener = new KairosListener() {
            @Override
            public void onSuccess(String s) {
                try {
                    JSONObject response = new JSONObject(s);
                    if(response.getJSONArray("images").getJSONObject(0).getJSONObject("transaction").getString("status").equals("success")) {
                        info.setText("Enrolled Successfully");
                    } else {
                        info.setText("Something went wrong!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFail(String s) {
                info.setText("An error occurred");
            }
        };

        verifylistener = new KairosListener() {
            @Override
            public void onSuccess(String s) {
                try {
                    JSONObject response = new JSONObject(s);
                    if (response.getJSONArray("images").getJSONObject(0).getJSONObject("transaction").getString("status").equals("success")) {
                        JSONArray array = response.getJSONArray("images").getJSONObject(0).getJSONArray("candidates");
                        if(array != null && array.length() > 0) {
                            info.setText("Matched!");
                        }
                    } else {
                        info.setText("Something went wrong!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String s) {
                info.setText("An error occurred");
            }
        };

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    RC_CAMERA_PERMISSION);
        }

        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, RC_CAMERA_ENROLL);
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, RC_CAMERA_VERIFY);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RC_CAMERA_PERMISSION:
                if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            Bitmap image;

            switch (requestCode) {
                case RC_CAMERA_ENROLL:
                    image = (Bitmap) data.getExtras().get("data");

                    try {
                        info.setText("Enrolling...");
                        kairos.enroll(image, subject, galleryID, selector, multipleFaces, "0.25", enrolllistener);
                    } catch (JSONException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    break;
                case RC_CAMERA_VERIFY:
                    image = (Bitmap) data.getExtras().get("data");

                    try {
                        info.setText("Verifying...");
                        kairos.recognize(image, galleryID, null, null, null, null, verifylistener);
                    } catch (JSONException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    }
}