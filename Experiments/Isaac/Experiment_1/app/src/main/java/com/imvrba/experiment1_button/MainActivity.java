package com.imvrba.experiment1_button;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //called when pressing hello world button
    public void sendHello(View view) {
        TextView text = findViewById(R.id.textView);
        text.setText("Hello World");

    }

}