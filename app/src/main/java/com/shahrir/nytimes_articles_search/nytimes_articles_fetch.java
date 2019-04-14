package com.shahrir.nytimes_articles_search;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;

// this class establish connection with actual nytimes api to fetch articles


public class nytimes_articles_fetch implements Serializable {


    String fetchurl, fetchheadline,fetchthumbnail ;

    //creating constructor of the class
    public nytimes_articles_fetch( String fetchurl, String fetchheadline, String fetchthumbnail){
        this.fetchurl = fetchurl;
        this.fetchheadline = fetchheadline;
        this.fetchthumbnail = fetchthumbnail;

    }

    public String getFetchurl() {
        return fetchurl;
    }

    public String getFetchheadline() {
        return fetchheadline;
    }

    public String getFetchthumbnail() {
        return fetchthumbnail;
    }

//opening article from nytimes website, bridling the link with JSONObject

    public nytimes_articles_fetch(JSONObject jsonObject) {

        try{
            this.fetchurl = jsonObject.getString("URL");
            this.fetchheadline = jsonObject.getJSONObject("HEADLINES").getString("main");

            JSONArray mmedia = jsonObject.getJSONArray("MEDIA");

            if (mmedia.length() > 0 ){
                JSONObject mmediajson = mmedia.getJSONObject(0);
                this.fetchthumbnail = "http://www.nytimes.com/" + mmediajson.getString("urlurl");
            }else{
                this.fetchthumbnail = "";

        }

        }catch (JSONException e){
            e.printStackTrace();

        }
    }
// adding article to JSONArray

    public static ArrayList<nytimes_articles_fetch> fromJSONArray(JSONArray jsarray) {
        ArrayList<nytimes_articles_fetch>  strorz = new ArrayList<>();

        for (int i = 0; i < jsarray.length() ; i++  ){
            try{

                strorz.add(new nytimes_articles_fetch(jsarray.getJSONObject(i)));

            }catch(JSONException e){
                e.printStackTrace();

            }

        }

        return strorz;
    }




}
