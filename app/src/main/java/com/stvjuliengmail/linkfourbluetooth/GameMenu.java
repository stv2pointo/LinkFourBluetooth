package com.stvjuliengmail.linkfourbluetooth;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameMenu extends Fragment {
    private View rootView;
    private Button btnSinglePlayer, btnTwoPlayerBT;
    public GameMenu() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_game_menu, container, false);
        btnSinglePlayer = (Button)rootView.findViewById(R.id.btnSinglePlayer);
        btnTwoPlayerBT = (Button)rootView.findViewById(R.id.btnTwoPlayerBT);

        btnSinglePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "Single player clicked", Toast.LENGTH_SHORT).show();
                loadSinglePlayer();
            }
        });
        btnTwoPlayerBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "Two player clicked", Toast.LENGTH_SHORT).show();
                loadTwoPlayerBT();
            }
        });
        return rootView;
    }

    public void loadTwoPlayerBT(){
        MainActivity mainActivity = (MainActivity)getActivity();
        mainActivity.loadBoard();
    }

    public void loadSinglePlayer(){
        MainActivity mainActivity = (MainActivity)getActivity();
        mainActivity.loadSinglePlayerBoard();
    }

}
