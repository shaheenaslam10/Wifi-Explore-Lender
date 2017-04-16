package com.zaingz.holygon.wifi_explorelender.Adapters;

/**
 * Created by Muhammad Shan on 12/04/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zaingz.holygon.wifi_explorelender.DataModel.RoutersModel;
import com.zaingz.holygon.wifi_explorelender.R;

import java.util.ArrayList;
import java.util.List;

public class GridAdapter extends BaseAdapter{
    ArrayList<RoutersModel> routerDatas = new ArrayList<RoutersModel>();
    Context context;

    public GridAdapter(Context c, ArrayList<RoutersModel> routersModels) {
        context = c;
        this.routerDatas = routersModels;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return routerDatas.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(context);
            grid = inflater.inflate(R.layout.list_grid_router, null);
            TextView numbers = (TextView) grid.findViewById(R.id.txt_numbers);
            TextView name = (TextView)grid.findViewById(R.id.txt_name);
            numbers.setText(routerDatas.get(position).getDevices());
            name.setText(routerDatas.get(position).getName());
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}