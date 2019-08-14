package com.coba.printapps.feature;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.coba.printapps.R;

public class DeviceActivity extends AppCompatActivity {
    TextView title_paired_devices,title_new_devices;
    ListView paired_devices,new_devices;
    Button button_scan;

    public static final String EXTRA_DEVICE_ADDRESS = "device_address";

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
}
