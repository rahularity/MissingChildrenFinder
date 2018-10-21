package com.ithought.rahularity.missingchild.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SectionStatePagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = "SectionStatePagerAdapte";

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final HashMap<Fragment,Integer> mFragments = new HashMap<>();
    private final HashMap<String, Integer> mFragmentNumbers = new HashMap<>();
    private final HashMap<Integer, String> mFragmentNames = new HashMap<>();


    public SectionStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }


    public void addFragment(Fragment fragment, String fragmentName ){
        fragmentList.add(fragment);
        mFragments.put(fragment,fragmentList.size()-1);
        mFragmentNumbers.put(fragmentName, fragmentList.size()-1);
        mFragmentNames.put(fragmentList.size()-1, fragmentName);
    }


    /**
     * return the fragment number with the name @param
     * @param fragmentName
     * @return
     */
    public Integer getFragmentNumber(String fragmentName){
        if(mFragmentNumbers.containsKey(fragmentName)){
            return mFragmentNumbers.get(fragmentName);
        }else{
            return null;
        }

    }

    /**
     * return the fragment number with fragment @param
     * @param fragment
     * @return
     */
    public Integer getFragmentNumber(Fragment fragment){
        if(mFragmentNumbers.containsKey(fragment)){
            return mFragmentNumbers.get(fragment);
        }else{
            return null;
        }
    }

    /**
     * return the fragment name with @param
     * @param fragmentNumber
     * @return
     */
    public String getFragmentName(Integer fragmentNumber){
        if(mFragmentNames.containsKey(fragmentNumber)){
            return mFragmentNames.get(fragmentNumber);
        }else{
            return null;
        }
    }
}
