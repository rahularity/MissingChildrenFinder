package com.ithought.rahularity.missingchild.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.ithought.rahularity.missingchild.Home.HomeActivity;
import com.ithought.rahularity.missingchild.Home2.HomeActivity2;
import com.ithought.rahularity.missingchild.MissingChildEntry.MissingChildrenEntryActivity;
import com.ithought.rahularity.missingchild.MissingChildrenList.MissingChildrenListActivity;
import com.ithought.rahularity.missingchild.R;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class BottomNavigationViewHelper {
    private static final String TAG = "BottomNavigationViewHel";

    public static void setUpBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx){
        Log.d(TAG, "setUpBottomNavigationView: Setting up BottomNavigationView");
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
    }

    public static void enableNavigation(final Context context, BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.ic_house:
                        Intent intent1 = new Intent(context, HomeActivity2.class);
                        context.startActivity(intent1);
                        break;

                    case R.id.ic_entry:
                        Intent intent2 = new Intent(context, MissingChildrenEntryActivity.class);
                        context.startActivity(intent2);
                        break;

                    case R.id.ic_list:
                        Intent intent3 = new Intent(context, MissingChildrenListActivity.class);
                        context.startActivity(intent3);
                        break;

                }

                return false;
            }
        });
    }
}
