package com.example.doanchuyennganh.Service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.doanchuyennganh.Database.ItemsDatabase;
import com.example.doanchuyennganh.Models.Items;
import com.example.doanchuyennganh.R;
import com.example.doanchuyennganh.Until.FormatUntil;
import com.example.doanchuyennganh.Until.subString;
import com.example.doanchuyennganh.Views.MainActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class getRSS extends Service {

    private static final String TAG = "TEST";
    Items chon;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String uri = "http://stu.edu.vn/vi/cat/21/thong-bao.html?pIndex=" + 1 + "&per-page=21";
        getHTMLbyURL(uri);

        return START_NOT_STICKY;
    }

    private String getHTMLbyURL(String url){
        chon= null;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Document document = Jsoup.parse(response);
                Elements array =   document.select("a.listNewsItem");

                for(Element items : array){
                    String Title=null;
                    String Datecreated =null;
                    String Link=null;

                    Element elementDate = items.getElementsByTag("span").first();
                    Datecreated = subString.subDate(elementDate.text());
                    Log.d("TAG","Date: " + Datecreated);

                    Link = items.attr("href");
                    Log.d("TAG","Link: " + Link);

                    Title = subString.subTrimTitle(Link.toString(),items.toString(),elementDate.toString());
                    Log.d("TAG","Title: " + Title);

                    chon = new Items(Title,Link,Datecreated,false);
                    SaveDataInRoom(chon);
                }
               MainActivity.FLAG= 200;

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG",error.getMessage());
            }
        });


        return requestQueue.add(stringRequest).toString();
    }

    private void SaveDataInRoom(final Items news){

        @SuppressLint("StaticFieldLeak")
        class SaveItemsTask extends AsyncTask<Void,Void,Void>{

            @Override
            protected Void doInBackground(Void... voids) {
                    ItemsDatabase.getDatabase(getApplicationContext())
                            .noteDao()
                            .insertItems(news);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                chon=null;
            }
        }
        new SaveItemsTask().execute();
    }



}
