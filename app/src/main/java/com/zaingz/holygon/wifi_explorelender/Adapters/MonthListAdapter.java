package com.zaingz.holygon.wifi_explorelender.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zaingz.holygon.wifi_explorelender.DataModel.RoutersModel;
import com.zaingz.holygon.wifi_explorelender.Database.SignUpDatabase;
import com.zaingz.holygon.wifi_explorelender.Database.WalletDataBase;
import com.zaingz.holygon.wifi_explorelender.MyInterface;
import com.zaingz.holygon.wifi_explorelender.MyWalletActivit;
import com.zaingz.holygon.wifi_explorelender.R;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Muhammad Shan on 11/04/2017.
 */

public class MonthListAdapter extends android.support.v7.widget.RecyclerView.Adapter<MonthListAdapter.RecyclerHolder> {


    ArrayList<String> routerDatas = new ArrayList<>();
    Context context;
    Realm realm;
    TextView blnce;
    public MyInterface listener;


    public MonthListAdapter(ArrayList<String> routerDatas, Context context, MyInterface listener) {
        this.routerDatas = routerDatas;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_month,parent,false);
        RecyclerHolder recyclerHolder = new RecyclerHolder(view);
        return recyclerHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, final int position) {


        Log.d("shani","routerdata name  :   "+routerDatas.get(position));
        holder.name.setText(routerDatas.get(position));

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (position){

                    case 0:
                        listener.sendToAdapter("January");
                        break;
                    case 1:
                         listener.sendToAdapter("February");
                        break;
                    case 2:
                        listener.sendToAdapter("March");
                        break;
                    case 3:
                        listener.sendToAdapter("April");
                        break;
                    case 4:
                        listener.sendToAdapter("May");
                        break;
                    case 5:
                        listener.sendToAdapter("June");
                        break;
                    case 6:
                        listener.sendToAdapter("July");
                        break;
                    case 7:
                        listener.sendToAdapter("August");
                        break;
                    case 8:
                        listener.sendToAdapter("September");
                        break;
                    case 9:
                        listener.sendToAdapter("October");
                        break;
                    case 10:
                        listener.sendToAdapter("November");
                        break;
                    case 11:
                        listener.sendToAdapter("December");


                }
            }
        });

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