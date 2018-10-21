package com.ithought.rahularity.missingchild.Home2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.ithought.rahularity.missingchild.Home.HomeActivity;
import com.ithought.rahularity.missingchild.R;
import com.ithought.rahularity.missingchild.RetrofitClient;
import com.ithought.rahularity.missingchild.models.Children;
import com.ithought.rahularity.missingchild.models.UserObject;
import com.ithought.rahularity.missingchild.utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity2 extends AppCompatActivity {


    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private static final String TAG = "HomeActivity2";
    private ImageView capturedImage;
    private Bitmap imageBitmap;
    private Button recognize;
    public static final int ACTIVITY_NUM = 0;
    private Context context = this;
    private String userChoosenTask;
    private String encoded = "";
    private RecyclerView recyclerView;
    private ProgressBar progress;
    private TextView instructionText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupBottomNavigationView();
        Log.d(TAG, "onCreate: in onCreate Method");

        init();

        //setting bitmap of default image
        //imageBitmap = ((BitmapDrawable)this.getResources().getDrawable(R.drawable.child)).getBitmap();
        //ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        //byte[] byteArray = bytes.toByteArray();
        //encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);


        capturedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        recognize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(encoded.isEmpty()){
                    Toast.makeText(context,"Please provide a picture of the child to recognize",Toast.LENGTH_LONG).show();
                }else{
                    faceRecognition();
                }

            }
        });


    }


    private void faceRecognition() {

        recyclerView.setVisibility(View.GONE);
        instructionText.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);

        Call<UserObject> call = RetrofitClient
                .getInstance()
                .getApi()
                .compare(encoded);

        call.enqueue(new Callback<UserObject>() {
            @Override
            public void onResponse(Call<UserObject> call, Response<UserObject> response) {
                Log.d(TAG, "onResponse: Server Response" + response.toString());

                int responseCode = response.code();

                if(responseCode == 200){

                    String status = response.body().getStatus();
                    if(status.equals("matched")){

                        List<Children> children = response.body().getChildren();
                        int numberOfChildrenMatched = children.size();

                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setAdapter(new ChildAdapter(children,R.layout.list_item_children, getApplicationContext()));
                        progress.setVisibility(View.GONE);
                        Toast.makeText(context, "Successfully fetched. There are " + numberOfChildrenMatched + " matched faces from our database.", Toast.LENGTH_LONG).show();



                    }else{
                        //TODO: when there is no match for the picture clicked
                        progress.setVisibility(View.GONE);
                        instructionText.setText("Alas! No face in our database is matched with this child.");
                        instructionText.setVisibility(View.VISIBLE);
                        Toast.makeText(context, "Alas! No face in our database is matched with this child.", Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<UserObject> call, Throwable t) {

                Log.e(TAG, "onFailure: something went wrong." + t.getMessage());
                progress.setVisibility(View.GONE);
                instructionText.setVisibility(View.VISIBLE);
                Toast.makeText(HomeActivity2.this,"Something went wrong...please try again.",Toast.LENGTH_LONG).show();

            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity2.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(HomeActivity2.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        imageBitmap = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] byteArray = bytes.toByteArray();

        encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        capturedImage.setImageBitmap(imageBitmap);


    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {


        if (data != null) {
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] byteArray = bytes.toByteArray();
        encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

        capturedImage.setImageBitmap(imageBitmap);
    }


    private void init() {
        instructionText = (TextView)findViewById(R.id.instruction_text);
        progress = (ProgressBar)findViewById(R.id.progress);
        capturedImage = (ImageView)findViewById(R.id.captured_image);
        recognize = (Button)findViewById(R.id.recognize_button);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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

}









