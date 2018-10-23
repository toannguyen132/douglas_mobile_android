package com.project.groupproject;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.project.groupproject.models.Event;

public class SingleEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_event);

        ActionBar actionBar = getSupportActionBar();
        TextView mDetailTv = findViewById(R.id.textView);

        //get data from previous activity when item of listview is clicked using intent
        Intent intent = getIntent();
        Event event = (Event)intent.getSerializableExtra("event");


        //set actionbar title
        actionBar.setTitle(event.name);
        //set text in textview
        mDetailTv.setText(event.description);
    }
}
