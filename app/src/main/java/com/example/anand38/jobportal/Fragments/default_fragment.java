package com.example.anand38.jobportal.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anand38.jobportal.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class default_fragment extends Fragment {


    public default_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_default_fragment, container, false);
    }

}
