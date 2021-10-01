package com.example.experiment1;

import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    int myInteger = 0;
    Button myButton;
    TextView myTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myTextView = findViewById(R.id.textview1);
        myButton = findViewById(R.id.button1);

        myButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                // What we want to display when button is clicked
                myTextView.setText(String.valueOf(myInteger));
                myInteger= myInteger + 1;
            }
        });


    }
}