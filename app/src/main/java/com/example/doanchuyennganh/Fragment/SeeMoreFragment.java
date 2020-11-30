package com.example.doanchuyennganh.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.doanchuyennganh.Adapter.ItemAdapter;
import com.example.doanchuyennganh.Adapter.NumberAdapter;
import com.example.doanchuyennganh.Models.Items;
import com.example.doanchuyennganh.R;
import com.example.doanchuyennganh.Until.isInternetAvailable;
import com.example.doanchuyennganh.Until.subString;
import com.example.doanchuyennganh.Views.MainActivity;
import com.example.doanchuyennganh.listener.ClickInterfaceRecy;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class SeeMoreFragment extends Fragment implements ClickInterfaceRecy {

    View view;
    ImageButton btn_back;
    NumberAdapter numAdapter;
    RecyclerView mRecyclerView;
    ListView listView ;
    ItemAdapter adapter;
    List<String> numList=new ArrayList<>();
    ArrayList<Items> list = new ArrayList<>();
    TextView viewNum;

    String url ="http://stu.edu.vn/vi/cat/21/thong-bao.html?per-page=21&pIndex=2";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_see_more, container, false);

        addControls();
        addEvents();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();
            }
        });
        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CreatorList();
    }

    private void CreatorList() {
        for (int i=2; i<=21;i++){
            numList.add(i+"");
        }
    }

    private void addEvents() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    if (position >= 0 && position < adapter.getCount()) {

                        MainActivity.itemsclick = adapter.getItem(position);

                        MainActivity.ListItems.get(position).setSeen(true);

                        Navigation.findNavController(view).navigate(R.id.action_seeMoreFragment_to_browserFragment);

                }
            }
        });
    }

    private void addControls() {
        btn_back=view.findViewById(R.id.btn_back);
        mRecyclerView=view.findViewById(R.id.list_num);
        listView=view.findViewById(R.id.list_item);
        viewNum=view.findViewById(R.id.viewNum);

        LinearLayoutManager layoutManager= new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        numAdapter = new NumberAdapter(getActivity(),numList,this);
        mRecyclerView.setAdapter(numAdapter);

        adapter = new ItemAdapter(getActivity(),R.layout.layout_item,list);
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {
        list.clear();
        viewNum.setVisibility(View.VISIBLE);
        String uri = "http://stu.edu.vn/vi/cat/21/thong-bao.html?pIndex=" + numList.get(position) + "&per-page=21";
        viewNum.setText("Bạn đang xem trang "+ numList.get(position));
        viewNum.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                viewNum.setVisibility(View.GONE);
            }
        },10000);

        getHTMLbyURL(uri);


    }

    private String getHTMLbyURL(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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

                    list.add(new Items(Title,Link,subString.convertDate(Datecreated),false));
                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG",error.getMessage());
            }
        });


        return requestQueue.add(stringRequest).toString();
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.itemsclick=null;
    }
}