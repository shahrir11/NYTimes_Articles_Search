package com.shahrir.nytimes_articles_search;


import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


//This class loads news articles  with images in the listview

public class nytimes_saved_arrayops extends ArrayAdapter<nytimes_articles_fetch> {


    //constructor matching super
    public nytimes_saved_arrayops(Context context, List<nytimes_articles_fetch> articles) {
        super(context, android.R.layout.simple_list_item_1, articles);
    }

//getView method to inflate layout nytimes_activity_searchresult,
// This will be using 'Picasso downloading and caching library package' to show the thumbnail results.

    public View getView(int position, View convertView, ViewGroup parent){

        nytimes_articles_fetch articleitem = this.getItem(position);

        if (convertView == null){
            LayoutInflater layoutinflater = LayoutInflater.from(getContext());
            convertView = layoutinflater.inflate(R.layout.nytimes_activity_searchresult, parent, false);

        }


        ImageView imgview = convertView.findViewById(R.id.image_viewer);
        imgview.setImageResource(0);

        TextView txtviw = convertView.findViewById(R.id.title_txtviewer) ;
        txtviw.setText(articleitem.getFetchheadline());

        String fetchthumbnail =  articleitem.getFetchthumbnail();
        if(!TextUtils.isEmpty(fetchthumbnail)){
            Picasso.with(getContext()).load(fetchthumbnail).into(imgview);

        }


        return convertView;
    }



}











