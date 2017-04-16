package com.zaingz.holygon.wifi_explorelender;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zaingz.holygon.wifi_explorelender.Adapters.GridAdapter;
import com.zaingz.holygon.wifi_explorelender.Adapters.RoutersListAdapter;
import com.zaingz.holygon.wifi_explorelender.DataModel.RoutersModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    DrawerLayout mDrawerLayout;
ImageView img_list;

    RecyclerView recyclerView;
    RoutersListAdapter recyclerAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<RoutersModel> routerDatas = new ArrayList<>();

    String name = "TP-Link External router";
    String rating = "4/5";
    String signal = "Good";
    String count = "6";

    GridView grid;
    boolean list=true;
    FrameLayout fl_grid;

    LinearLayout ll_wallet,ll_history,ll_add,ll_devices,ll_logout,ll_profile;
    TextView txt_earnings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ll_wallet= (LinearLayout) findViewById(R.id.ll_wallet);
        ll_add= (LinearLayout) findViewById(R.id.ll_add);
        ll_devices= (LinearLayout) findViewById(R.id.ll_devices);
        ll_history= (LinearLayout) findViewById(R.id.ll_history);
        ll_logout= (LinearLayout) findViewById(R.id.ll_logout);
        ll_profile= (LinearLayout) findViewById(R.id.ll_profile);


        ll_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MyWalletActivit.class);
                startActivity(intent);
            }
        });
        ll_devices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MyDevicesActivity.class);
                startActivity(intent);
            }
        });
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.hamburger);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        img_list= (ImageView) findViewById(R.id.img_list);
        fl_grid= (FrameLayout) findViewById(R.id.fl_grid);
        fl_grid.setVisibility(View.INVISIBLE);


        img_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(list){

                    img_list.setImageDrawable(getResources().getDrawable(R.drawable.list));
                    fl_grid.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                    list=false;

                }
                else {
                    img_list.setImageDrawable(getResources().getDrawable(R.drawable.grid));
                    fl_grid.setVisibility(View.INVISIBLE);
                    list=true;
                    recyclerView.setVisibility(View.VISIBLE);
                }

            }
        });




        int i=0;
        for(i=0; i<10; i++){
            RoutersModel routerData=new RoutersModel(name,signal,rating,count);
            routerDatas.add(routerData);
            Log.d("shani","name  :   "+name );
            Log.d("shani","name  :   "+signal);
            Log.d("shani","name  :   "+rating);
            Log.d("shani","name  :   "+name);

        }
        Log.d("shani","Loop count:   "+i);
        recyclerAdapter = new RoutersListAdapter(routerDatas,getApplicationContext());
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);

        //grid view

        GridAdapter adapter = new GridAdapter(MainActivity.this,routerDatas);
        grid=(GridView)findViewById(R.id.grid_view);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(MainActivity.this, "You Clicked at " +routerDatas.get(position).getName(), Toast.LENGTH_SHORT).show();

            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                   /* Toast.makeText(getApplicationContext(), "this is my HomeT",
                            Toast.LENGTH_LONG).show();*/
                mDrawerLayout.openDrawer(GravityCompat.START);


                return true;

            /*    case R.id.action_settings:
                    return true;*/
        }
        return super.onOptionsItemSelected(item);
    }
}
