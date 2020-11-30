package com.example.doanchuyennganh.Fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.doanchuyennganh.Adapter.ItemAdapter;
import com.example.doanchuyennganh.Database.ItemsDatabase;
import com.example.doanchuyennganh.Models.Items;
import com.example.doanchuyennganh.R;
import com.example.doanchuyennganh.Views.MainActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchFragment extends Fragment {

    View view;
    EditText inputSearch;
    ImageView btnSearch;
    ImageButton btn_back;
    ListView listView;
    ItemAdapter adapter;
    ArrayList<Items> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view = inflater.inflate(R.layout.fragment_search, container, false);

       inputSearch= view.findViewById(R.id.inputSearch);
       inputSearch.requestFocus();
       btnSearch= view.findViewById(R.id.btnQuerySearch);
       listView = view.findViewById(R.id.list_item);
       btn_back=view.findViewById(R.id.btn_back);

       adapter= new ItemAdapter(getActivity(),R.layout.layout_item,list);
       listView.setAdapter(adapter);


       btnSearch.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(inputSearch.getText().length()>2){
                   getAllItems();
               }else {
                   Toast.makeText(getActivity(),"Bạn cần nhập từ khóa dài hơn",Toast.LENGTH_SHORT).show();
               }
           }
       });

       btn_back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Navigation.findNavController(view).popBackStack();
           }
       });

       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
               if (position >= 0 && position < adapter.getCount()) {
                   //Toast.makeText(getActivity(),adapter.getItem(position).toString(),Toast.LENGTH_LONG).show();

                   MainActivity.itemsclick = adapter.getItem(position);
                   Items chon = adapter.getItem(position);
                   if (MainActivity.itemsclick.getSeen().equals(false)) {
                       clickReadItem(chon);
                   }

                   MainActivity.ListItems.get(position).setSeen(true);
                   //adapter.notifyDataSetChanged();

                   Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_browserFragment);

               }
           }
       });


       return view;
    }

    private void clickReadItem(final Items chon) {
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

            }
        }
        new SaveNoteTask().execute();
    }

    private void getAllItems() {

        @SuppressLint("StaticFieldLeak")
        class GetItemsTask extends AsyncTask<Void, Void, List<Items>> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                list.clear();

            }

            @Override
            protected List<Items> doInBackground(Void... voids) {
                return ItemsDatabase
                        .getDatabase(getActivity())
                        .noteDao()
                        .selectItems(inputSearch.getText().toString());
            }

            @Override
            protected void onPostExecute(List<Items> items) {
                super.onPostExecute(items);
                list.addAll(items);
                adapter.notifyDataSetChanged();
                if(list.size()==0){
                    Toast.makeText(getActivity(),"Không tìm thấy thông báo nào !",Toast.LENGTH_LONG).show();
                }
                Toast.makeText(getActivity(),"Tìm thấy " + list.size()+ " thông báo",Toast.LENGTH_LONG).show();
            }
        }
        new GetItemsTask().execute();
    }
}