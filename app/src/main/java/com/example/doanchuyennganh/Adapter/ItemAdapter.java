package com.example.doanchuyennganh.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelUuid;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.doanchuyennganh.Models.Items;
import com.example.doanchuyennganh.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class ItemAdapter extends ArrayAdapter<Items> {

    List<Items> listitems;
    List<Items> listitemssource;
    Activity context;
    int resource;
    private Timer timer;


    public ItemAdapter(@NonNull Activity context, int resource, @NonNull List<Items> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource= resource;
        this.listitems = objects;
        listitemssource= listitems;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.layout_item,parent,false);

        TextView title,date;
        title=convertView.findViewById(R.id.txt_title);
        date=convertView.findViewById(R.id.txt_date);

        Items items= listitems.get(position);
        title.setText(items.getTitle());
        date.setText(items.getDateCreated());

        if(items.getSeen()==false){
            title.setTextColor(Color.BLACK);
        }

        return  convertView;
    }

    public void searchNotes(final String searchNodeKeyWord){
        timer= new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(searchNodeKeyWord.trim().isEmpty()){
                    listitems= listitemssource;
                }else{
                    ArrayList<Items> items=new ArrayList<>();
                    for(Items note: listitemssource){
                        if (note.getTitle().toLowerCase().contains(searchNodeKeyWord.toLowerCase())
                                || note.getDateCreated().toLowerCase().contains(searchNodeKeyWord.toLowerCase())
                               ){
                            items.add(note);
                        }
                    }
                    listitems=items;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }

        }, 200);
    }

    public void cancelTimer(){
        if(timer !=null){
            timer.cancel();
        }
    }


}
