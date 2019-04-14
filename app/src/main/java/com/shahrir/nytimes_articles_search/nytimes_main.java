package com.shahrir.nytimes_articles_search;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

 //The nytimes_main class is the main activity of the application


public class nytimes_main extends AppCompatActivity {

    ProgressBar progressionBar;
    EditText edittextQuery;
    ListView listviewResults;

    ArrayList<nytimes_articles_fetch> arrarticles;
    Button btnsavedlist;
    nytimes_articles_arrayops opsadapter;

    private static final String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
    private static final String API_KEY = "nGhORsp4W6LhNZnA1DtcYdeVv2Kp0l8r";

    Snackbar snackbar;
    Intent intent;

    SharedPreferences sp;

    Intent sl_Intent;
    AsyncHttpClient asynclient;
    String qry;
    RequestParams params;
    nytimes_DataBaseHelper databaseOpener;
    public static SQLiteDatabase db;
    nytimes_articles_fetch fetcharticle;


  //initializing main class
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nytimes_activity_search);

        //obtain a database:
        databaseOpener = new nytimes_DataBaseHelper(this);
        db = databaseOpener.getWritableDatabase();

        //query the results
        String[] columns = {nytimes_DataBaseHelper.COL_ID, nytimes_DataBaseHelper.COL_HEADER, nytimes_DataBaseHelper.COL_URL, nytimes_DataBaseHelper.COL_PIC_URL};
        Cursor results_saved = db.query(false, nytimes_DataBaseHelper.TABLE_NAME, columns, null, null, null, null, null, null);

        while(results_saved.moveToNext())
        {
            int headerindex = results_saved.getColumnIndex(nytimes_DataBaseHelper.COL_HEADER);
            String header = results_saved.getString(headerindex);
            int urlindex = results_saved.getColumnIndex(nytimes_DataBaseHelper.COL_URL);
            String url = results_saved.getString(urlindex);
            int urlpictureindex = results_saved.getColumnIndex(nytimes_DataBaseHelper.COL_PIC_URL);
            String urlpicture = results_saved.getString(urlpictureindex);
            //add the new Contact to the array list:
            fetcharticle = new nytimes_articles_fetch(  url,header, urlpicture );
            nytimes_articles_activity.saved_Articles.add(fetcharticle);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_search);
        edittextQuery = (EditText) findViewById(R.id.qrytxt);
        btnsavedlist = (Button) findViewById(R.id.savethearticle);
        listviewResults = (ListView) findViewById(R.id.result_list_viewer);
        sp = getSharedPreferences("searchQuery", Context.MODE_PRIVATE);
        String searchText = sp.getString("searchQuery", "");
        edittextQuery.setText(searchText);

        progressionBar = findViewById(R.id.progressionbar);
        arrarticles = new ArrayList<>();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        ForecastQuery networkThreadOne = new ForecastQuery();

        opsadapter = new nytimes_articles_arrayops(this, arrarticles);

        sl_Intent = new Intent(nytimes_main.this, nytimes_articles_saved.class);
        listviewResults.setAdapter(opsadapter);

        snackbar = Snackbar.make(toolbar, getResources().getString(R.string.nytimes_welcome), Snackbar.LENGTH_LONG);snackbar.show();


        listviewResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(getApplicationContext(), nytimes_articles_activity.class);
                fetcharticle = arrarticles.get(position);
                alertDialog();
            }
        });

        listviewResults.setOnScrollListener(new nytimes_scrollops() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                loadMoreData(page);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });

        btnsavedlist.setOnClickListener(btn -> {
            startActivity(sl_Intent);
        });
    }

   //define the search of articles and retrieving them from the API using AsyncHttpClient and JSON object,with exception handling of the results


    private void loadMoreData(int offset) {
        qry = edittextQuery.getText().toString();
        try {
            URLEncoder.encode(qry, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, getResources().getString(R.string.article_searching), Toast.LENGTH_LONG).show();
        ForecastQuery networkThreadOne = new ForecastQuery();
        asynclient = new AsyncHttpClient();
        params = new RequestParams();
        params.put("api-key", API_KEY);
        params.put("q", qry);
        params.put("page", offset);
        asynclient.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray articleJsonResults = null;
                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    progressionBar.setVisibility(View.INVISIBLE);
                    opsadapter.addAll(nytimes_articles_fetch.fromJSONArray(articleJsonResults));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        progressionBar.setVisibility(View.VISIBLE);

    }


    public void onArticleSearch(View view) {
        opsadapter.clear();
        loadMoreData(0);
    }


    public void alertDialog() {
        View middle = getLayoutInflater().inflate(R.layout.nytimes_activity_showdialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("")
                .setPositiveButton(getResources().getString(R.string.article_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        intent.putExtra("article", fetcharticle);
                        startActivity(intent);
                        progressionBar.setVisibility(View.VISIBLE);
                    }

                }).setView(middle)

                .setNegativeButton(getResources().getString(R.string.article_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).setView(middle);

        builder.create().show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        progressionBar.setVisibility(View.INVISIBLE);

    }

    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("searchQuery", edittextQuery.getText().toString());
        edit.commit();
    }



    public class ForecastQuery extends AsyncTask<String, Integer, String> {
        String min, max, current, iconName;
        Bitmap icon;
        String API_KEY = "nGhORsp4W6LhNZnA1DtcYdeVv2Kp0l8r";
        String query = edittextQuery.getText().toString();
        int offset;
        JSONObject jObject;

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                InputStream stream = conn.getInputStream();
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(stream, null);

                while (parser.next() != XmlPullParser.END_DOCUMENT) {

                    //Start of JSON reading of UV factor:

                    //create the network connection:
                    URL UVurl = new URL("http://api.nytimes.com/svc/search/v2/articlesearch.json");
                    HttpURLConnection UVConnection = (HttpURLConnection) UVurl.openConnection();
                    InputStream inStream = UVConnection.getInputStream();


                    //create a JSON object from the response
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    String result = sb.toString();
                    query = edittextQuery.getText().toString();
                    jObject = new JSONObject(result);
                    API_KEY = jObject.getString("api-key");
                    query = jObject.getString("q");
                    offset = jObject.getInt("page");
                    JSONArray articleJsonResults = null;
                    try {
                        articleJsonResults = jObject.getJSONObject("response").getJSONArray("docs");
                        progressionBar.setVisibility(View.INVISIBLE);
                        opsadapter.addAll(nytimes_articles_fetch.fromJSONArray(articleJsonResults));
                        progressionBar.setVisibility(View.INVISIBLE);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    publishProgress(125);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.e("Crash!!", ex.getMessage());
            }
            return null;


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressionBar.setVisibility(View.VISIBLE);
            progressionBar.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(String s) {
        }

    }}