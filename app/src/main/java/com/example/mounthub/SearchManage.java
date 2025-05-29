package com.example.mounthub;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.tabs.TabLayout;

public class SearchManage {
    private Context context;
    private String searchQuery;

    public SearchManage(Context context, TextInputEditText searchInput) {
        this.context = context;

        searchInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                            event.getAction() == KeyEvent.ACTION_DOWN)) {

                searchQuery = searchInput.getText().toString().trim();
                if (!searchQuery.isEmpty()) {
                    //Log.d("SearchManage", "Search query: " + searchQuery);

                    // Launch search results activity
                    Intent intent = new Intent(context, SearchResultsActivity.class);
                    intent.putExtra("searchQuery", searchQuery);
                    context.startActivity(intent);
                }
                return true;
            }
            return false;
        });
    }
}
