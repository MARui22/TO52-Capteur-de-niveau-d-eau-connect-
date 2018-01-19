package com.example.vladmir.appto52.Controlleur;
        import android.Manifest;
        import android.annotation.TargetApi;
        import android.app.Activity;
        import android.app.AlertDialog;
        import android.bluetooth.BluetoothAdapter;
        import android.bluetooth.BluetoothDevice;
        import android.bluetooth.BluetoothManager;
        import android.bluetooth.le.BluetoothLeScanner;
        import android.bluetooth.le.ScanCallback;
        import android.bluetooth.le.ScanResult;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.os.AsyncTask;
        import android.os.Build;
        import android.support.annotation.RequiresApi;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.Button;
        import android.widget.ListView;
        import android.widget.TextView;

        import com.example.vladmir.appto52.R;

        import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
        BluetoothManager btManager;
        BluetoothAdapter btAdapter;
        BluetoothLeScanner btScanner;
        Button startScanningButton;
        Button stopScanningButton;
        Context context;
        Activity activity;
        TextView Noteview;
         ListView peripheralListView;
         String selectedDevice;
        private ArrayList<String> names;
        private ArrayList<String> rssi;
        private ArrayList<String> addresses;
        private final static int REQUEST_ENABLE_BT = 1;
        private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main2);
            context= this;
            activity= this;
            names = new ArrayList<String>();
            rssi = new ArrayList<String>();
            addresses = new ArrayList<String>();
            //Noteview = (TextView) findViewById(R.id.View_txt);
            peripheralListView = (ListView) findViewById(R.id.PeripheralListView);

            startScanningButton = (Button) findViewById(R.id.StartScanButton);
            startScanningButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startScanning();
                }
            });

            stopScanningButton = (Button) findViewById(R.id.StopScanButton);
            stopScanningButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    stopScanning();
                }
            });
            stopScanningButton.setVisibility(View.INVISIBLE);

            btManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
            btAdapter = btManager.getAdapter();
            btScanner = btAdapter.getBluetoothLeScanner();


            if (btAdapter != null && !btAdapter.isEnabled()) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent,REQUEST_ENABLE_BT);
            }

            // Make sure we have access coarse location enabled, if not, prompt the user to enable it
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect peripherals.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
            }



            peripheralListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
                    selectedDevice = (String) peripheralListView.getItemAtPosition(position);
                    Intent intent = new Intent(MainActivity.this, ConsultingActivity.class);
                    BluetoothDevice device = btAdapter.getRemoteDevice(selectedDevice);
                    intent.putExtra("Adress",selectedDevice);
                    intent.putExtra("btdevice", device);
                    startActivity(intent);
                }
            });
        }

        // Device scan callback.
        private ScanCallback leScanCallback = new ScanCallback() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                if (result.getDevice().getName()==null)
                    names.add("NULL");
                else

            names.add(result.getDevice().getName());
            rssi.add(Integer.toString(result.getRssi()));
            addresses.add(result.getDevice().getAddress());

            CustomAdapter adapter = new CustomAdapter(activity, names, rssi, addresses);
            peripheralListView.setAdapter(adapter);


            }
        };



        public void startScanning() {
            System.out.println("start scanning");
            startScanningButton.setVisibility(View.INVISIBLE);
            stopScanningButton.setVisibility(View.VISIBLE);
            AsyncTask.execute(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    btScanner.startScan(leScanCallback);
                }
            });
        }

        public void stopScanning() {
            System.out.println("stopping scanning");
            startScanningButton.setVisibility(View.VISIBLE);
            stopScanningButton.setVisibility(View.INVISIBLE);
            AsyncTask.execute(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    btScanner.stopScan(leScanCallback);
                }
            });
        }


    /*public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }*/


}