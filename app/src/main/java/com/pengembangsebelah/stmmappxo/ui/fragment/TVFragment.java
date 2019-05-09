package com.pengembangsebelah.stmmappxo.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.pengembangsebelah.stmmappxo.R;

public class TVFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    public TVFragment() {
        setHasOptionsMenu(true);
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TVFragment newInstance(int sectionNumber) {
        TVFragment fragment = new TVFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_test_tabbed, container, false);
        setHasOptionsMenu(true);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.tv_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
