package com.ithought.rahularity.missingchild.MissingChildrenList;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ithought.rahularity.missingchild.Home2.ChildAdapter;
import com.ithought.rahularity.missingchild.Home2.HomeActivity2;
import com.ithought.rahularity.missingchild.R;
import com.ithought.rahularity.missingchild.RetrofitClient;
import com.ithought.rahularity.missingchild.models.Children;
import com.ithought.rahularity.missingchild.models.UserObject;
import com.ithought.rahularity.missingchild.utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MissingChildrenListActivity extends AppCompatActivity {

    private static final String TAG = "ChildrenListActivity";
    public static final int ACTIVITY_NUM = 2;
    private Context context = this;
    private ProgressBar progress;
    private TextView instructionText;
    private RecyclerView recyclerView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missing_children_list);

        Log.d(TAG, "onCreate: MissingChildrenListActivity has started");
        setupBottomNavigationView();
        init();

        //faceRecognition();



    }

    private void refresh() {
        faceRecognition();
    }

    private void faceRecognition() {
        instructionText.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);

        Call<UserObject> call = RetrofitClient
                .getInstance()
                .getApi()
                .getall();

        call.enqueue(new Callback<UserObject>() {
            @Override
            public void onResponse(Call<UserObject> call, Response<UserObject> response) {
                Log.d(TAG, "onResponse: Server Response" + response.toString());

                int responseCode = response.code();

                if(responseCode == 200){

                    String status = response.body().getStatus();
                    if(status.equals("good")){

                        List<Children> children = response.body().getChildren();
                        int numberOfChildrenMatched = children.size();

                        recyclerView.setAdapter(new ChildAdapter(children,R.layout.list_item_children, getApplicationContext()));
                        progress.setVisibility(View.GONE);
                        Toast.makeText(context, "Successfully fetched. There are " + numberOfChildrenMatched + " lost children in our database.", Toast.LENGTH_LONG).show();



                    }else{
                        //TODO: when there is no match for the picture clicked
                        progress.setVisibility(View.GONE);
                        instructionText.setText("There are no children to show in our database");
                        instructionText.setVisibility(View.VISIBLE);
                        Toast.makeText(context, "There are no children in our database", Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<UserObject> call, Throwable t) {

                Log.e(TAG, "onFailure: something went wrong." + t.getMessage());
                progress.setVisibility(View.GONE);
                instructionText.setVisibility(View.VISIBLE);
                Toast.makeText(MissingChildrenListActivity.this,"Something went wrong...please try again.",Toast.LENGTH_LONG).show();

            }
        });
    }

    private void init() {
        instructionText = (TextView)findViewById(R.id.instruction_text);
        progress = (ProgressBar)findViewById(R.id.progress);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        toolbar = (Toolbar)findViewById(R.id.list_activity_toolbar);
        setSupportActionBar(toolbar);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_activity_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_menu_item:
                refresh();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


}
