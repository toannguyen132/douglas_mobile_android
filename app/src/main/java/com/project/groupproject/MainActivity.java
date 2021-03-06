package com.project.groupproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.project.groupproject.fragment.CreateEventFragment;
import com.project.groupproject.fragment.EventCreatedFragment;
import com.project.groupproject.fragment.EventLikedFragment;
import com.project.groupproject.fragment.EventsListFragment;
import com.project.groupproject.fragment.UserEditFragment;
import com.project.groupproject.fragment.UserInfoFragment;
import com.project.groupproject.models.User;
import com.project.groupproject.lib.BottomNavigationViewHelper;


public class MainActivity extends AppCompatActivity implements UserInfoFragment.OnUserInfoFragmentListener,
        UserEditFragment.OnUserEditFragmentListener,
        CreateEventFragment.CreateEventFragmentListener,
        EventLikedFragment.OnFragmentInteractionListener,
        EventCreatedFragment.OnFragmentInteractionListener
        {

    public final static int LOCATION_PERMISSION = 1;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // create fragments
        fragmentEventsList = EventsListFragment.getInstance();
        fragmentUserInfo = UserInfoFragment.newInstance();
        fragmentUserEdit = new UserEditFragment();
        fragmentCreateEvent = CreateEventFragment.getInstance();

        //Load navigation
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 26, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 26, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }

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
