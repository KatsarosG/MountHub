package com.example.mounthub;

import android.app.Activity;
import android.content.Intent;
import android.widget.SearchView;

public class SearchManage {

    private final Activity activity;

    public SearchManage(Activity activity, SearchView searchView) {
        this.activity = activity;

        searchView.setImeOptions(android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                openSearchResults(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Optional: live filtering
                return false;
            }
        });
    }

    private void openSearchResults(String query) {
        Intent intent = new Intent(activity, SearchResultsActivity.class);
        intent.putExtra("query", query);
        activity.startActivity(intent);
    }
}

