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
import android.widget.EditText;
import android.widget.ImageView;

import com.kairos.*;

import org.json.JSONException;





import java.io.UnsupportedEncodingException;



public class MainActivity extends Activity {
    EditText mEdit,mEdit2;

    private static final int RC_CAMERA_PERMISSION = 100;

    private static final int RC_CAMERA = 101;

    @Override

    public void onCreate(Bundle savedInstanceState) {




        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        if(ContextCompat.checkSelfPermission(this,

                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,

                    new String[]{Manifest.permission.CAMERA},

                    RC_CAMERA_PERMISSION);

        }



        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(intent, RC_CAMERA);

            }

        });

    }



    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case RC_CAMERA_PERMISSION:

                if(!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)){

                    finish();

                }

                break;

        }

    }



    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case RC_CAMERA:



                break;

        }
      final  EditText mEdit=(EditText)findViewById(R.id.editText);
        final  EditText mEdit2=(EditText)findViewById(R.id.editText3);






        // listener

        KairosListener listener = new KairosListener() {



            @Override

            public void onSuccess(String response) {

                mEdit.setText(response);

            }



            @Override

            public void onFail(String response) {

                mEdit.setText(response);
            }

        };
        KairosListener listener2 = new KairosListener()
        {
            @Override

            public void onSuccess(String response) {

                mEdit2.setText(response);

            }



            @Override

            public void onFail(String response) {

                mEdit2.setText(response);
            }

        };





        /* * * instantiate a new kairos instance * * */

        Kairos myKairos = new Kairos();



        /* * * set authentication * * */

        String app_id = "76bb40a3";

        String api_key = "ec86592c2dd6e66bb5a0009540a7a6ec";

        myKairos.setAuthentication(this, app_id, api_key);









        try {

            //  List galleries

           //  myKairos.listGalleries(listener);


            // Bare-essentials Example:



          //  String image = "http://media.kairos.com/liz.jpg";

         //   myKairos.detect(image, null, null, listener);

/*

         // Fine-grained Example:

            // This example uses a bitmap image and also optional parameters

           / Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.liz);

            String selector = "FULL";

            String minHeadScale = "0.25";

            myKairos.detect(image, selector, minHeadScale, listener);

            */

            // Bare-essentials Example:

            // This example uses only an image url, setting optional params to null

// enrolling

           String image = "http://www.jta.org/wp-content/uploads/2016/05/Hillary-Clinton.jpg";

            String subjectId = "Kareena3";

             String galleryId = "new";
            String selector = "FULL";

            String multipleFaces = "false";

            String minHeadScale = "0.25";

           myKairos.enroll(image, subjectId, galleryId, selector, multipleFaces,minHeadScale,listener);

            // recognising

           image = "http://www.jta.org/wp-content/uploads/2016/05/Hillary-Clinton.jpg";

            galleryId = "new";

            myKairos.recognize(image, galleryId, null, null, null, null, listener2);




/*

            // Fine-grained Example:

            // This example uses a bitmap image and also optional parameters

            Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.liz);

            String subjectId = "Elizabeth";

            String galleryId = "friends";

            String selector = "FULL";

            String multipleFaces = "false";

            String minHeadScale = "0.25";

            myKairos.enroll(image,

                    subjectId,

                    galleryId,

                    selector,

                    multipleFaces,

                    minHeadScale,

                    listener);



                    */

            // Bare-essentials Example:

            // This example uses only an image url, setting optional params to null
/*
            // Fine-grained Example:

            // This example uses a bitmap image and also optional parameters

            Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.liz);

            String galleryId = "friends";

            String selector = "FULL";

            String threshold = "0.75";

            String minHeadScale = "0.25";

            String maxNumResults = "25";

            myKairos.recognize(image,

                    galleryId,

                    selector,

                    threshold,

                    minHeadScale,

                    maxNumResults,

                    listener);

                    */

            /* * * * GALLERY-MANAGEMENT EXAMPLES * * * *

            //  List galleries

            myKairos.listGalleries(listener);

            //  List subjects in gallery

            myKairos.listSubjectsForGallery("your_gallery_name", listener);

            // Delete subject from gallery

            myKairos.deleteSubject("your_subject_id", "your_gallery_name", listener);


            // Delete an entire gallery

            myKairos.deleteGallery("your_gallery_name", listener);



            */
        } catch (JSONException e) {

            e.printStackTrace();

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        }





    }



}