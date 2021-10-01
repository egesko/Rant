package com.example.androidappdaman;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;


/**
 * @author Damandeep Riat
 * Simple Android Application that shows 5 different life quotes. Displays a button that must be clicked in order to cycle
 * through the different quotes.
 */
public class MainActivity extends AppCompatActivity {


    private Button click; // button attribute
    private TextView display; // textbook
    /**
     * Holds the 5 quotes in a string array
     */
    private String[] sentence = {"The only true wisdom is in knowing you know nothing.", "It's better to be a lion for a day than a sheep all your life.", "Be happy. It's one way of being wise.", "It requires wisdom to understand wisdom: the music is nothing if the audience is deaf.", "If the world were perfect, it wouldn't be."};
    private int index = 0; // keeps track of position within the array


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* Set instance vars with appropriate elements */
        click = (Button) findViewById(R.id.b111);
        display = (TextView) findViewById(R.id.b101);

        /**
         * Code that runs after each button click
         */
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index < 5) { // cycle until array bounds reached
                    display.setText(sentence[index]);
                    index++; //iterate
                    System.out.println(index);
                } else { // restart at zero
                    index = 0;
                    display.setText(sentence[index]);
                    index++; //iterate

                }


            }

        });
    }
}