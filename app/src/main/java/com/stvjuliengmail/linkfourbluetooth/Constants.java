package com.stvjuliengmail.linkfourbluetooth;

/**
 * Created by Steven on 8/2/2017.
 */

public interface Constants {
    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MOVE_SEND = 6;
    public static final int MOVE_RECEIVE = 7;
    public static final int MESSAGE_USER_NAME = 8;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
}
