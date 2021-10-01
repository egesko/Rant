package com.example.androidapp;

import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    TextView helloWorld;
    Button click;
    EditText name;
    int count = 0;
    String personName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        click = (Button)findViewById(R.id.button);
        helloWorld = (TextView)findViewById(R.id.hello);
        name = (EditText)findViewById(R.id.name);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personName = name.getText().toString();
                if (personName.compareTo("Name") == 0) {
                    helloWorld.setText("Hello World! +" + ++count);
                } else {
                    helloWorld.setText("Hello " + personName + "! +" + ++count);
                }
            }
        });
    }
}