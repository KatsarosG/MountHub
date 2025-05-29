package com.example.mounthub;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.tabs.TabLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchResultsActivity extends AppCompatActivity {
    private String searchQuery;
    private int selectedTabIndex = 0; // 0: Trails, 1: Locations, 2: Users

    private TabLayout tabLayout;
    private TextView textView;
    private DatabaseManager dbManager;
    private TrailAdapter trailAdapter;
    private LocationAdapter locationAdapter;
    private UserAdapter userAdapter;
    private Context context;
    private RecyclerView locationRecyclerView;
    private RecyclerView trailRecyclerView;
    private RecyclerView userRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_search_results);
        searchQuery = getIntent().getStringExtra("searchQuery");
        dbManager = new DatabaseManager(context);

        trailRecyclerView = findViewById(R.id.trail_recycler_view);
        locationRecyclerView = findViewById(R.id.location_recycler_view);
        userRecyclerView = findViewById(R.id.user_recycler_view);

        tabLayout = findViewById(R.id.tab_layout);
        // Listen for tab changes
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedTabIndex = tab.getPosition(); // 0 = Trails, 1 = Locations, 2 = Users
                switch (selectedTabIndex) {
                    case 0:
                        locationRecyclerView.setVisibility(View.INVISIBLE);
                        userRecyclerView.setVisibility(View.INVISIBLE);
                        Trail[] resultTrails = dbManager.searchForTrails(searchQuery);
                        trailRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                        trailAdapter = new TrailAdapter(resultTrails);
                        trailRecyclerView.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        trailRecyclerView.setVisibility(View.INVISIBLE);
                        locationRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                        Location[] resultLocations = dbManager.searchForLocations(searchQuery);
                        locationAdapter = new LocationAdapter(resultLocations);
                        locationRecyclerView.setAdapter(locationAdapter);
                        locationRecyclerView.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        trailRecyclerView.setVisibility(View.INVISIBLE);
                        locationRecyclerView.setVisibility(View.INVISIBLE);
                        userRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                        User[] resultUsers = dbManager.searchForUsers(searchQuery);
                        userAdapter = new UserAdapter(resultUsers);
                        userRecyclerView.setVisibility(View.VISIBLE);
                        dbManager.searchForUsers(searchQuery);
                        break;
                }

                refreshSearchResults();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        locationRecyclerView.setVisibility(View.INVISIBLE);
        userRecyclerView.setVisibility(View.INVISIBLE);
        Trail[] resultTrails = dbManager.searchForTrails(searchQuery);
        trailRecyclerView = findViewById(R.id.trail_recycler_view);
        trailRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        trailAdapter = new TrailAdapter(resultTrails);
        refreshSearchResults();
    }

    void refreshSearchResults() {
        trailRecyclerView.setAdapter(trailAdapter);
        locationRecyclerView.setAdapter(locationAdapter);
        userRecyclerView.setAdapter(userAdapter);
    }
}
