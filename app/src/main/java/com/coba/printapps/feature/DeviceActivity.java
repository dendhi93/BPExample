package com.coba.printapps.feature;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.coba.printapps.R;
import com.zj.btsdk.BluetoothService;

import java.util.Set;

public class DeviceActivity extends AppCompatActivity {
    TextView title_paired_devices,title_new_devices;
    ListView lvPaired_devices,lvNew_devices;
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
        lvPaired_devices = findViewById(R.id.paired_devices);
        lvNew_devices = findViewById(R.id.new_devices);
        button_scan = findViewById(R.id.button_scan);
    }

    @Override
    protected void onStart(){
        super.onStart();
        mService = new BluetoothService(this, null);
        Set<BluetoothDevice> pairedDevice = mService.getPairedDev();

        button_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doDiscovery();
                button_scan.setVisibility(View.GONE);
            }
        });

        ArrayAdapter<String> pairedDeviceAdapter = new ArrayAdapter<>(this, R.layout.list_item);
        lvPaired_devices.setAdapter(pairedDeviceAdapter);
        lvPaired_devices.setOnItemClickListener(mDeviceClickListener);

        ArrayAdapter<String> newDeviceAdapter = new ArrayAdapter<>(this, R.layout.list_item);
        lvNew_devices.setAdapter(newDeviceAdapter);
        lvNew_devices.setOnItemClickListener(mDeviceClickListener);

        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, intentFilter);
        intentFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, intentFilter);

        if (pairedDevice.size() > 0) {
            title_paired_devices.setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevice) {
                pairedDeviceAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            String noDevice = "Tidak ada perangkat terhubung!";
            pairedDeviceAdapter.add(noDevice);
            button_scan.setVisibility(View.VISIBLE);
        }
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

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mService.cancelDiscovery();

            String info = ((TextView) view).getText().toString();
            String address = info.substring(info.length() - 17);

            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

            setResult(RESULT_OK, intent);
            finish();
        }
    };

    private void doDiscovery() {
        setTitle("Mencari perangkat...");
        title_new_devices.setVisibility(View.VISIBLE);

        if (mService.isDiscovering()) {
            mService.cancelDiscovery();
        }

        mService.startDiscovery();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            mService.cancelDiscovery();
        }
        mService = null;
        unregisterReceiver(mReceiver);
    }

}
