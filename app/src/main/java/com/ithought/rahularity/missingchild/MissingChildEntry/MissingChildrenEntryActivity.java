package com.ithought.rahularity.missingchild.MissingChildEntry;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ithought.rahularity.missingchild.Home2.HomeActivity2;
import com.ithought.rahularity.missingchild.Home2.Utility;
import com.ithought.rahularity.missingchild.R;
import com.ithought.rahularity.missingchild.RetrofitClient;
import com.ithought.rahularity.missingchild.models.AddingChildStatusObject;
import com.ithought.rahularity.missingchild.utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MissingChildrenEntryActivity extends AppCompatActivity {

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private static final String TAG = "ChildrenEntryActivity";
    public static final int ACTIVITY_NUM = 1;
    private Context context = this;
    private CircleImageView childPhoto;
    private EditText name, fatherName, contactNumber, age, policeStation, missingDate, missingPlace, height, weight, color, address;
    private Button saveDetails;
    private Bitmap imageBitmap;
    private String userChoosenTask;
    private String encoded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missing_children_entry);

        Log.d(TAG, "onCreate: MissingChildrenEntry started");

        init();
        setupBottomNavigationView();

        saveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savingDetailsProcess();
            }
        });

        childPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });





    }

    private void savingDetailsProcess() {

        String name_text = name.getText().toString().trim();
        String fatherName_text = fatherName.getText().toString().trim();
        String contact_text = contactNumber.getText().toString().trim();
        String age_text = age.getText().toString().trim();
        String policeStation_text = policeStation.getText().toString().trim();
        String date_text = missingDate.getText().toString().trim();
        String place_text = missingPlace.getText().toString().trim();
        String height_text = height.getText().toString().trim();
        String weight_text = weight.getText().toString().trim();
        String complexion_text = color.getText().toString().trim();
        String address_text = address.getText().toString().trim();

        if(imageBitmap == null){
            Toast.makeText(this,"please provide a picture of the missing person.",Toast.LENGTH_LONG).show();
        }

        else if (name_text.isEmpty())
            inputValidation(name,"Person's name is required");

        else if (place_text.isEmpty())
            inputValidation(missingPlace,"address is required");

        else if (fatherName_text.isEmpty())
            inputValidation(fatherName,"Father's Name is required");

        else if (contact_text.isEmpty())
            inputValidation(contactNumber,"Please provide a contact number");

        else if (age_text.isEmpty())
            inputValidation(age,"Age of the person is required");

        else if (policeStation_text.isEmpty())
            inputValidation(policeStation,"please provide your nearest police station");

        else if (date_text.isEmpty())
            inputValidation(missingDate,"please provide the missing date (e.g., 21/06/2018)");

        else if (height_text.isEmpty())
            inputValidation(height,"height is required");

        else if (weight_text.isEmpty())
            inputValidation(weight,"weight is required");

        else if (complexion_text.isEmpty())
            inputValidation(color,"please provide color complextion of the child");

        else if(address_text.isEmpty())
            inputValidation(address,"please provide address of the missing child");

        else{

            Call<AddingChildStatusObject> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .add(encoded , name_text,  age_text, fatherName_text, policeStation_text, complexion_text, contact_text,
                            address_text, date_text, place_text, height_text, weight_text);

            call.enqueue(new Callback<AddingChildStatusObject>() {
                @Override
                public void onResponse(Call<AddingChildStatusObject> call, Response<AddingChildStatusObject> response) {
                    Log.d(TAG, "onResponse: Server Response; " + response.toString());

                    int responseCode = response.code();

                    if(responseCode == 200){

                        String status = response.body().getStatus();
                        Log.d(TAG, "onResponse: picture added successfuly : " + status);
                        Toast.makeText(context,status,Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MissingChildrenEntryActivity.this,HomeActivity2.class));
                        finish();


                    }
                }

                @Override
                public void onFailure(Call<AddingChildStatusObject> call, Throwable t) {
                    Log.e(TAG, "onFailure: something went wrong." + t.getMessage());
                    Toast.makeText(MissingChildrenEntryActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();

                }
            });
        }

    }

    private void init() {

        saveDetails = (Button)findViewById(R.id.save_details);
        childPhoto = (CircleImageView)findViewById(R.id.missing_pic);
        name = (EditText)findViewById(R.id.name);
        fatherName = (EditText)findViewById(R.id.father_name);
        contactNumber = (EditText)findViewById(R.id.contact_no);
        age = (EditText)findViewById(R.id.age);
        policeStation = (EditText)findViewById(R.id.police_station);
        missingDate = (EditText)findViewById(R.id.date_missing);
        missingPlace = (EditText)findViewById(R.id.place_missing);
        height = (EditText)findViewById(R.id.height_child);
        weight = (EditText)findViewById(R.id.weight_child);
        color = (EditText)findViewById(R.id.color_child);
        address = (EditText)findViewById(R.id.address);
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

    private void inputValidation(EditText et, String msg){
        et.setError(msg);
        et.requestFocus();
    }



    //getting image from gallery or camera
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(MissingChildrenEntryActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(MissingChildrenEntryActivity.this);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
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


    private void onCaptureImageResult(Intent data) {
        imageBitmap = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] byteArray = bytes.toByteArray();

        encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        childPhoto.setImageBitmap(imageBitmap);


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

        childPhoto.setImageBitmap(imageBitmap);
    }



}
