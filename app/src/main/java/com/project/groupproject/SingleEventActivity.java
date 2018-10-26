package com.project.groupproject;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.project.groupproject.models.Event;
import com.project.groupproject.models.User;
import com.project.groupproject.viewmodals.EventViewModel;

public class SingleEventActivity extends AppCompatActivity {

    TextView viewMonth, viewDate, viewTitle, viewOwner, viewDesc;
    Event event;
    EventViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_event);

        final ActionBar actionBar = getSupportActionBar();

        // setup views
        viewMonth = findViewById(R.id.view_month);
        viewDate = findViewById(R.id.view_day);
        viewTitle = findViewById(R.id.view_title);
        viewOwner = findViewById(R.id.view_owner);
        viewDesc = findViewById(R.id.view_desc);

        //get data from previous activity when item of listview is clicked using intent
        Intent intent = getIntent();
//        event = (Event)intent.getSerializableExtra("event");
        String id = intent.getStringExtra("event_id");

        // create viewModel
        viewModel = ViewModelProviders.of(this).get(EventViewModel.class);

        // track event data
        viewModel.getEvent().observe(this, new Observer<Event>() {
            @Override
            public void onChanged(@Nullable Event newEvent) {
                event = newEvent;
                //set text in textview
                viewTitle.setText(event.name);
                viewMonth.setText(getEventStartMonth());
                viewDate.setText(getEventStartDay());
                viewDesc.setText(event.description);
                //set actionbar title
                actionBar.setTitle(event.name);
            }
        });

        // track user data
        viewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                viewOwner.setText(user.firstname + " " + user.lastname);
            }
        });

        // pass event to view model
        viewModel.fetchEvent(id);
    }

    private String getEventStartMonth(){
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        return df.format("MMM", this.event.start_date).toString();
    }

    private String getEventStartDay(){
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        return df.format("dd", this.event.start_date).toString();
    }
}
