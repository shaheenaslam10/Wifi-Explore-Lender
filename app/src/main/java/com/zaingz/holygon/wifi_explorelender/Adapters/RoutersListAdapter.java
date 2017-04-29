package com.zaingz.holygon.wifi_explorelender.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zaingz.holygon.wifi_explorelender.DataModel.RoutersModel;
import com.zaingz.holygon.wifi_explorelender.MyDevicesActivity;
import com.zaingz.holygon.wifi_explorelender.R;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by Muhammad Shan on 11/04/2017.
 */

public class RoutersListAdapter extends android.support.v7.widget.RecyclerView.Adapter<RoutersListAdapter.RecyclerHolder> {


    ArrayList<RoutersModel> routerDatas = new ArrayList<RoutersModel>();
    Intent intent;
    Context context;
    Realm realm;

    public RoutersListAdapter(ArrayList<RoutersModel> routerDatas, Context context) {
        this.routerDatas = routerDatas;
        this.context = context;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_router,parent,false);
        RecyclerHolder recyclerHolder = new RecyclerHolder(view);
        return recyclerHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, final int position) {


        final RoutersModel routerData = routerDatas.get(position);
        Log.d("shani","routerdata name  :   "+routerData.getName());
        holder.name.setText(routerData.getName());
        Log.d("shani"," after set text  routerdata name  :   "+routerData.getName());
        holder.signal.setText(routerData.getSignal_strength());
        holder.rating.setText(routerData.getRating());
        holder.count.setText(routerData.getDevices());
        holder.rowRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent(context, MyDevicesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("namrouter", routerData.getName());
                intent.putExtra("idrouterlist", routerData.getId());
                context.startActivity(intent);

            }
        });


       /* if (position%2 == 0) {
            holder.rowRoot.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
        }
        else {
            holder.rowRoot.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBlack));
        }*/

        Log.d("shani","routerdata name  :   "+routerData.getName());
        Log.d("shani","routerdata name  :   "+routerData.getDevices());
        Log.d("shani","routerdata name  :   "+routerData.getRating());
        Log.d("shani","routerdata name  :   "+routerData.getSignal_strength());

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

            name = (TextView)itemView.findViewById(R.id.tv_router_name);
            signal = (TextView)itemView.findViewById(R.id.tv_signal_strength);
            rating = (TextView)itemView.findViewById(R.id.rating);
            count = (TextView)itemView.findViewById(R.id.count);
            rowRoot = (LinearLayout)itemView.findViewById(R.id.rowRoot);
        }
    }
}