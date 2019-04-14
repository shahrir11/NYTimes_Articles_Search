package com.shahrir.nytimes_articles_search;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.ArrayList;


//This class opens links to read new article

public class nytimes_articles_activity extends AppCompatActivity {

    nytimes_DataBaseHelper dbOpener;
    public static SQLiteDatabase db;

    WebView webView;
    Toolbar toolbar;

    Cursor savedresult;
    MenuInflater mnuinflater;

    String url_string;
    String title_header;

    public static String url_picture;

    long newId;

    ContentValues newRowValues;
    nytimes_articles_fetch article;

    public static ArrayList<nytimes_articles_fetch> saved_Articles = new ArrayList<nytimes_articles_fetch>();
    public static nytimes_saved_arrayops savedAdapter;



    //initializing to get the URL string from nytimes_articles_fetch class.

    public static void createAdapter(Context cnt) {
        savedAdapter = new nytimes_saved_arrayops(cnt, saved_Articles);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nytimes_activity_articles);
        toolbar = findViewById(R.id.toolbarmenu);
        setSupportActionBar(toolbar);
        savedAdapter = new nytimes_saved_arrayops(this, saved_Articles);
        article = (nytimes_articles_fetch) getIntent().getSerializableExtra("article");
        webView = findViewById(R.id.article_web_view);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.loadUrl(article.getFetchurl());
    }

//inflating menu with about icon

    @Override
    public boolean onCreateOptionsMenu(Menu menuitem) {
        mnuinflater = getMenuInflater();
        mnuinflater.inflate(R.menu.newyorktimes_menu_search, menuitem);
        return true;
    }

//inflater for App information

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.info_details:
                infoDialog();
                break;
            case R.id.save_icon:
                onSaveIcon();
                Toast.makeText(this, getResources().getString(R.string.article_saving), Toast.LENGTH_LONG).show();
                break;
        }
        return false;

    }

  //inflating About menu
    public void infoDialog() {
        View middle_info = getLayoutInflater().inflate(R.layout.nytimes_activity_showdialoginfo, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("").setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                onPause();
            }
        }).setView(middle_info);
        builder.create().show();

    }

    public void onSaveIcon() {

        dbOpener = new nytimes_DataBaseHelper(this);
        db = dbOpener.getWritableDatabase();

        String[] columns = {nytimes_DataBaseHelper.COL_ID, nytimes_DataBaseHelper.COL_HEADER, nytimes_DataBaseHelper.COL_URL, nytimes_DataBaseHelper.COL_PIC_URL};
        savedresult = db.query(false, nytimes_DataBaseHelper.TABLE_NAME, columns, null, null, null, null, null, null);

        title_header = article.getFetchheadline();
        url_string = article.getFetchurl();
        url_picture = article.getFetchthumbnail();


        newRowValues = new ContentValues();

        newRowValues.put(nytimes_DataBaseHelper.COL_HEADER, title_header);
        newRowValues.put(nytimes_DataBaseHelper.COL_URL, url_string);
        newRowValues.put(nytimes_DataBaseHelper.COL_PIC_URL, url_picture);
        newId = db.insert(nytimes_DataBaseHelper.TABLE_NAME, null, newRowValues);
        saved_Articles.add(article);
        savedAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }


}
