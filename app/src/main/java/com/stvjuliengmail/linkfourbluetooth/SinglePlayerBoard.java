package com.stvjuliengmail.linkfourbluetooth;


import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static com.stvjuliengmail.linkfourbluetooth.R.id.imvA6;


/**
 * A simple {@link Fragment} subclass.
 */
public class SinglePlayerBoard extends Fragment implements View.OnTouchListener{
        //game board stuff
        final Handler paintHandler = new Handler();
        final int COLUMNS = 7, ROWS = 6;
        int[][] cells;
        View rootView;
        LinearLayout colA, colB, colC,colD,colE,colF,colG;
        TextView tvP1Name, tvP2Name;
        LinearLayout buttons_view, scores_view;
        private final int BLACK=1,RED=2;
        int currentPlayer = BLACK;
        int blackScore = 0, redScore = 0;
        String userName = "NoName", opponentName = "Computer";
        private boolean isPlayer1 = false; // make this alternate??


    public SinglePlayerBoard() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);

        //for tracking filled spaces
        cells = new int[COLUMNS][ROWS];
        for (int i=0;i<COLUMNS;i++) {
            for(int j=0;j<ROWS;j++) {
                cells[i][j] = 0;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_single_player_board, container, false);
        colA = (LinearLayout) rootView.findViewById(R.id.colA);
        colA.setOnTouchListener(this);
        colB = (LinearLayout) rootView.findViewById(R.id.colB);
        colB.setOnTouchListener(this);
        colC = (LinearLayout) rootView.findViewById(R.id.colC);
        colC.setOnTouchListener(this);
        colD = (LinearLayout) rootView.findViewById(R.id.colD);
        colD.setOnTouchListener(this);
        colE = (LinearLayout) rootView.findViewById(R.id.colE);
        colE.setOnTouchListener(this);
        colF = (LinearLayout) rootView.findViewById(R.id.colF);
        colF.setOnTouchListener(this);
        colG = (LinearLayout) rootView.findViewById(R.id.colG);
        colG.setOnTouchListener(this);
        buttons_view = (LinearLayout) rootView.findViewById(R.id.buttons_view);
        scores_view = (LinearLayout) rootView.findViewById(R.id.scores_view);
        tvP1Name = (TextView) rootView.findViewById(R.id.tvP1Name);
        tvP2Name = (TextView) rootView.findViewById(R.id.tvP2Name);
        MainActivity ma = (MainActivity) getActivity();
        userName = ma.userName;
        tvP1Name.setText(userName);
        return rootView;

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP) {
            int selectedRow;
            switch (v.getId()) {
                case R.id.colA:
                    makeMove(0);
                    lockBoard();
                    if(checkForWin()) {
                        chickenDinner();
                    }
                    playAI();
                    break;
                case R.id.colB:
                    makeMove(1);
                    lockBoard();
                    if(checkForWin()) {
                        chickenDinner();
                    }
                    playAI();
                    break;
                case R.id.colC:
                    makeMove(2);
                    lockBoard();
                    if(checkForWin()) {
                        chickenDinner();
                    }
                    playAI();
                    break;
                case R.id.colD:
                    makeMove(3);
                    lockBoard();
                    if(checkForWin()) {
                        chickenDinner();
                    }
                    playAI();
                    break;
                case R.id.colE:
                    makeMove(4);
                    lockBoard();
                    if(checkForWin()) {
                        chickenDinner();
                    }
                    playAI();
                    break;
                case R.id.colF:
                    makeMove(5);
                    lockBoard();
                    if(checkForWin()) {
                        chickenDinner();
                    }
                    playAI();
                    break;
                case R.id.colG:
                    makeMove(6);
                    lockBoard();
                    if(checkForWin()) {
                        chickenDinner();
                    }
                    playAI();
                    break;
                default:
                    break;
            }

        }
        return false;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // ?? set up escape button here???
//        mConversationView = (ListView) view.findViewById(R.id.in);
//        mOutEditText = (EditText) view.findViewById(R.id.edit_text_out);
//        mSendButton = (Button) view.findViewById(R.id.button_send);
    }

    // ??? what the hell is this????
    private void setStatus(CharSequence subTitle) {
        FragmentActivity activity = getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(subTitle);
    }

    public void connectPlayer1() {
        currentPlayer = BLACK;
        unlockBoard();
//        buttons_view.setVisibility(View.GONE);
//        scores_view.setVisibility(View.VISIBLE);
    }

    // get lowest open space in a column
    public int openSpace(int col) {
        int row = -1;
        for (int i=0;i<ROWS;i++) {
            if(cells[col][i] == 0){
                cells[col][i] = currentPlayer;
                paintIt(col,i);
                return i;
            }
        }
        return row;
    }

    public void playAI() {
        Toast.makeText(getActivity()," ai played", Toast.LENGTH_SHORT).show();
    }

    public void paintIt(int col, int row) {
        ImageView iv;
        iv = (ImageView) rootView.findViewById(R.id.imvB1);
        switch (col) {
            case 0:
                switch (row){
                    case 0:
                        iv = (ImageView) rootView.findViewById(R.id.imvA1);

                        break;
                    case 1:
                        iv = (ImageView) rootView.findViewById(R.id.imvA2);
                        break;
                    case 2:
                        iv = (ImageView) rootView.findViewById(R.id.imvA3);
                        break;
                    case 3:
                        iv = (ImageView) rootView.findViewById(R.id.imvA4);
                        break;
                    case 4:
                        iv = (ImageView) rootView.findViewById(R.id.imvA5);
                        break;
                    case 5:
                        iv = (ImageView) rootView.findViewById(imvA6);
                        break;
                    default:
                        break;
                }
                break;
            case 1:
                switch (row){
                    case 0:
                        iv = (ImageView) rootView.findViewById(R.id.imvB1);
                        break;
                    case 1:
                        iv = (ImageView) rootView.findViewById(R.id.imvB2);
                        break;
                    case 2:
                        iv = (ImageView) rootView.findViewById(R.id.imvB3);
                        break;
                    case 3:
                        iv = (ImageView) rootView.findViewById(R.id.imvB4);
                        break;
                    case 4:
                        iv = (ImageView) rootView.findViewById(R.id.imvB5);
                        break;
                    case 5:
                        iv = (ImageView) rootView.findViewById(R.id.imvB6);
                        break;
                    default:
                        break;
                }
                break;
            case 2:
                switch (row){
                    case 0:
                        iv = (ImageView) rootView.findViewById(R.id.imvC1);
                        break;
                    case 1:
                        iv = (ImageView) rootView.findViewById(R.id.imvC2);
                        break;
                    case 2:
                        iv = (ImageView) rootView.findViewById(R.id.imvC3);
                        break;
                    case 3:
                        iv = (ImageView) rootView.findViewById(R.id.imvC4);
                        break;
                    case 4:
                        iv = (ImageView) rootView.findViewById(R.id.imvC5);
                        break;
                    case 5:
                        iv = (ImageView) rootView.findViewById(R.id.imvC6);
                        break;
                    default:
                        break;
                }
                break;
            case 3:
                switch (row){
                    case 0:
                        iv = (ImageView) rootView.findViewById(R.id.imvD1);
                        break;
                    case 1:
                        iv = (ImageView) rootView.findViewById(R.id.imvD2);
                        break;
                    case 2:
                        iv = (ImageView) rootView.findViewById(R.id.imvD3);
                        break;
                    case 3:
                        iv = (ImageView) rootView.findViewById(R.id.imvD4);
                        break;
                    case 4:
                        iv = (ImageView) rootView.findViewById(R.id.imvD5);
                        break;
                    case 5:
                        iv = (ImageView) rootView.findViewById(R.id.imvD6);
                        break;
                    default:
                        break;
                }
                break;
            case 4:
                switch (row){
                    case 0:
                        iv = (ImageView) rootView.findViewById(R.id.imvE1);
                        break;
                    case 1:
                        iv = (ImageView) rootView.findViewById(R.id.imvE2);
                        break;
                    case 2:
                        iv = (ImageView) rootView.findViewById(R.id.imvE3);
                        break;
                    case 3:
                        iv = (ImageView) rootView.findViewById(R.id.imvE4);
                        break;
                    case 4:
                        iv = (ImageView) rootView.findViewById(R.id.imvE5);
                        break;
                    case 5:
                        iv = (ImageView) rootView.findViewById(R.id.imvE6);
                        break;
                    default:
                        break;
                }
                break;
            case 5:
                switch (row){
                    case 0:
                        iv = (ImageView) rootView.findViewById(R.id.imvF1);
                        break;
                    case 1:
                        iv = (ImageView) rootView.findViewById(R.id.imvF2);
                        break;
                    case 2:
                        iv = (ImageView) rootView.findViewById(R.id.imvF3);
                        break;
                    case 3:
                        iv = (ImageView) rootView.findViewById(R.id.imvF4);
                        break;
                    case 4:
                        iv = (ImageView) rootView.findViewById(R.id.imvF5);
                        break;
                    case 5:
                        iv = (ImageView) rootView.findViewById(R.id.imvF6);
                        break;
                    default:
                        break;
                }
                break;
            case 6:
                switch (row){
                    case 0:
                        iv = (ImageView) rootView.findViewById(R.id.imvG1);
                        break;
                    case 1:
                        iv = (ImageView) rootView.findViewById(R.id.imvG2);
                        break;
                    case 2:
                        iv = (ImageView) rootView.findViewById(R.id.imvG3);
                        break;
                    case 3:
                        iv = (ImageView) rootView.findViewById(R.id.imvG4);
                        break;
                    case 4:
                        iv = (ImageView) rootView.findViewById(R.id.imvG5);
                        break;
                    case 5:
                        iv = (ImageView) rootView.findViewById(R.id.imvG6);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        try {
            if (currentPlayer == BLACK) {
                iv.setImageResource(R.drawable.black);
            }
            else if(currentPlayer == RED){
                iv.setImageResource(R.drawable.red);
            }
            else {
                Log.d("test", "paintIt(): issues with currentPlayer");
            }
            Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(25);

        }catch (Exception e) {
            Log.d("test", e.getMessage());
        }
    }

    public void toggleTurn() {
        if (currentPlayer == BLACK)
        {currentPlayer = RED;}
        else if (currentPlayer == RED){
            currentPlayer = BLACK;
        }
        else {
            Log.d("test", "toggle turn is busted");
        }
    }

    public void lockBoard() {
        colA.setClickable(false);
        colB.setClickable(false);
        colC.setClickable(false);
        colD.setClickable(false);
        colE.setClickable(false);
        colF.setClickable(false);
        colG.setClickable(false);
    }

    public void unlockBoard() {
        colA.setClickable(true);
        colB.setClickable(true);
        colC.setClickable(true);
        colD.setClickable(true);
        colE.setClickable(true);
        colF.setClickable(true);
        colG.setClickable(true);
    }

    public void makeMove(int col) {
        int row = openSpace(col);
        if (row > 0) {
            paintIt(col,row);
        }
    }

    public boolean checkForWin() {
        if(checkVertical()) {
            return true;
        }
        if(checkHorizontal()) {
            return true;
        }
        if(checkDiagLeft()) {
            return true;
        }
        if(checkDiagRight()) {
            return true;
        }
        return false;
    }

    public boolean checkVertical() {
        int numContiguous = 0;
        int prior = -1;
        for (int i=0;i<COLUMNS;i++) {
            for(int j=0;j<ROWS;j++) {
                if(cells[i][j] == currentPlayer && cells[i][j] == prior) {
                    numContiguous ++;
                    prior = currentPlayer;
                }
                else if (cells[i][j] == currentPlayer && cells[i][j] != prior) {
                    numContiguous = 1;
                    prior = currentPlayer;
                }
                else {
                    numContiguous = 0;
                    prior = 0;
                }
                if (numContiguous >= 4) {
//                    chickenDinner();
                    return true;
                }

            }
        }
        return false;
    }

    public boolean checkHorizontal() {
        int numContiguous = 0;
        int prior = -1;
        for (int i=0;i<ROWS;i++) {
            for(int j=0;j<COLUMNS;j++) {
                if(cells[j][i] == currentPlayer && cells[j][i] == prior) {
                    numContiguous ++;
                    prior = currentPlayer;
                }
                else if (cells[j][i] == currentPlayer && cells[j][i] != prior) {
                    numContiguous = 1;
                    prior = currentPlayer;
                }
                else {
                    numContiguous = 0;
                    prior = 0;
                }
                if (numContiguous >= 4) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkDiagLeft() {
        for (int i = 0; i < 3; i++)
        {
            for (int j = 3; j < 7; j++)
            {
                if (cells[j][i] == currentPlayer)
                {
                    if (cells[j - 3][i + 3] == currentPlayer && cells[j - 2][i + 2] == currentPlayer && cells[j - 1][i + 1] == currentPlayer)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean checkDiagRight() {
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                if (cells[j][i] == currentPlayer)
                {
                    if (cells[j + 3][i + 3] == currentPlayer && cells[j + 2][i + 2] == currentPlayer && cells[j + 1][i + 1] == currentPlayer)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void chickenDinner() {
        if (currentPlayer == BLACK) {
            blackScore ++;
            if(isPlayer1) {
                Toast.makeText(getActivity(),userName + " wins!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getActivity(),opponentName + " wins!", Toast.LENGTH_SHORT).show();
            }
        }
        else if (currentPlayer == RED) {
            redScore++;
            if(isPlayer1 == false) {
                Toast.makeText(getActivity(),userName + " wins!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getActivity(),opponentName + " wins!", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Log.d("test", "chicken dinner is broken");
        }
        resetGame();
    }

    public void resetGame() {
        TextView tvP1Score = (TextView) rootView.findViewById(R.id.tvP1Score);
        TextView tvP2Score = (TextView) rootView.findViewById(R.id.tvP2Score);
        tvP1Score.setText(Integer.toString(blackScore));
        tvP2Score.setText(Integer.toString(redScore));
        paintHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<COLUMNS;i++) {
                    for(int j=0;j<ROWS;j++) {
                        cells[i][j] = 0;
                    }
                }
                // maybe these can be pushed into an array, then iterate through generically
                ImageView iv = (ImageView) rootView.findViewById(R.id.imvA1);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvA2);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvA3);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvA4);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvA5);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvA6);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvB1);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvB2);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvB3);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvB4);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvB5);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvB6);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvC1);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvC2);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvC3);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvC4);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvC5);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvC6);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvD1);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvD2);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvD3);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvD4);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvD5);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvD6);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvE1);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvE2);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvE3);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvE4);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvE5);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvE6);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvF1);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvF2);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvF3);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvF4);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvF5);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvF6);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvG1);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvG2);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvG3);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvG4);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvG5);
                iv.setImageResource(R.drawable.circle_button);
                iv = (ImageView) rootView.findViewById(R.id.imvG6);
                iv.setImageResource(R.drawable.circle_button);
            }
        },1500);
    }
}
