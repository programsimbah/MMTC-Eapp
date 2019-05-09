package com.pengembangsebelah.stmmappxo.ui.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.pengembangsebelah.stmmappxo.ui.fragment.PromoFragment;
import com.pengembangsebelah.stmmappxo.ui.fragment.RadioFragment;
import com.pengembangsebelah.stmmappxo.ui.fragment.HomeFragment;

public class SectionHomeAdapter extends FragmentPagerAdapter {


    public SectionHomeAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        if(i==0){
            return HomeFragment.newInstance(1);
        }else if(i==1){
            return RadioFragment.newInstance(1);
        }else if(i==2){
            //return TVFragment.newInstance(1);
            return PromoFragment.newInstance(1);
        //}else if(i==3){
            //return PromoFragment.newInstance(1);
        }else {
            return HomeFragment.newInstance(1);
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
