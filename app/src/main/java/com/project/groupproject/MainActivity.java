package com.project.groupproject;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

import com.project.groupproject.adapters.ListEventsAdapter;
import com.project.groupproject.fragment.CreateEventFragment;
import com.project.groupproject.fragment.EventsListFragment;
import com.project.groupproject.fragment.UserEditFragment;
import com.project.groupproject.fragment.UserInfoFragment;
import com.project.groupproject.models.Event;
import com.project.groupproject.models.User;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements UserInfoFragment.OnUserInfoFragmentListener,
        UserEditFragment.OnUserEditFragmentListener,
        CreateEventFragment.CreateEventFragmentListener
        {


    public final static String TAG = "group_project";
    ListView listView;
    String[] title;
    String[] description;
    int[] icon;

    // fragments
    CreateEventFragment fragmentCreateEvent;
    EventsListFragment fragmentEventsList;
    UserInfoFragment fragmentUserInfo;
    UserEditFragment fragmentUserEdit;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = fragmentEventsList;
                    break;

                case R.id.navigation_dashboard:
                    selectedFragment = fragmentCreateEvent;
                    break;

                case R.id.navigation_notifications:
                    selectedFragment = fragmentUserInfo;
                    break;
            }
            return loadFragment(selectedFragment);
        }
    };

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_view, fragment)
                    .commit();
            return true;
        }
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            Drawable background = this.getResources().getDrawable(R.drawable.header_bg);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//            window.setStatusBarColor(this.getResources().getColor(android.R.color.transparent));
//            window.setNavigationBarColor(this.getResources().getColor(android.R.color.transparent));
//            window.setBackgroundDrawable(background);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create fragments
        fragmentEventsList = EventsListFragment.getInstance();
        fragmentUserInfo = UserInfoFragment.newInstance();
        fragmentUserEdit = new UserEditFragment();
        fragmentCreateEvent = CreateEventFragment.getInstance();

        //Load navigation
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

//        final ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();

        // load home fragment by default
        navigation.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (!TextUtils.isEmpty(s)){
                    fragmentEventsList.applyFilter(s);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id==R.id.action_settings){
            //do your functionality here
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveUserInfo(User user) {
        loadFragment(fragmentUserInfo);
    }

    @Override
    public void onEditButtonClicked() {
        loadFragment(fragmentUserEdit);
    }

    @Override
    public void onCreated(String id) {
        loadFragment(fragmentEventsList);

        Intent intent = new Intent(this, SingleEventActivity.class);
        intent.putExtra("event_id", id);
        startActivity(intent);
    }
}
