package com.example.doanchuyennganh.Service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.doanchuyennganh.Dao.ItemDao;
import com.example.doanchuyennganh.Database.ItemsDatabase;
import com.example.doanchuyennganh.Models.Items;
import com.example.doanchuyennganh.Until.subString;
import com.example.doanchuyennganh.Views.MainActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Collections;
import java.util.List;

public class UpdateDataService extends Service {

    String title = "";
    Items chon;
    int i=0;

    public UpdateDataService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(),"Service Update Chạy",Toast.LENGTH_LONG).show();

        String uri = "http://stu.edu.vn/vi/cat/21/thong-bao.html?pIndex=" + 1 + "&per-page=21";
        getHTMLbyURL(uri);

        return START_STICKY;
    }
    private void checkData(){
        if(!restorePrefData().equals("")){
            Log.d("TAG","1"+restorePrefData());
            Log.d("TAG","2"+title);

                if (restorePrefData().equals(title)) {
                    Intent serviceIntent = new Intent(getApplicationContext(), ForegroundService.class);
                    serviceIntent.putExtra("inputExtra", "Không có thông báo nào mới");
                    ContextCompat.startForegroundService(getApplicationContext(), serviceIntent);
                } else {
                    DelleteAll();
                }
            }

    }

    private String restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(MainActivity.SHARED_PREFERENCE,MODE_PRIVATE);
        String data = pref.getString("oldData","");
        return  data;
    }

    private String getHTMLbyURL(String url){
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
                    //Log.d("TAG","Date: " + Datecreated);

                    Link = items.attr("href");
                    //Log.d("TAG","Link: " + Link);

                    Title = subString.subTrimTitle(Link.toString(),items.toString(),elementDate.toString());
                   // Log.d("TAG","Title: " + Title);

                    title=Title;
                    break;
                }
                checkData();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG",error.getMessage());
            }
        });


        return requestQueue.add(stringRequest).toString();
    }

    private void DelleteAll(){

        class DeleteAll extends AsyncTask<Void,Void, List<Items>> {
            @Override
            protected List<Items> doInBackground(Void... voids) {
                 ItemsDatabase
                        .getDatabase(getApplicationContext())
                        .noteDao()
                        .deleteAll();
                 return null;
            }

            @Override
            protected void onPostExecute(List<Items> items) {
                super.onPostExecute(items);
                String uri = "http://stu.edu.vn/vi/cat/21/thong-bao.html?pIndex=" + 1 + "&per-page=21";
                updateDatabase(uri);

                Intent serviceIntent = new Intent(getApplicationContext(), ForegroundService.class);
                serviceIntent.putExtra("inputExtra", title);
                ContextCompat.startForegroundService(getApplicationContext(), serviceIntent);

            }
        }
        new DeleteAll().execute();
    }

    private String updateDatabase(String url){
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
                i++;
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                chon=null;
                if(i==21){
                    Intent serviceIntent = new Intent(getApplicationContext(), ForegroundService.class);
                    serviceIntent.putExtra("inputExtra", title);
                    ContextCompat.startForegroundService(getApplicationContext(), serviceIntent);
                    //System.exit(0);
                }
            }
        }
        new SaveItemsTask().execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}