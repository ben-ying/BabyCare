package com.ben.yjh.babycare.main.event;

import android.os.Bundle;
import android.view.View;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.base.BaseActivity;

public class EventDetailActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle(R.string.comment);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        initToolbar(R.string.add_event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
