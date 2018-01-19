package com.example.vladmir.appto52.Controlleur;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vladmir.appto52.R;

import java.util.ArrayList;

/**
 * Created by vladmir on 12/01/18.
 */

public class CustomAdapter extends ArrayAdapter<String> {

    private Activity mContext;
    private ArrayList<String> mDevice;
    //private ArrayList<String> mRssi;

    //The ArrayAdapter constructor
    public CustomAdapter(Activity context, ArrayList<String> device, ArrayList<String> rssi, ArrayList<String> values) {
        super(context, R.layout.ble_device_adapter, values);
       mContext= context;
        //Set the value of variables
        mDevice = device;
        //mRssi = rssi;
    }

    //Here the ListView will be displayed
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View layoutView = mContext.getLayoutInflater().inflate(R.layout.ble_device_adapter, null, true);
        TextView mDeviceNameView = (TextView) layoutView.findViewById(R.id.Device_name_textview);
       // TextView mDeviceRssiView = (TextView) layoutView.findViewById(R.id.Device_rssi_textview);
        mDeviceNameView.setText(mDevice.get(position));
        //mDeviceRssiView.setText(mRssi.get(position));
        return layoutView;
    }
}