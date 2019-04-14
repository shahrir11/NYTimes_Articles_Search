package com.shahrir.nytimes_articles_search;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;


//exapanded via fragment

public class nytimes_articles_expanded extends AppCompatActivity {


     FrameLayout framelayout;
     nytimes_search_fragment messagefragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        framelayout = new FrameLayout(this);
        framelayout.setId(R.id.fragmentLocation);
        setContentView(framelayout);


        messagefragment = new nytimes_search_fragment();
        messagefragment.setArguments(getIntent().getExtras());


        //Error: wrong 2nd argument type found
        //Solutiom: https://stackoverflow.com/questions/30339524/obj-fragment-wrong-2nd-argument-type-found-android-support-v4-app-fragment-re

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentLocation,messagefragment).commit();

    }















}
