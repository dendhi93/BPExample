package com.coba.printapps.feature;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.coba.printapps.R;

public class DeviceActivity extends AppCompatActivity {
    TextView title_paired_devices,title_new_devices;
    ListView paired_devices,new_devices;

    public static final String EXTRA_DEVICE_ADDRESS = "device_address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        title_paired_devices = findViewById(R.id.title_paired_devices);
        title_new_devices = findViewById(R.id.title_new_devices);
        paired_devices = findViewById(R.id.paired_devices);
        new_devices = findViewById(R.id.new_devices);
    }
}
