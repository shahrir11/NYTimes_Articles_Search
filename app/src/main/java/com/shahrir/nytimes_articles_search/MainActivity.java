package com.shahrir.nytimes_articles_search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;

// This class Launches Home Screen of the App
public class MainActivity extends AppCompatActivity {

    ImageButton nybutton;


    // Android App starting point
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        nybutton = findViewById(R.id.launchbutton);

        //adding a listener to application launch button

        nybutton.setOnClickListener(v ->{

            Intent intnt = new Intent(v.getContext(), nytimes_main.class);
            startActivity(intnt);

           } );

    }
}
