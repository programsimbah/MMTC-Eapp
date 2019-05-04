package com.pengembangsebelah.stmmappxo.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pengembangsebelah.stmmappxo.MainActivity;
import com.pengembangsebelah.stmmappxo.R;
import com.pengembangsebelah.stmmappxo.model.Kontent;
import com.pengembangsebelah.stmmappxo.model.ScheduleProgram;
import com.pengembangsebelah.stmmappxo.ui.adapter.SectionRadioFragmentAdapter;
import com.pengembangsebelah.stmmappxo.utils.CustomViewPager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RadioFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    SectionRadioFragmentAdapter sectionRadioFragmentAdapter;
    CustomViewPager viewPager;

    public RadioFragment() {
        setHasOptionsMenu(true);
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static RadioFragment newInstance(int sectionNumber) {
        RadioFragment fragment = new RadioFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    void Valid(List<ScheduleProgram> scheduleProgramse){
        ((MainActivity) getActivity()).scheduleProgramse=scheduleProgramse;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_radio_tab, container, false);
        setHasOptionsMenu(true);
        init(rootView);
        FirebaseDatabase.getInstance().getReference().child(Kontent.ARG_RADIO_APK).child(Kontent.ARG_RADIO_SCH).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshotIterator=dataSnapshot.getChildren().iterator();
                List<ScheduleProgram> schedulePrograms=new ArrayList<>();
                while (dataSnapshotIterator.hasNext()){
                    DataSnapshot dataSnapshot1=dataSnapshotIterator.next();
                    ScheduleProgram itm=dataSnapshot1.getValue(ScheduleProgram.class);
                    schedulePrograms.add(itm);
                }

                Valid(schedulePrograms);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.radio_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    void init(View view){
        AppBarLayout appBarLayout = view.findViewById(R.id.appbar);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs_radio_fragment);
        sectionRadioFragmentAdapter = new SectionRadioFragmentAdapter(getChildFragmentManager());

        viewPager = view.findViewById(R.id.container_radio_fragment);
        viewPager.setAdapter(sectionRadioFragmentAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

    }
}
