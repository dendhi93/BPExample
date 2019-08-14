package com.coba.printapps.feature;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.coba.printapps.R;
import com.coba.printapps.utils.BluetoothHandler;
import com.coba.printapps.utils.PrinterCommands;
import com.zj.btsdk.BluetoothService;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, BluetoothHandler.HandlerInterface {
    TextView et_text, tv_status;
    Button btn_print_image, btn_print_text;
    ImageView imageView;

    private final String TAG = MainActivity.class.getSimpleName();
    public static final int RC_BLUETOOTH = 0;
    public static final int RC_CONNECT_DEVICE = 1;
    public static final int RC_ENABLE_BLUETOOTH = 2;

    private BluetoothService mService = null;
    private boolean isPrinterReady = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_text = findViewById(R.id.et_text);
        tv_status = findViewById(R.id.tv_status);
        btn_print_image = findViewById(R.id.btn_print_image);
        btn_print_text = findViewById(R.id.btn_print_text);
        imageView = findViewById(R.id.imageView);
    }

    @Override
    protected void onStart(){
        super.onStart();
        initBluetooth();
        btn_print_image.setVisibility(View.GONE);
        btn_print_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPrinterReady) {
                    Toast.makeText(getApplicationContext(), "Under Maintenance", Toast.LENGTH_LONG);
                }else{ Toast.makeText(getApplicationContext(), "Belum terhubung ke Perangkat", Toast.LENGTH_LONG); }
            }
        });

        btn_print_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mService.isAvailable()) {
                    tv_status.setText("perangkat tidak support bluetooth");
                }else if (et_text.getText().toString().isEmpty()){
                    tv_status.setText("mohon diisi data yang kosong");
                }else{
                    if (isPrinterReady){
                        mService.write(PrinterCommands.ESC_ALIGN_CENTER);
                        mService.sendMessage(et_text.getText().toString(), "");
                        mService.write(PrinterCommands.ESC_ENTER);
                    }else{ printerNotReady(); }
                }
            }
        });

    }

    private void printerNotReady(){
        if (mService.isBTopen()){
            startActivityForResult(new Intent(this, DeviceActivity.class), RC_CONNECT_DEVICE);
        }
        else{requestBluetooth();}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RC_ENABLE_BLUETOOTH:
                if (resultCode == RESULT_OK) {
                    Log.i(TAG, "onActivityResult: bluetooth aktif");
                } else
                    Log.i(TAG, "onActivityResult: bluetooth harus aktif untuk menggunakan fitur ini");
                break;
            case RC_CONNECT_DEVICE:
                if (resultCode == RESULT_OK) {
                    String address = data.getExtras().getString(DeviceActivity.EXTRA_DEVICE_ADDRESS);
                    BluetoothDevice mDevice = mService.getDevByMac(address);
                    mService.connect(mDevice);
                }
                break;
        }
    }

    @AfterPermissionGranted(RC_BLUETOOTH)
    private void initBluetooth() {
        String[] params = {Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN};
        if (!EasyPermissions.hasPermissions(this, params)) {
            EasyPermissions.requestPermissions(this, "You need bluetooth permission",
                    RC_BLUETOOTH, params);
            return;
        }
        mService = new BluetoothService(this, new BluetoothHandler(this));
    }


    @Override
    public void onDeviceConnected() {
        isPrinterReady = true;
        tv_status.setText("Terhubung dengan perangkat");
    }

    @Override
    public void onDeviceConnecting() { tv_status.setText("Sedang menghubungkan..."); }

    @Override
    public void onDeviceConnectionLost() {
        isPrinterReady = false;
        tv_status.setText("Koneksi perangkat terputus");
    }

    private void requestBluetooth() {
        if (mService != null) {
            if (!mService.isBTopen()) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, RC_ENABLE_BLUETOOTH);
            }
        }
    }

    @Override
    public void onDeviceUnableToConnect() {tv_status.setText("Tidak dapat terbuhubung ke perangkat"); }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) { }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) { }
}
