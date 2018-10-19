package com.project.groupproject;


import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ListView;
import android.widget.TextView;
import com.project.groupproject.adapters.ListEventsAdapter;
import com.project.groupproject.fragment.UserEditFragment;
import com.project.groupproject.fragment.UserInfoFragment;
import com.project.groupproject.models.Event;
import com.project.groupproject.models.User;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements UserInfoFragment.OnFragmentInteractionListener,
        UserEditFragment.OnUserEditFragmentInteractionListener{

    ListView listView;
    ListEventsAdapter adapter;
    String[] title;
    String[] description;
    int[] icon;
    ArrayList<Event> arrayList = new ArrayList<>();


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    break;

                case R.id.navigation_dashboard:
                    selectedFragment = new UserInfoFragment();
                    break;

                case R.id.navigation_notifications:
                    selectedFragment = new UserEditFragment();
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
                    .replace(R.id.container, fragment)
                    .commit();
            return true;
        }
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Load navigation
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Top events near you");

        title = new String[]{"Neighbor Community Potluck", "Andre Nickatina", "DON DIABLO", "DIM SUM Making", "My BizDay Vancouver"};
        description = new String[]{"Date: Tue, 16 Oct 2018\nTime: 6:00pm – 8:00pm",
                "Date: Fri, 16 Nov 2018\nTime: 9:00pm – 1:00am",
                "Date: Sun, 11 Nov 2018\nTime: 8:00pm - 1:00am",
                "Date: Wed, 21 Nov 2018\nTime: 6:30pm – 9:00pm",
                "Date: Thu, 18 Oct 2018\nTime: 10:00am – 5:00pm"};
        icon = new int[]{R.drawable.event1, R.drawable.event2, R.drawable.event3, R.drawable.event4, R.drawable.event5};

        listView = findViewById(R.id.listView);

        for (int i =0; i<title.length; i++){
            Event model = new Event(title[i], description[i], icon[i]);
            //bind all strings in an array
            arrayList.add(model);
        }

        //pass results to listViewAdapter class
        adapter = new ListEventsAdapter(this, arrayList);

        //bind the adapter to the listview
        listView.setAdapter(adapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (TextUtils.isEmpty(s)){
                    adapter.filter("");
                    listView.clearTextFilter();
                }
                else {
                    adapter.filter(s);
                }
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

    }

    @Override
    public void onEditButtonClicked() {

    }
}
