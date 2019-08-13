package com.coba.printapps.feature;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.coba.printapps.R;

public class MainActivity extends AppCompatActivity {
    TextView et_text, tv_status;
    Button btn_print_image;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_text = findViewById(R.id.et_text);
        tv_status = findViewById(R.id.tv_status);
        btn_print_image = findViewById(R.id.btn_print_image);
        imageView = findViewById(R.id.imageView);
    }

    @Override
    protected void onStart(){
        btn_print_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


}
