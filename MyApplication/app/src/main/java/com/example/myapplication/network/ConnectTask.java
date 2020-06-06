package com.example.myapplication.network;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;

import com.example.myapplication.MainView;

import java.io.IOException;
import java.util.UUID;

public class ConnectTask extends AsyncTask<Void, Void, Boolean> {

    private BluetoothSocket mBluetoothSocket = null;
    private BluetoothDevice mBluetoothDevice = null;
    private static final String TAG = "BluetoothClient";
    private MainView view;
    private ConnectedTask mConnectedTask;

    public ConnectTask(BluetoothDevice bluetoothDevice, MainView view) {
        mBluetoothDevice = bluetoothDevice;
        this.view = view;
//        mConnectedDeviceName = bluetoothDevice.getName();

        //SPP
        UUID uuid = UUID.fromString("00001108-0000-1000-8000-00805f9b34fb");

        try {
            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(uuid);
//            Log.d( TAG, "create socket for "+mConnectedDeviceName);

        } catch (IOException e) {
            Log.e( TAG, "socket create failed " + e.getMessage());
        }

//        mConnectionStatus.setText("connecting...");
    }


    @Override
    protected Boolean doInBackground(Void... params) {

        // Always cancel discovery because it will slow down a connection
        view.cancelDiscover();

        // Make a connection to the BluetoothSocket
        try {
            // This is a blocking call and will only return on a
            // successful connection or an exception
            mBluetoothSocket.connect();
        } catch (IOException e) {
            // Close the socket
            try {
                mBluetoothSocket.close();
            } catch (IOException e2) {
                Log.e(TAG, "unable to close() " +
                        " socket during connection failure", e2);
            }

            return false;
        }

        return true;
    }


    @Override
    protected void onPostExecute(Boolean isSucess) {

        if ( isSucess ) {
            mConnectedTask = new ConnectedTask(mBluetoothSocket, mBluetoothDevice.getName());
            mConnectedTask.execute();
        }
        else{

//            isConnectionError = true;
//            Log.d( TAG,  "Unable to connect device");
//            showErrorDialog("Unable to connect device");
        }
    }

    public void write(String msg){
        mConnectedTask.write(msg);
    }
}