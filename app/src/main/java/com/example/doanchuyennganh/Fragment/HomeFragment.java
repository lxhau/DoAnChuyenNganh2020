package com.example.doanchuyennganh.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.doanchuyennganh.Adapter.ItemAdapter;
import com.example.doanchuyennganh.Database.ItemsDatabase;
import com.example.doanchuyennganh.Models.Items;
import com.example.doanchuyennganh.R;
import com.example.doanchuyennganh.Service.ForegroundService;
import com.example.doanchuyennganh.Service.getRSS;
import com.example.doanchuyennganh.Views.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    ItemAdapter adapter;
    ListView listView;
    ImageView btn_caidat, btnSearch;
    EditText inputSearch;
    View view;
    ArrayList<Items> list ;

    int REST = 0;
    SwipeRefreshLayout refeshLayout;
    Items chon;
    @Override
    public void onStart() {

        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_home, container, false);

        addControls();
        addEvents();
        getAllItems();

        return view;
    }

    private void addEvents() {
        btn_caidat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_settingFragment);
            }
        });

        refeshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.notifyDataSetChanged();
                refeshLayout.setRefreshing(false);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(position>=0&&position<adapter.getCount()) {
                    //Toast.makeText(getActivity(),adapter.getItem(position).toString(),Toast.LENGTH_LONG).show();

                    MainActivity.itemsclick = adapter.getItem(position);
                    chon=adapter.getItem(position);

                    clickReadItem();

                    MainActivity.ListItems.get(position).setSeen(true);
                    //adapter.notifyDataSetChanged();

                    Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_browserFragment);

                }
            }
        });

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(MainActivity.ListItems.size()!=0){
                    adapter.searchNotes(editable.toString());
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private void clickReadItem(){
        chon.setSeen(true);
        @SuppressLint("StaticFieldLeak")
        class SaveNoteTask extends AsyncTask<Void,Void,Void>{

            @Override
            protected Void doInBackground(Void... voids) {

                ItemsDatabase.getDatabase(getActivity())
                        .noteDao()
                        .insertItems(chon);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                chon= null;
            }
        }
        new SaveNoteTask().execute();
    }

    private void addControls() {
        listView= view.findViewById(R.id.list_item);
        btn_caidat = view.findViewById(R.id.btn_caidat);
        btnSearch= view.findViewById(R.id.btnSearch);
        refeshLayout= view.findViewById(R.id.refeshLayout);
        inputSearch= view.findViewById(R.id.inputSearch);

        adapter = new ItemAdapter(getActivity(),R.layout.layout_item,MainActivity.ListItems);
        listView.setAdapter(adapter);
        listView.setDividerHeight(0);

    }

    public void getAllItems(){
      new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
              if(MainActivity.REST==200){
                  adapter.notifyDataSetChanged();
              }else
                  getAllItems();
          }
      },1000);
    }

}