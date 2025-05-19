package com.example.mounthub;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

public class TrailActionsPopup extends InfoWindow {
        public TrailActionsPopup(MapView mapView) {
            super(R.layout.trail_actions_popup, mapView);
        }

        @Override
        public void onOpen(Object item) {
            Marker marker = (Marker) item;
            View view = mView;

            Button button1 = view.findViewById(R.id.button1);
            Button button2 = view.findViewById(R.id.button2);
            Button button3 = view.findViewById(R.id.button3);

            button1.setOnClickListener(v -> {
                Toast.makeText(view.getContext(), "Action 1 clicked!", Toast.LENGTH_SHORT).show();
                // handle action
            });

            button2.setOnClickListener(v -> {
                Toast.makeText(view.getContext(), "Action 2 clicked!", Toast.LENGTH_SHORT).show();
            });

            button3.setOnClickListener(v -> {
                Toast.makeText(view.getContext(), "Action 3 clicked!", Toast.LENGTH_SHORT).show();
            });
        }

        @Override
        public void onClose() {
            // Optional: handle when popup closes
    }

}
