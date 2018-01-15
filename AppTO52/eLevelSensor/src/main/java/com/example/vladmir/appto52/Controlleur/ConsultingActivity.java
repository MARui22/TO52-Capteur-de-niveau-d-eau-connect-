package com.example.vladmir.appto52.Controlleur;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

//import com.example.vladmir.appto52.Controlleur.CuveDataSource;
import com.example.vladmir.appto52.Modele.Cuve;
import com.example.vladmir.appto52.R;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static android.bluetooth.BluetoothGattCharacteristic.PERMISSION_READ;
import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_NOTIFY;
import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_READ;
import static android.bluetooth.BluetoothGattService.SERVICE_TYPE_PRIMARY;


public class ConsultingActivity extends AppCompatActivity {
    private static final String TAG = "ERREUR";
    Context context;
    TextView eTemperature,eProfondeur;
    ListView archiveList;
    String Device_Adress;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothGatt bluetoothGatt;
    BluetoothDevice Device;
    Cuve cuve;
    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";

   UUID SERVICE_UUID = UUID.fromString("000fff0-0000-1000-8000-00805f9b34fb");
    UUID CHARACTERISTIC_DEPTH = UUID.fromString("000fff4-0000-1000-8000-00805f9b34fb");

    UUID DESCRIPTOR_CONFIG_UUID = convertFromInteger(0x2902);
    private CuveDataSource datasource;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulting2);
        Intent intent = getIntent();
        context = this;
        datasource = new CuveDataSource(this);
        datasource.open();
        cuve = new Cuve();
        Device = getIntent().getExtras().getParcelable("btdevice");
        Device_Adress = intent.getStringExtra("Adress");
        eTemperature = (TextView) findViewById(R.id.temptextview) ;
        eProfondeur = (TextView) findViewById(R.id.profondeur_textview) ;
        archiveList= (ListView)findViewById(R.id.bdd_listview);

        connectToDeviceSelected(Device_Adress,Device);
    }

    //Fonction de traitement des messages reçu
    void Traitement_Message(String valeur)
    {
        float temp, prof;
        String date ;
        float temp1=0,prof1=0;
        float temp2= 0,prof2=0;
        int a = 0;
        int tab[] = new int [5];

        for(int i = 0; i<valeur.length();i++)
        {
            if (valeur.charAt(i)==';')
            {
                tab[a]= i;
                a++;
            }
        }
        //recuperation de la partie entière de la température
       int b =  tab[0]-1;
        int b1= 0;
        while(b>=0)
        {
            temp1 += (Character.getNumericValue(valeur.charAt(b)))*Math.pow(10,b1);
            b1++;
                    b--;
        }

        //recuperation de la partie decimale de la température
        int c =  tab[1]-1;
        int c1= 0;
        while(c>tab[0])
        {
            temp2 += (Character.getNumericValue(valeur.charAt(c)))*Math.pow(10,c1);
            c1++;
            c--;
        }
        //recuperation de la partie entiere de la profondeur
        int d =  tab[2]-1;
        int d1= 0;
        while(d>tab[1])
        {
            prof1 += (Character.getNumericValue(valeur.charAt(d)))*Math.pow(10,d1);
            d1++;
            d--;
        }
        //recuperation de la partie decimale de la profondeur
        int e =  tab[3]-1;
        int e1= 0;
        while(e>tab[2])
        {
            prof2 += (Character.getNumericValue(valeur.charAt(e)))*Math.pow(10,e1);
            e1++;
            e--;
        }

        temp = temp1+ (temp2/100);
        prof= prof1 + (prof2/100);
        date = "13/01/2018";
        cuve.setmDate(date);
        cuve.setmProfondeur(prof);
        cuve.setmTemperature(temp);
        //showMessage("Test",Float.toString(cuve.getmTemperature()));

        eTemperature.setText(Float.toString(cuve.getmTemperature()));
        eProfondeur.setText(Float.toString(cuve.getmProfondeur()));
        datasource.insertCuveData(cuve);
        viewArchivesList();
           }
    public UUID convertFromInteger(int i) {
        final long MSB = 0x0000000000001000L;
        final long LSB = 0x800000805f9b34fbL;
        long value = i & 0xFFFFFFFF;
        return new UUID(MSB | (value << 32), LSB);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void connectToDeviceSelected(String address, BluetoothDevice bluetoothDevice) {

       //
        bluetoothGatt = bluetoothDevice.connectGatt(this, false, btleGattCallback);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void disconnectDeviceSelected() {
        bluetoothGatt.disconnect();
    }

    // Device connect call back
    private final BluetoothGattCallback btleGattCallback = new BluetoothGattCallback() {

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            // this will get called anytime you perform a read or write characteristic operation
            ConsultingActivity.this.runOnUiThread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
                public void run() {
                    readCounterCharacteristic(characteristic);
                }
            });
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
            // this will get called when a device connects or disconnects
            System.out.println(newState);
            switch (newState) {
                case 0:
                    ConsultingActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            //showMessage("Deconnection","device disconnected\n");

                        }
                    });
                    break;
                case 2:
                    ConsultingActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                           // showMessage("Connection","device connected\n");
                            createService();
                        }
                    });

                    // discover services and characteristics for this device
                    bluetoothGatt.discoverServices();

                    break;
                default:
                    ConsultingActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                           showMessage("Error","we encounterned an unknown state, uh oh\n");
                        }
                    });
                    break;
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, final int status) {
            // this will get called after the client initiates a 			BluetoothGatt.discoverServices() call
            ConsultingActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                   // showMessage("Succes","device services have been discovered\n");
                }
            });
            // Get the counter characteristic
            BluetoothGattCharacteristic characteristic = gatt
                    .getService(SERVICE_UUID)
                    .getCharacteristic(CHARACTERISTIC_DEPTH);

            // Enable notifications for this characteristic locally
            gatt.setCharacteristicNotification(characteristic, true);

            // Write on the config descriptor to be notified when the value changes
            BluetoothGattDescriptor descriptor =
                    characteristic.getDescriptor(DESCRIPTOR_CONFIG_UUID);
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            gatt.writeDescriptor(descriptor);

            displayGattServices(bluetoothGatt.getServices());
        }
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onDescriptorWrite(BluetoothGatt gatt,
                                      BluetoothGattDescriptor descriptor, int status) {
            if (DESCRIPTOR_CONFIG_UUID.equals(descriptor.getUuid())) {
                BluetoothGattCharacteristic characteristic = gatt
                        .getService(SERVICE_UUID)
                        .getCharacteristic(CHARACTERISTIC_DEPTH);
                gatt.readCharacteristic(characteristic);
            }
        }
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        // Result of a characteristic read operation
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {


            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
                readCounterCharacteristic(characteristic);
            }
        }
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        private void readCounterCharacteristic(BluetoothGattCharacteristic
                                                       characteristic) {
            if (CHARACTERISTIC_DEPTH.equals(characteristic.getUuid())) {

                byte[] data = characteristic.getValue();
                String messageString = null;
                try {
                    messageString = new String(data, "UTF-8");
                    Traitement_Message(messageString);

                    //db.insertCuveData(cuve);
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "Unable to convert message bytes to string");
                }

            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {

        System.out.println(characteristic.getUuid());
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {

            final String uuid = gattService.getUuid().toString();
            System.out.println("Service discovered: " + uuid);
            ConsultingActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    //if (uuid.equals(SERVICE_UUID.toString()))

                    //peripheralTextView.append("Service discovered: "+uuid+"\n");

                }
            });
            new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic :
                    gattCharacteristics) {

                final String charUuid = gattCharacteristic.getUuid().toString();
                System.out.println("Characteristic discovered for service: " + charUuid);
                ConsultingActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                       // if (charUuid.equals(CHARACTERISTIC_DEPTH.toString()))
                       // peripheralTextView.append("Characteristic discovered for service: "+charUuid+"\n");
                    }
                });

            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private BluetoothGattService createService() {
        BluetoothGattService service = new BluetoothGattService(SERVICE_UUID, SERVICE_TYPE_PRIMARY);

        // Counter characteristic (read-only, supports subscriptions)
        BluetoothGattCharacteristic counter = new BluetoothGattCharacteristic(CHARACTERISTIC_DEPTH, PROPERTY_READ | PROPERTY_NOTIFY, PERMISSION_READ);
        //BluetoothGattDescriptor counterConfig = new BluetoothGattDescriptor(DESCRIPTOR_CONFIG_UUID, PERMISSION_READ | PERMISSION_WRITE);
        //counter.addDescriptor(counterConfig);

        service.addCharacteristic(counter);

        return service;
    }

    public void showMessage(String title, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(true)
                .setTitle(title)
                .setMessage(message)
                .show();

    }
    private void viewArchivesList()
    {

        Cursor data = datasource.getCuveData();

        if(data.getCount()==0)
        {
            showMessage("Error","aucune donnée disponible");
            return;

        }
        ToDoCursorAdapter adapter = new ToDoCursorAdapter(this, data);
        archiveList.setAdapter(adapter);


    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onPause() {
        datasource.close();
        if (bluetoothGatt == null) {
            return;
        }
        bluetoothGatt.close();
        bluetoothGatt = null;
        super.onPause();
    }
}
