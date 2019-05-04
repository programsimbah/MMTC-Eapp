package com.pengembangsebelah.stmmappxo.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.pengembangsebelah.stmmappxo.ui.fragment.RadioChatFragment;
import com.pengembangsebelah.stmmappxo.ui.fragment.RadioNewsFragment;
import com.pengembangsebelah.stmmappxo.ui.fragment.RadioSchduleFragment;
import com.pengembangsebelah.stmmappxo.ui.fragment.RadioTopChartFragment;

public class SectionRadioFragmentAdapter extends FragmentPagerAdapter {


    public SectionRadioFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        if(i==0){
            return RadioChatFragment.newInstance(1);
        }else if(i==1){
            return RadioSchduleFragment.newInstance(1);
        }else if(i==2){
            return RadioTopChartFragment.newInstance(1);
        }else if(i==3){
            return RadioNewsFragment.newInstance(1);
        }else {
            return RadioChatFragment.newInstance(1);
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
