package com.example.princ.inclass06;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GetNewsTask extends AsyncTask<String, Void, ArrayList<NewsArticle>> {

    INewsArticle iNewsArticle;
    Context ctx;
    ProgressDialog pDlg;

    public GetNewsTask(INewsArticle iNewsArticle,Context ctx) {
        this.iNewsArticle = iNewsArticle;
        this.ctx=ctx;
        pDlg = new ProgressDialog(ctx);
        pDlg.setMessage("Loading News...");
        pDlg.setCancelable(false);
        pDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDlg.show();
    }

    HttpURLConnection con;
    @Override
    protected ArrayList<NewsArticle> doInBackground(String... strings) {
        ArrayList<NewsArticle> result = new ArrayList<>();
        for (int i = 0; i <100 ; i++) {
            for (int j = 0; j <10000 ; j++) {

            }
        }
        try {
            URL url = new URL(strings[0]);
            con = (HttpURLConnection) url.openConnection();
            con.connect();
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String json = IOUtils.toString(con.getInputStream(), "UTF8");
                JSONObject root = new JSONObject(json);
                JSONArray articles = root.getJSONArray("articles");
                for (int i = 0; i < articles.length(); i++) {
                    JSONObject articleJson = articles.getJSONObject(i);
                    NewsArticle nArticle = new NewsArticle();
                    nArticle.title = articleJson.getString("title");
                    nArticle.publishedAt = articleJson.getString("publishedAt");
                    nArticle.urlToImage = articleJson.getString("urlToImage");
                    nArticle.description = articleJson.getString("description");
                    result.add(nArticle);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }

        return result;
    }

        @Override
    protected void onPostExecute(ArrayList<NewsArticle> s) {
            iNewsArticle.handleNewsArticle(s);
            pDlg.dismiss();
    }

    public static interface INewsArticle {
        public void handleNewsArticle(ArrayList<NewsArticle> s);
    }
}
