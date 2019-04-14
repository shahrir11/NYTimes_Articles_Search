package com.shahrir.nytimes_articles_search;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


//Fragmen for both phone and tablet mode
public class nytimes_search_fragment extends Fragment {

    protected ImageView image_article;
    protected TextView title_article, url_article;
    protected Button deleteButton;
    Intent intent;
    public static String urlTextToLoad;
    int articleID;
    String urlText;
    protected nytimes_articles_saved article_window;

    public nytimes_search_fragment() {
        super();
    }

    @SuppressLint("ValidFragment")
    public nytimes_search_fragment(nytimes_articles_saved article_window) {
        super();
        this.article_window = article_window;
    }

//initializing fields and fragments

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.nytimes_activity_articles_fragment, container, false);
        final Bundle args = getArguments();
        image_article = (ImageView) view.findViewById(R.id.fragment_image_viewer);
        title_article = (TextView) view.findViewById(R.id.fragment_article_title);
        url_article = (TextView) view.findViewById(R.id.fragment_article_urladdress);
        deleteButton = (Button) view.findViewById(R.id.delete_button);
        String titleText = "Article Title:" + " " + args.getString("headline");
        urlText = "URL: " + " " + args.getString("url");
        articleID = args.getInt("id");
        String thumbnail = args.getString("pic_url");
        if (thumbnail.length()>0) {
            Picasso.with(getContext()).load(thumbnail).into(image_article);
        }
        title_article.setText(titleText);
        url_article.setText(urlText);
        urlTextToLoad = args.getString("url");
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (article_window == null) {
                    Intent msgDetailsIntent = new Intent(getActivity(), nytimes_search_fragment.class);
                    msgDetailsIntent.putExtra("msgID", articleID);

                    getActivity().setResult(nytimes_articles_saved.CONTEXT_INCLUDE_CODE, msgDetailsIntent);
                    getActivity().finish();
                } else {
                    article_window.deleteItem(articleID);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.remove(nytimes_search_fragment.this);
                    ft.commit();
                }
            }
        });

        url_article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), nytimes_saved_articles_webview.class);
                startActivity(intent);
            }
        });
        return view;
    }









}
