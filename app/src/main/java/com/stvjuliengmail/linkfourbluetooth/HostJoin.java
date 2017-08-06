package com.stvjuliengmail.linkfourbluetooth;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class HostJoin extends Fragment {

    View rootView;
    Button btnHost, btnJoin;

    public HostJoin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_host_join, container, false);
        btnHost = (Button) rootView.findViewById(R.id.btnHost);
        btnJoin = (Button) rootView.findViewById(R.id.btnJoin);

        btnHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Host", Toast.LENGTH_SHORT).show();
                MainActivity ma = (MainActivity) getActivity();
                ma.showHost();
            }
        });
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Join", Toast.LENGTH_SHORT).show();
                MainActivity ma = (MainActivity) getActivity();
                ma.loadBoard();

                ma.join();


            }
        });
//        View decorView = rootView.getDecorView();
// Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.
//        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_FULLSCREEN;
//        rootView.setSystemUiVisibility(uiOptions);
        // Inflate the layout for this fragment
        return rootView;
    }



}
