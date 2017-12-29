package com.stvjuliengmail.linkfourbluetooth;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.EventLog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
public class Board extends Fragment implements View.OnTouchListener{

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
    String userName = "NoName", opponentName = "";
    private boolean isPlayer1 = false;


    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    // Layout Views
    private ListView mConversationView;
    private EditText mOutEditText;
    private Button mSendButton;
    Button btnHost, btnJoin;

    //Name of the connected device
    private String mConnectedDeviceName = null;
    //Array adapter for the conversation thread
    private ArrayAdapter<String> mConversationArrayAdapter;
    //String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;
    //Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    //Member object for the chat services
    private BluetoothService mChatService = null;

    public Board() {
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
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            FragmentActivity activity = getActivity();
            Toast.makeText(activity, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            activity.finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mChatService == null) {
            setupChat();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatService != null) {
            mChatService.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_board, container, false);
        btnHost = (Button) rootView.findViewById(R.id.btnHost);
        btnJoin = (Button) rootView.findViewById(R.id.btnJoin);

        btnHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Host", Toast.LENGTH_SHORT).show();
                connectPlayer1();
            }
        });
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Join", Toast.LENGTH_SHORT).show();
                connectPlayer2();
            }
        });
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
        return rootView;

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_UP) {
            int selectedRow;
            switch (v.getId()) {
                case R.id.colA:
                    sendMessage("#@~0");
                    break;
                case R.id.colB:
                    sendMessage("#@~1");
                         break;
                case R.id.colC:
                    sendMessage("#@~2");
                    break;
                case R.id.colD:
                    sendMessage("#@~3");
                    break;
                case R.id.colE:
                    sendMessage("#@~4");
                    break;
                case R.id.colF:
                    sendMessage("#@~5");
                    break;
                case R.id.colG:
                    sendMessage("#@~6");
                    break;
                default:
                    break;
            }

        }
        return false;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mConversationView = (ListView) view.findViewById(R.id.in);
        mOutEditText = (EditText) view.findViewById(R.id.edit_text_out);
        mSendButton = (Button) view.findViewById(R.id.button_send);
    }

    /**
     * Set up the UI and background operations for chat.
     */
    private void setupChat() {
//        Log.d("test", "setupChat()");

        // Initialize the array adapter for the conversation thread
        mConversationArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message);

        mConversationView.setAdapter(mConversationArrayAdapter);

        // Initialize the compose field with a listener for the return key
        mOutEditText.setOnEditorActionListener(mWriteListener);

        // Initialize the send button with a listener that for click events
        mSendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Send a message using content of the edit text widget
                View view = getView();
                if (null != view) {
                    TextView textView = (TextView) view.findViewById(R.id.edit_text_out);
                    String message = textView.getText().toString();
                    sendMessage(message);
                    mOutEditText.setEnabled(false);
                    mOutEditText.setEnabled(true);
                    rootView.setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                }
            }
        });

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothService(getActivity(), mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }

    /**
     * Makes this device discoverable for 300 seconds (5 minutes).
     */
    protected void ensureDiscoverable() {  // changed this from private to protected to access from ma
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            currentPlayer = BLACK;
            tvP1Name.setText(userName);
            isPlayer1 = true;
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 500);
            startActivity(discoverableIntent);
        }
    }

    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(getActivity(), "Not connected", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
            mOutEditText.setText(mOutStringBuffer);
        }
    }

    /**
     * The action listener for the EditText widget, to listen for the return key
     */
    private TextView.OnEditorActionListener mWriteListener
            = new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // If the action is a key-up event on the return key, send the message
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
                sendMessage(message);
            }
            return true;
        }
    };

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

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            FragmentActivity activity = getActivity();
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            setStatus("connected to " + mConnectedDeviceName);
                            mConversationArrayAdapter.clear();
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            setStatus("connecting ");
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            setStatus("not connected");
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
//                    Log.d("test", "writemessage: " + writeMessage);
//                    Log.d("test", "mconnectedDevice: " + mConnectedDeviceName);
//                    Log.d("test", "msg.tostring(): " + msg.toString());

                    //hijack the chat stream for connect4 moves
                    if(isMove(writeMessage)){
                        makeMove(Integer.parseInt(writeMessage.substring(writeMessage.length() - 1)));
                            lockBoard();
                            if(checkForWin()) {
                                chickenDinner();
                            }
                    }
                    else if (writeMessage.length() >= 4 && writeMessage.substring(0,3).equals("#@#")) {
                       Log.d("test", "send out my name: " + writeMessage.substring(3));
                    }
                    else {
                        mConversationArrayAdapter.add("Me:  " + (writeMessage));
                    }

                    break;

                case Constants.MESSAGE_READ:

                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Log.d("test", "writemessage: " + readMessage);
                    Log.d("test", "mconnectedDevice: " + mConnectedDeviceName);
//                    Log.d("test", "msg.tostring(): " + msg.toString());
                    if(isMove(readMessage)){
                        toggleTurn();
                        makeMove(Integer.parseInt(readMessage.substring(readMessage.length()-1)));
                        if(checkForWin()) {
                            chickenDinner();
                            paintHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    unlockBoard();
                                    toggleTurn();
                                }
                            },2500);
                        }
                        else {
                            unlockBoard();
                            toggleTurn();
                        }
                    }
                    else {
                        if(readMessage.equalsIgnoreCase("go")) {
                            long[] pattern = {0,80,100,80};
                            Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(pattern,-1);
                        }
                        else if (readMessage.length() >= 4 && readMessage.substring(0,3).equals("#@#")) {
                            opponentName += readMessage.substring(3);
                            setOpponentName(opponentName);
                            readMessage = opponentName + " is signed in";
                        }
                        if(opponentName.length() > 0) {
                            mConversationArrayAdapter.add(opponentName + ":  " + readMessage);
                        }
                        else {
                            mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                        }
                    }
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != activity) {
                        Toast.makeText(activity, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                        sendMyName();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
//                case Constants.MOVE_RECEIVE:
//                    Log.d("test", "MOVE_RECEIVE");
//                    byte[] receivedBuffer = (byte[]) msg.obj;
//                    String readMove = new String(receivedBuffer, 0, msg.arg1);////////////////////////////////////////////////////////////////////////
//                    Log.d("test", "received move: " + readMove);
//                    break;
//                case Constants.MOVE_SEND:
//                    Log.d("test","Move sent");
//                    break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occurred
//                    Log.d("test", "BT not enabled");
                    Toast.makeText(getActivity(), "BT not enabled",
                            Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
        }
    }

    /**
     * Establish connection with other device
     *
     * @param data   An {@link Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }

    public void connectPlayer1() {
        ensureDiscoverable();
        currentPlayer = BLACK;
        unlockBoard();
        buttons_view.setVisibility(View.GONE);
        scores_view.setVisibility(View.VISIBLE);
    }
    public void connectPlayer2(){
        currentPlayer = RED;
        tvP2Name.setText(userName);
        lockBoard();
        buttons_view.setVisibility(View.GONE);
        scores_view.setVisibility(View.VISIBLE);
        Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
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

    public boolean isMove(String str) {
        int strToInt = -1;
        if(str.isEmpty()){
            return false;
        }
        if(str.length() != 4) {
            Log.d("test", "length: " + str.length());
            return false;
        }
        String code = str.substring(0,3);
        if ( ! code.equals("#@~")) {
            Log.d("test", "code: " + code);
            return false;
        }
        str = str.substring(3);
        Log.d("test", "str: " + str);
        try {
            strToInt = Integer.parseInt(str);
        }catch (Exception e) {
            return false;
        }
        if ( strToInt < 0 || strToInt > 7) {
            return false;
        }
        return true;
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
//            return true;
        }
//        return false;
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
//                    chickenDinner();
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
//                        chickenDinner();
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
//                        chickenDinner();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void chickenDinner() {
//        Toast.makeText(getActivity(),"Player " + Integer.toString(currentPlayer) + " wins!", Toast.LENGTH_SHORT).show();
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

    public void sendMyName() {
        sendMessage("#@#" + userName);
    }

    public void setOpponentName(String opponentName) {
        if(isPlayer1) {
            tvP2Name.setText(opponentName);
        }
        else{
            tvP1Name.setText(opponentName);
        }


    }
}


