package com.example.doanchuyennganh.Service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.doanchuyennganh.Models.Items;
import com.example.doanchuyennganh.Until.subString;
import com.example.doanchuyennganh.Views.MainActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class UpdateDataService extends Service {

    String title = "";

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
            Log.d("TAG",restorePrefData());
            Log.d("TAG",title);

                if (restorePrefData().equals(title)) {
                    Intent serviceIntent = new Intent(getApplicationContext(), ForegroundService.class);
                    serviceIntent.putExtra("inputExtra", "Không có thông báo nào mới");
                    ContextCompat.startForegroundService(getApplicationContext(), serviceIntent);
                } else {
                    Intent serviceIntent = new Intent(getApplicationContext(), ForegroundService.class);
                    serviceIntent.putExtra("inputExtra", "Có Thông Báo Mới");
                    ContextCompat.startForegroundService(getApplicationContext(), serviceIntent);
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
}