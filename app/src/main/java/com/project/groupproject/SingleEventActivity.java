package com.project.groupproject;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.rpc.Help;
import com.project.groupproject.lib.Helper;
import com.project.groupproject.models.Event;
import com.project.groupproject.models.User;
import com.project.groupproject.viewmodals.EventViewModel;
import com.squareup.picasso.Picasso;

public class SingleEventActivity extends AppCompatActivity
        implements OnMapReadyCallback, Observer<Event>, AppBarLayout.OnOffsetChangedListener {

    TextView viewMonth, viewDate, viewTitle, viewOwner, viewDesc, viewLocation, viewTime, viewLike;
    ImageView viewImage;
//    Button btnLike;
    FloatingActionButton btnLike;
    Event event;
    EventViewModel viewModel;
    SupportMapFragment mapView;
    String currentUid;
    Toolbar toolbar;
    GoogleMap gmap;

    private static final int PERCENTAGE_TO_SHOW_IMAGE = 20;
    private View mFab;
    private int mMaxScrollSize;
    private boolean mIsImageHidden;

    private static final String MAP_VIEW_BUNDLE_KEY = "viewbundlekey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_event);

        //hide actionbar
//        final ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        AppBarLayout appbar = (AppBarLayout) findViewById(R.id.flexible_example_appbar);
        appbar.addOnOffsetChangedListener(this);

        toolbar = findViewById(R.id.toolbarSingleEvent);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        toolbar.setTitleTextAppearance(this, R.style.TitleFont);

        final SingleEventActivity instance = this;

        // setup views
//        viewMonth = findViewById(R.id.view_month);
        viewDate = findViewById(R.id.view_day);
        viewTitle = findViewById(R.id.view_title);
        viewOwner = findViewById(R.id.view_owner);
        viewDesc = findViewById(R.id.view_desc);
        viewLocation = findViewById(R.id.view_location);
//        viewTime = findViewById(R.id.view_time);
        viewLike = findViewById(R.id.view_like);
//        btnLike = findViewById(R.id.btn_like);
        btnLike = findViewById(R.id.likeBtn);
        viewImage = findViewById(R.id.view_image);
//        viewImage.setImageResource(R.drawable.event1);
//        viewImage.setImageResource(R.drawable.logo222);
        mapView = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_view);

        // map init
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);

        //get data from previous activity when item of listview is clicked using intent
        Intent intent = getIntent();
        String id = intent.getStringExtra("event_id");

        // get current user id
//        final String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // button event
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // disable button first
                btnLike.setEnabled(false);
                // the process
                if (isLiked()) {
                    viewModel.unlike(currentUid);
                    btnLike.setImageResource(R.drawable.heart_passive);
                }
                else{
                    viewModel.likeEvent(currentUid);
                    btnLike.setImageResource(R.drawable.heart);
                }

            }
        });

        // create viewModel
        viewModel = ViewModelProviders.of(this).get(EventViewModel.class);

        // track event data
        viewModel.getEvent().observe(this, this);

        // track user data
        viewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                viewOwner.setText("By " +user.firstname + " " + user.lastname);
            }
        });

        // pass event to view model
        viewModel.fetchEvent(id);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String getEventDate(){
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        return df.format("dd MMM yyyy", this.event.start_date).toString();
    }

    private String getEventTime() {
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        return df.format("hh:mm a", this.event.start_date).toString();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setMinZoomPreference(15);
        LatLng coord = new LatLng(event.lat, event.lng);
//        LatLng ny = new LatLng(40.7143528, -74.0059731);

        gmap.addMarker(new MarkerOptions().position(coord).title(event.name));
        gmap.moveCamera(CameraUpdateFactory.newLatLng(coord));
    }

    public boolean isLiked() {
        return event.likes.contains(currentUid);
    }

    public void checkLikeButton() {
        if (isLiked()) {
//            btnLike.setText("Unlike");
            btnLike.setImageResource(R.drawable.heart);
        } else {
//            btnLike.setText("Like");
            btnLike.setImageResource(R.drawable.heart_passive);
        }
        // activate button again
        btnLike.setEnabled(true);
    }

    @Override
    public void onChanged(@Nullable Event newEvent) {
        event = newEvent;
        //set text in textview
        viewTitle.setText(event.name);
//        viewMonth.setText(getEventStartMonth());
        viewDate.setText( getEventDate() + " | " + getEventTime());
        viewDesc.setText(event.description);
        viewLocation.setText(event.location);
//        viewTime.setText(getEventTime());
        viewLike.setText(String.valueOf(event.num_like) + " " + Helper.pluralize(event.num_like, "Like", "Likes"));

        // set image
        if (event.image != null){
//                    viewImage.setImageURI(event.image);
            Picasso.get().load(event.image).into(viewImage);
        } else {
//            viewImage.setImageResource(R.drawable.event1);
            viewImage.setImageResource(R.drawable.logo222);
        }

        //
        checkLikeButton();

        //set toolbar title
        toolbar.setTitle(event.name);

        // render map
        mapView.getMapAsync(this);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0){
            mMaxScrollSize = appBarLayout.getTotalScrollRange();
        }

        int currentScrollPercentage = (Math.abs(i)) * 100
                / mMaxScrollSize;

        if(currentScrollPercentage == 0) {
            toolbar.setTitle("");
        }

        if (currentScrollPercentage >= PERCENTAGE_TO_SHOW_IMAGE) {
            if (!mIsImageHidden) {
                mIsImageHidden = true;
                ViewCompat.animate(mFab).scaleY(0).scaleX(0).start();
            }
        }

        if (currentScrollPercentage < PERCENTAGE_TO_SHOW_IMAGE) {
            if (mIsImageHidden) {
                mIsImageHidden = false;
                ViewCompat.animate(mFab).scaleY(1).scaleX(1).start();
            }
        }
    }
}
