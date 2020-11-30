package com.example.doanchuyennganh.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanchuyennganh.R;
import com.example.doanchuyennganh.listener.ClickInterfaceRecy;

import java.util.List;

public class NumberAdapter extends RecyclerView.Adapter<NumberAdapter.MyViewHolder> {

    List<String> list;
    Context context;
    ClickInterfaceRecy clickInterfaceRecy;

    public NumberAdapter(Context context, List<String> list, ClickInterfaceRecy clickInterfaceRecy) {
        this.list = list;
        this.context = context;
        this.clickInterfaceRecy=clickInterfaceRecy;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_number,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.textView_num.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textView_num;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            textView_num=itemView.findViewById(R.id.num);

            itemView.setOnClickListener(new OnClickListener() {
               @Override
               public void onClick(View view) {
                   //Toast.makeText(context,getAdapterPosition()+"",Toast.LENGTH_SHORT).show();
                   clickInterfaceRecy.onItemClick(getAdapterPosition());
               }
           });
        }
    }

}
