package com.shahrir.nytimes_articles_search;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


// this activity loads  saved articles into Listview

public class nytimes_articles_saved extends AppCompatActivity {

    protected boolean isTablet;
    ListView listvw1;
    nytimes_search_fragment messageFragment;
    nytimes_DataBaseHelper databasebOpener;
    public static SQLiteDatabase db;
    nytimes_articles_fetch fetcharticle;

    //initializing for both ph and tablets

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newyorktimes_saved_articles);
        listvw1 = (ListView) findViewById(R.id.listviewer1);

        nytimes_articles_activity.createAdapter(nytimes_articles_saved.this);
        listvw1.setAdapter(nytimes_articles_activity.savedAdapter);
        databasebOpener = new nytimes_DataBaseHelper(this);
        db = databasebOpener.getWritableDatabase();
        isTablet = (findViewById(R.id.fragmentLocation) != null);
        listvw1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle newBundle = new Bundle();
                newBundle.putString("headline", nytimes_articles_activity.savedAdapter.getItem(position).getFetchheadline());
                newBundle.putString("url", nytimes_articles_activity.savedAdapter.getItem(position).getFetchurl());
                newBundle.putString("pic_url", nytimes_articles_activity.savedAdapter.getItem(position).getFetchthumbnail());
                newBundle.putInt("id", nytimes_articles_activity.savedAdapter.getPosition(nytimes_articles_activity.savedAdapter.getItem(position)));


                if (isTablet) {
                    messageFragment = new nytimes_search_fragment(nytimes_articles_saved.this);
                    messageFragment.setArguments(newBundle);


                    //Error: wrong 2nd argument type found
                    //Solutiom: https://stackoverflow.com/questions/30339524/obj-fragment-wrong-2nd-argument-type-found-android-support-v4-app-fragment-re


                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragmentLocation,messageFragment).commit();


                } else {
                    Intent msgDetailsIntent = new Intent(getApplicationContext(), nytimes_articles_expanded.class);
                    msgDetailsIntent.putExtras(newBundle);
                    startActivityForResult(msgDetailsIntent, CONTEXT_INCLUDE_CODE);
                }
            }
        });

    }
    public void deleteItem(int id) {
        db.delete(nytimes_DataBaseHelper.TABLE_NAME, nytimes_DataBaseHelper.COL_ID + "=" + id, null);
        nytimes_articles_fetch position = nytimes_articles_activity.savedAdapter.getItem(id);
        nytimes_articles_activity.saved_Articles.remove(position);

        nytimes_articles_activity.savedAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == CONTEXT_INCLUDE_CODE) {
            int msgID = data.getIntExtra("msgID", -1);
            deleteItem(msgID);
        }
    }
























}
