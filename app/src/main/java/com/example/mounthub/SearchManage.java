package com.example.mounthub;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;

import com.google.android.material.textfield.TextInputEditText;

public class SearchManage {
    private Context context;

    public SearchManage(Context context, TextInputEditText searchInput) {
        this.context = context;

        searchInput.setOnEditorActionListener((v, actionId, event) -> {
            // Trigger when "done" or Enter key is pressed
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                            event.getAction() == KeyEvent.ACTION_DOWN)) {

                String query = searchInput.getText().toString().trim();
                if (!query.isEmpty()) {
                    Log.d("SearchManage", "Search query: " + query);

                    // Launch search results activity
                    Intent intent = new Intent(context, SearchResultsActivity.class);
                    intent.putExtra("searchQuery", query);
                    context.startActivity(intent);
                }
                return true; // Consume event
            }
            return false; // Let system handle other keys
        });
    }
}
