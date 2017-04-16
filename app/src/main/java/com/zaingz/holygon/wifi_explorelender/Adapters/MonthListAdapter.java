package com.zaingz.holygon.wifi_explorelender.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zaingz.holygon.wifi_explorelender.DataModel.RoutersModel;
import com.zaingz.holygon.wifi_explorelender.R;

import java.util.ArrayList;

/**
 * Created by Muhammad Shan on 11/04/2017.
 */

public class MonthListAdapter extends android.support.v7.widget.RecyclerView.Adapter<MonthListAdapter.RecyclerHolder> {


    ArrayList<String> routerDatas = new ArrayList<>();
    Context context;


    public MonthListAdapter(ArrayList<String> routerDatas, Context context) {
        this.routerDatas = routerDatas;
        this.context = context;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_month,parent,false);
        RecyclerHolder recyclerHolder = new RecyclerHolder(view);
        return recyclerHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {


        Log.d("shani","routerdata name  :   "+routerDatas.get(position));
        holder.name.setText(routerDatas.get(position));


       /* if (position%2 == 0) {
            holder.rowRoot.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
        }
        else {
            holder.rowRoot.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBlack));
        }*/


    }

    @Override
    public int getItemCount() {

        return routerDatas.size();
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView name, rating, signal, count;
        LinearLayout rowRoot;
        public RecyclerHolder(View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.txt_name);

        }
    }
}