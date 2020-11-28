package com.example.doanchuyennganh.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.doanchuyennganh.Adapter.ItemAdapter;
import com.example.doanchuyennganh.Database.ItemsDatabase;
import com.example.doanchuyennganh.Models.Items;
import com.example.doanchuyennganh.R;
import com.example.doanchuyennganh.Service.ForegroundService;
import com.example.doanchuyennganh.Service.UpdateDataService;
import com.example.doanchuyennganh.Service.getRSS;
import com.example.doanchuyennganh.Until.isInternetAvailable;
import com.example.doanchuyennganh.Views.MainActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {

    ItemAdapter adapter;
    ListView listView;
    ImageView btn_caidat;
    LinearLayout btnSearch;
    View view;
    ArrayList<Items> list;
    View Seemore;
    AlertDialog dialogInternet;
    final static int ENABLE_INTERNET=112;

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
        view = inflater.inflate(R.layout.fragment_home, container, false);

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

                if (isInternetAvailable.isInternetAvai(getActivity()) == true) {

                    Intent intentservice = new Intent(getActivity(), UpdateDataService.class);
                    getActivity().startService(intentservice);

                    refAllItems();
                    getAllItems();
                } else {
                    Toast.makeText(getActivity(), "Không có kết nối Internet", Toast.LENGTH_LONG).show();
                }
                adapter.notifyDataSetChanged();
                refeshLayout.setRefreshing(false);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                if(isInternetAvailable.isInternetAvai(getActivity())==false){
                    showHopThoi();
                }else {
                    if (position >= 0 && position < adapter.getCount()) {
                        //Toast.makeText(getActivity(),adapter.getItem(position).toString(),Toast.LENGTH_LONG).show();

                        MainActivity.itemsclick = adapter.getItem(position);
                        chon = adapter.getItem(position);
                        if (MainActivity.itemsclick.getSeen().equals(false)) {
                            clickReadItem();
                        }

                        MainActivity.ListItems.get(position).setSeen(true);
                        //adapter.notifyDataSetChanged();

                        Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_browserFragment);

                    }
                }


            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_searchFragment);
            }
        });

        Seemore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isInternetAvailable.isInternetAvai(getActivity())) {
                    Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_seeMoreFragment);
                }else{
                    showHopThoi();
                }
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private void clickReadItem() {
        chon.setSeen(true);
        @SuppressLint("StaticFieldLeak")
        class SaveNoteTask extends AsyncTask<Void, Void, Void> {

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
                chon = null;
            }
        }
        new SaveNoteTask().execute();
    }

    private void addControls() {
        listView = view.findViewById(R.id.list_item);
        btn_caidat = view.findViewById(R.id.btn_caidat);
        refeshLayout = view.findViewById(R.id.refeshLayout);
        btnSearch = view.findViewById(R.id.btnSearch);

        Seemore=LayoutInflater.from(getActivity()).inflate(R.layout.footerview,null);

        adapter = new ItemAdapter(getActivity(), R.layout.layout_item, MainActivity.ListItems);
        listView.setAdapter(adapter);
        listView.setDividerHeight(0);
        listView.addFooterView(Seemore);

    }

    public void getAllItems() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (MainActivity.REST == 200) {
                    adapter.notifyDataSetChanged();
                } else
                    getAllItems();
            }
        }, 1000);
    }

    private void refAllItems() {

        @SuppressLint("StaticFieldLeak")
        class GetItemsTask extends AsyncTask<Void, Void, List<Items>> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                MainActivity.REST = -1;
                MainActivity.ListItems.clear();
            }

            @Override
            protected List<Items> doInBackground(Void... voids) {
                return ItemsDatabase
                        .getDatabase(getActivity())
                        .noteDao()
                        .getAllItems();
            }

            @Override
            protected void onPostExecute(List<Items> items) {
                super.onPostExecute(items);
                MainActivity.ListItems.addAll(items);
                MainActivity.saveData(getContext());

                Collections.sort(MainActivity.ListItems);
                MainActivity.REST = 200;
            }
        }
        new GetItemsTask().execute();
    }
    private void showHopThoi(){
        if(dialogInternet==null){
            Context context;
            android.app.AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_dialog_no_internet,(ViewGroup) getActivity().findViewById(R.id.layout_dialog_no_internet));
            builder.setView(view);

            dialogInternet= builder.create();
            if(dialogInternet.getWindow() !=null){
                dialogInternet.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            final Button btn_goToSetting= view.findViewById(R.id.btn_goToSetting);
            final Button btn_close= view.findViewById(R.id.btn_close);
            final LottieAnimationView lottieAnimationView = view.findViewById(R.id.imgView_gif_nointernet);
            lottieAnimationView.setSpeed(-1);
            lottieAnimationView.playAnimation();


            view.findViewById(R.id.btn_goToSetting).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), ENABLE_INTERNET);
                    dialogInternet.dismiss();
                }
            });

            view.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogInternet.dismiss();
                }
            });
        }

        dialogInternet.setCanceledOnTouchOutside(false);
        dialogInternet.show();
    }

}