package com.coba.printapps.feature;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.coba.printapps.R;
import com.zj.btsdk.BluetoothService;

public class DeviceActivity extends AppCompatActivity {
    TextView title_paired_devices,title_new_devices;
    ListView paired_devices,new_devices;
    Button button_scan;

    public static final String EXTRA_DEVICE_ADDRESS = "device_address";
    private BluetoothService mService = null;
    private ArrayAdapter<String> newDeviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        title_paired_devices = findViewById(R.id.title_paired_devices);
        title_new_devices = findViewById(R.id.title_new_devices);
        paired_devices = findViewById(R.id.paired_devices);
        new_devices = findViewById(R.id.new_devices);
        button_scan = findViewById(R.id.button_scan);
    }

    @Override
    protected void onStart(){
        super.onStart();
        button_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DeviceActivity.this, "Under Maintenance", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    newDeviceAdapter.add(device.getName() + "\n" + device.getAddress());
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    setTitle("Pilih Perangkat");
                    if (newDeviceAdapter.getCount() == 0) {
                        newDeviceAdapter.add("Perangkat tidak ditemukan");
                    }
                }
            }
        }
    };

}
