package com.example.princ.inclass06;

/*
  Author : Sujanth Babu Guntupalli
*/

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GetNewsTask.INewsArticle {

    Button go;
    ImageButton pButton, nButton;
    TextView viewCategory, titleTV, dateTV, descTV, articleNoTV;
    ImageView iv;
    AlertDialog.Builder builder;
    String[] categories={"Business","Entertainment","General","Health","Science","Sports","Technology"};
    ArrayList<NewsArticle> newsArticles=new ArrayList<>();
    int newsArticleId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Main Activity");

        go = (Button) findViewById(R.id.goButton);
        pButton = (ImageButton) findViewById(R.id.pButton);
        nButton = (ImageButton) findViewById(R.id.nButton);
        viewCategory = (TextView) findViewById(R.id.categoryTV);
        titleTV = (TextView) findViewById(R.id.titleTV);
        dateTV = (TextView) findViewById(R.id.dateTV);
        descTV = (TextView) findViewById(R.id.descTV);
        articleNoTV = (TextView) findViewById(R.id.articleNoTV);
        iv=(ImageView) findViewById(R.id.imageView);
        iv.setVisibility(View.GONE);
        pButton.setEnabled(false);
        nButton.setEnabled(false);


        builder = new AlertDialog.Builder(this);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected()) {
                    Toast.makeText(MainActivity.this, "Is Connected", Toast.LENGTH_SHORT).show();
                    builder.setTitle("Choose Category")
                            .setItems(categories, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {
                                    viewCategory.setText(categories[item]);
                                    newsArticles.clear();
                                    newsArticleId=0;
                                    if(isConnected()) {
                                        new GetNewsTask(MainActivity.this,MainActivity.this).execute("https://newsapi.org/v2/top-headlines?country=us&apiKey=bbee98d8505449b6ae2f0f5e4bdb22b3&category="+categories[item]);
                                    }else{
                                        Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).create().show();
                } else {
                    Toast.makeText(MainActivity.this, "Not Connected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        pButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (newsArticles != null && !newsArticles.isEmpty()) {
                        if (newsArticleId > 0) {
                            newsArticleId -= 1;
                            if(newsArticles.get(newsArticleId).getTitle()!=null&&!newsArticles.get(newsArticleId).getTitle().trim().isEmpty()&&newsArticles.get(newsArticleId).getTitle()!="null") {
                                titleTV.setText(newsArticles.get(newsArticleId).getTitle());
                            }else{
                                titleTV.setText("No Title Found");
                            }
                            if(newsArticles.get(newsArticleId).getPublishedAt()!=null&&!newsArticles.get(newsArticleId).getPublishedAt().trim().isEmpty()&&newsArticles.get(newsArticleId).getPublishedAt()!="null") {
                                dateTV.setText(newsArticles.get(newsArticleId).getPublishedAt());
                            }else{
                                dateTV.setText("No Published Date found");
                            }
                            if(newsArticles.get(newsArticleId).getDescription()!=null&&!newsArticles.get(newsArticleId).getDescription().trim().isEmpty()&&newsArticles.get(newsArticleId).getDescription()!="null") {
                                descTV.setText(newsArticles.get(newsArticleId).getDescription());
                            }else{
                                descTV.setText("No Description Found");
                            }
                            articleNoTV.setText(String.format("%d out of %d",(newsArticleId+1),newsArticles.size()));
                           if(isConnected()) {
                               Picasso.get().load(newsArticles.get(newsArticleId).getUrlToImage()).into(iv);
                            }else{
                                Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                            }
                        } else if (newsArticleId == 0) {
                            newsArticleId = newsArticles.size() - 1;
                            if(newsArticles.get(newsArticleId).getTitle()!=null&&!newsArticles.get(newsArticleId).getTitle().trim().isEmpty()&&newsArticles.get(newsArticleId).getTitle()!="null") {
                                titleTV.setText(newsArticles.get(newsArticleId).getTitle());
                            }else{
                                titleTV.setText("No Title Found");
                            }
                            if(newsArticles.get(newsArticleId).getPublishedAt()!=null&&!newsArticles.get(newsArticleId).getPublishedAt().trim().isEmpty()&&newsArticles.get(newsArticleId).getPublishedAt()!="null") {
                                dateTV.setText(newsArticles.get(newsArticleId).getPublishedAt());
                            }else{
                                dateTV.setText("No Published Date found");
                            }
                            if(newsArticles.get(newsArticleId).getDescription()!=null&&!newsArticles.get(newsArticleId).getDescription().trim().isEmpty()&&newsArticles.get(newsArticleId).getDescription()!="null") {
                                descTV.setText(newsArticles.get(newsArticleId).getDescription());
                            }else{
                                descTV.setText("No Description Found");
                            }
                            articleNoTV.setText(String.format("%d out of %d",(newsArticleId+1),newsArticles.size()));
                            if(isConnected()) {
                                Picasso.get().load(newsArticles.get(newsArticleId).getUrlToImage()).into(iv);
                            }else{
                                Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                            }
                        } else {

                        }
                    } else {
                        Toast.makeText(MainActivity.this, "No Images to dispaly", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){

                }
            }
        });

        nButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (newsArticles != null && !newsArticles.isEmpty()) {
                        if (newsArticleId < newsArticles.size() - 1) {
                            newsArticleId += 1;
                            if(newsArticles.get(newsArticleId).getTitle()!=null&&!newsArticles.get(newsArticleId).getTitle().trim().isEmpty()&&newsArticles.get(newsArticleId).getTitle()!="null") {
                                titleTV.setText(newsArticles.get(newsArticleId).getTitle());
                            }else{
                                titleTV.setText("No Title Found");
                            }
                            if(newsArticles.get(newsArticleId).getPublishedAt()!=null&&!newsArticles.get(newsArticleId).getPublishedAt().trim().isEmpty()&&newsArticles.get(newsArticleId).getPublishedAt()!="null") {
                                dateTV.setText(newsArticles.get(newsArticleId).getPublishedAt());
                            }else{
                                dateTV.setText("No Published Date found");
                            }
                            if(newsArticles.get(newsArticleId).getDescription()!=null&&!newsArticles.get(newsArticleId).getDescription().trim().isEmpty()&&newsArticles.get(newsArticleId).getDescription()!="null") {
                                descTV.setText(newsArticles.get(newsArticleId).getDescription());
                            }else{
                                descTV.setText("No Description Found");
                            }
                            articleNoTV.setText(String.format("%d out of %d",(newsArticleId+1),newsArticles.size()));
                            if(isConnected()) {
                                Picasso.get().load(newsArticles.get(newsArticleId).getUrlToImage()).into(iv);
                            }else{
                                Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                            }
                        } else if (newsArticleId == newsArticles.size() - 1) {
                            newsArticleId = 0;
                            if(newsArticles.get(newsArticleId).getTitle()!=null&&!newsArticles.get(newsArticleId).getTitle().trim().isEmpty()&&newsArticles.get(newsArticleId).getTitle()!="null") {
                                titleTV.setText(newsArticles.get(newsArticleId).getTitle());
                            }else{
                                titleTV.setText("No Title Found");
                            }
                            if(newsArticles.get(newsArticleId).getPublishedAt()!=null&&!newsArticles.get(newsArticleId).getPublishedAt().trim().isEmpty()&&newsArticles.get(newsArticleId).getPublishedAt()!="null") {
                                dateTV.setText(newsArticles.get(newsArticleId).getPublishedAt());
                            }else{
                                dateTV.setText("No Published Date found");
                            }
                            if(newsArticles.get(newsArticleId).getDescription()!=null&&!newsArticles.get(newsArticleId).getDescription().trim().isEmpty()&&newsArticles.get(newsArticleId).getDescription()!="null") {
                                descTV.setText(newsArticles.get(newsArticleId).getDescription());
                            }else{
                                descTV.setText("No Description Found");
                            }
                            articleNoTV.setText(String.format("%d out of %d",(newsArticleId+1),newsArticles.size()));
                            if(isConnected()) {
                                Picasso.get().load(newsArticles.get(newsArticleId).getUrlToImage()).into(iv);
                            }else{
                                Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                            }
                        } else {

                        }
                    } else {
                        Toast.makeText(MainActivity.this, "No Images to Display", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){

                }
            }
        });


    }



    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    @Override
    public void handleNewsArticle(ArrayList<NewsArticle> s) {
            if(s!=null&&!s.isEmpty()) {
                newsArticles = s;
                iv.setVisibility(View.VISIBLE);
                if(s.get(newsArticleId).getTitle()!=null&&!s.get(newsArticleId).getTitle().trim().isEmpty()&&s.get(newsArticleId).getTitle()!="null") {
                    titleTV.setText(s.get(newsArticleId).getTitle());
                }else{
                    titleTV.setText("No Title Found");
                }
                if(s.get(newsArticleId).getPublishedAt()!=null&&!s.get(newsArticleId).getPublishedAt().trim().isEmpty()&&s.get(newsArticleId).getPublishedAt()!="null") {
                    dateTV.setText(s.get(newsArticleId).getPublishedAt());
                }else{
                    dateTV.setText("No Published Date found");
                }
                if(s.get(newsArticleId).getDescription()!=null&&!s.get(newsArticleId).getDescription().trim().isEmpty()&&s.get(newsArticleId).getDescription()!="null") {
                    descTV.setText(s.get(newsArticleId).getDescription());
                }else{
                    descTV.setText("No Description Found");
                }
                articleNoTV.setText(String.format("%d out of %d", (newsArticleId + 1), newsArticles.size()));
                if (isConnected()) {
                        Picasso.get().load(newsArticles.get(newsArticleId).getUrlToImage()).into(iv);
                } else {
                    Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
                }
                if(s.size()>1) {
                    pButton.setEnabled(true);
                    nButton.setEnabled(true);
                }else{
                    pButton.setEnabled(false);
                    nButton.setEnabled(false);
                }
            }else{
                Toast.makeText(this, "No News Found", Toast.LENGTH_SHORT).show();
            }

    }
}
