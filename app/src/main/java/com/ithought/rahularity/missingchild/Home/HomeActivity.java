package com.ithought.rahularity.missingchild.Home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionPoint;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark;
import com.ithought.rahularity.missingchild.R;
import com.ithought.rahularity.missingchild.RetrofitClient;
import com.ithought.rahularity.missingchild.models.Children;
import com.ithought.rahularity.missingchild.models.UserObject;
import com.ithought.rahularity.missingchild.utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {


    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = "MainActivity";
    private ImageView capturedImage;
    private Bitmap imageBitmap;
    public static final int ACTIVITY_NUM = 0;
    private Context context = this;
    private Button recognize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupBottomNavigationView();
        Log.d(TAG, "onCreate: in onCreate Method");

        init();



        capturedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

                }
            }
        });

        //faceRecognition();
    }

    private void init() {
        capturedImage = (ImageView)findViewById(R.id.captured_image);
        recognize = (Button)findViewById(R.id.recognize_button);
    }

    private void faceRecognition() {

        Call<UserObject> call = RetrofitClient
                .getInstance()
                .getApi()
                .compare("rahul");

        call.enqueue(new Callback<UserObject>() {
            @Override
            public void onResponse(Call<UserObject> call, Response<UserObject> response) {
                Log.d(TAG, "onResponse: Server Response" + response.toString());

                int responseCode = response.code();

                if(responseCode == 200){

                    String status = response.body().getStatus();
                    if(status.equals("matched")){

                        List<Children> children = response.body().getChildren();

                    }else{
                        //TODO: when there is no match for the picture clicked
                    }

                }
            }

            @Override
            public void onFailure(Call<UserObject> call, Throwable t) {

                Log.e(TAG, "onFailure: something went wrong." + t.getMessage());
                Toast.makeText(HomeActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();

            }
        });

    }


    /**
     *BottomNavigationView setup
     */
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx)findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setUpBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(context,bottomNavigationViewEx);

        //getting menu item so as to highlight the correct BottomNavigationItem
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

//    private void settingUpFaceDetection() {
//        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);
//
//        FirebaseVisionFaceDetectorOptions options =
//                new FirebaseVisionFaceDetectorOptions.Builder()
//                        .setModeType(FirebaseVisionFaceDetectorOptions.ACCURATE_MODE)
//                        .setLandmarkType(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
//                        .setClassificationType(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
//                        .setMinFaceSize(0.15f)
//                        .setTrackingEnabled(true)
//                        .build();
//
//
//
//        FirebaseVisionFaceDetector detector = FirebaseVision.getInstance()
//                .getVisionFaceDetector(options);
//
//
//        Task<List<FirebaseVisionFace>> result =
//                detector.detectInImage(image)
//                        .addOnSuccessListener(
//                                new OnSuccessListener<List<FirebaseVisionFace>>() {
//                                    @Override
//                                    public void onSuccess(List<FirebaseVisionFace> faces) {
//                                        // Task completed successfully
//
//
//                                        list.clear();
//
//                                        for (FirebaseVisionFace face : faces) {
//                                            Rect bounds = face.getBoundingBox();
//                                            float rotY = face.getHeadEulerAngleY();  // Head is rotated to the right rotY degrees
//                                            float rotZ = face.getHeadEulerAngleZ();  // Head is tilted sideways rotZ degrees
//
//                                            String p1 = "rotated to right = " + rotY + "\nrotated to left = " + rotZ;
//
//                                            Log.d(TAG, "onSuccess: rotated to right = " + rotY + "\nrotated to left = " + rotZ);
//
//                                            // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
//                                            // nose available):
//                                            FirebaseVisionFaceLandmark leftEar = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EAR);
//                                            if (leftEar != null) {
//                                                FirebaseVisionPoint leftEarPos = leftEar.getPosition();
//                                            }
//
//
//
//                                            // If classification was enabled:
//                                            if (face.getSmilingProbability() != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
//                                                float smileProb = face.getSmilingProbability();
//                                                p1 +=  "\nsmile probability = " + smileProb;
//                                            }
//
//
//                                            if (face.getRightEyeOpenProbability() != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
//                                                float rightEyeOpenProb = face.getRightEyeOpenProbability();
//                                                p1 +=  "\nright eye open probability = " + rightEyeOpenProb;
//                                                float leftEyeOpenProb = face.getLeftEyeOpenProbability();
//                                                p1 +=  "\nright eye open probability = " + leftEyeOpenProb;
//                                            }
//
//                                            // If face tracking was enabled:
//                                            if (face.getTrackingId() != FirebaseVisionFace.INVALID_ID) {
//                                                int id = face.getTrackingId();
//                                                p1 +=  "\nface tracking id = " + id;
//                                            }
//
//                                            list.add(p1);
//                                        }
//
//                                        String finalString = "";
//                                        for(String faceDescription : list){
//                                            finalString += "\n"+faceDescription;
//                                        }
//                                        picDetails.setText(finalString);
//
//                                    }
//                                })
//                        .addOnFailureListener(
//                                new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        // Task failed with an exception
//                                        // ...
//                                    }
//                                });
//
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            capturedImage.setImageBitmap(imageBitmap);


        }
    }
}
