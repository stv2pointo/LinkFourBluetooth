package com.stvjuliengmail.linkfourbluetooth;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    final Handler vHandler = new Handler();
    String userName = "", opponent = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_main);

        // don't know why, but won't poll for devices without this
        checkBTPermissions();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragContainer, new Title(), "TF")
                .addToBackStack(null)
                .commit();


        vHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragContainer, new Board(),"BF")
//                        .addToBackStack(null)
//                        .commit();
                promptUserName();
            }
        },1500);


    }

    /**
     *  Sticky Immersion:
     *  an inward swipe in the system bars areas causes the bars to temporarily appear in a
     *  semi-transparent state, but no flags are cleared, and your system UI visibility change
     *  listeners are not triggered. The bars automatically hide again after a short delay, or
     *  if the user interacts with the middle of the screen.
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View decorView = getWindow().getDecorView();
        if (hasFocus) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

//    public void showHost() {
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragContainer, new Board(),"BF")
//                .addToBackStack(null)
//                .commit();
//
//        Board board = (Board) getSupportFragmentManager().findFragmentByTag("BF");
//        if(board != null){
//            board.ensureDiscoverable();
//        }
//
//
//    }

//    public void loadBoard() {
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragContainer, new Board(),"BF")
//                .addToBackStack(null)
//                .commit();
//    }
//    public void join() {
//        vHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Board board = (Board) getSupportFragmentManager().findFragmentByTag("BF");
//                if(board != null){
//                    Log.d("test", "board is not null");
//                    board.connectPlayer2();
//                }
//                else{
//                    Log.d("test", "board is null");
//                }
//            }
//        },3500);
//
//    }

    /**
     * this method is required for all devices running api23 +
     * android must programmatically check the permissions for bt
     * putting teh proper permissions in the manifest is not enough for some reason ..??
     */
    private void checkBTPermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if(permissionCheck != 0) {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},1001);
            }
        }
        else {
            Log.d("test", "lollipop or less");
        }
    }

    private void promptUserName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("What's your handle?");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setCancelable(false);

// Set up the buttons
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userName = input.getText().toString();
                loadBoard();
            }
        });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });

        builder.show();
    }
    public void loadBoard() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragContainer, new Board(),"BF")
                .addToBackStack(null)
                .commit();
    }
}
