package com.example.myfirstapp;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //Create Objects
    Button mainButton;
    TextView message;
    String[] messages = {"Hello World!", "Γειά σου Κόσμε!", "你好世界！", "안녕하세요!", "Ciao mondo!", "Привет мир!", "Hola Mundo!", "مرحبا بالعالم!"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign objects to UI
        mainButton = (Button)findViewById(R.id.mainButton);
        message = (TextView)findViewById(R.id.message);

        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //Randomly changes text from message array when button clicked
            public void onClick(View v) {
                int randMessage = new Random().nextInt(messages.length);
                message.setText(messages[randMessage]);
            }
        });
    }
}