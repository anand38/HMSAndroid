package com.example.anand38.jobportal.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.anand38.jobportal.Activity.Home;
import com.example.anand38.jobportal.Activity.Search;
import com.example.anand38.jobportal.Global.Global_Variables;
import com.example.anand38.jobportal.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class default_fragment extends Fragment {
    Context c;
    EditText search_box;
    public default_fragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_default_fragment, container, false);
        search_box= (EditText) v.findViewById(R.id.search_box);
        search_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Searchbox was clicked");
                Intent i=new Intent(getActivity(), Search.class);
                startActivity(i);
          }
        });
        return  v;
    }

}
