package com.zaingz.holygon.wifi_explorelender;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.eralp.circleprogressview.CircleProgressView;
import com.zaingz.holygon.wifi_explorelender.Adapters.MonthListAdapter;
import com.zaingz.holygon.wifi_explorelender.Adapters.RoutersListAdapter;
import com.zaingz.holygon.wifi_explorelender.DataModel.RoutersModel;

import java.util.ArrayList;

public class MyWalletActivit extends AppCompatActivity {


    RecyclerView recyclerView;
    MonthListAdapter recyclerAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> routerDatas = new ArrayList<>();
    private CircleProgressView mCircleProgressView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);


        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.drawable.back);

        mToolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }

                }

        );



        recyclerView= (RecyclerView) findViewById(R.id.recyclerView);

        mCircleProgressView3 = (CircleProgressView) findViewById(R.id.circle_progress_view3);
        mCircleProgressView3.setTextEnabled(false);
        mCircleProgressView3.setInterpolator(new AccelerateDecelerateInterpolator());
        mCircleProgressView3.setStartAngle(45);
        mCircleProgressView3.setProgressWithAnimation(85, 2000);


        routerDatas.add("December");
        routerDatas.add("January");
        routerDatas.add("February");
        routerDatas.add("March");
        routerDatas.add("April");
        routerDatas.add("May");
        routerDatas.add("June");
        routerDatas.add("July");
        routerDatas.add("August");
        routerDatas.add("September");
        routerDatas.add("October");
        routerDatas.add("November");

        recyclerAdapter = new MonthListAdapter(routerDatas,getApplicationContext());
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }
}
