package com.example.mounthub.ui.home;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mounthub.Map;
import com.example.mounthub.R;
import com.example.mounthub.NearbyLocActivity;

import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;

public class HomeFragment extends Fragment {

    private Map mainMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Load osmdroid preferences
        Configuration.getInstance().load(requireContext(), PreferenceManager.getDefaultSharedPreferences(requireContext()));

        // Inflate the layout
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Get views
        Button buttonLayers = root.findViewById(R.id.button4);
        Button locationsBtn = root.findViewById(R.id.locationsnear_btn);
        MapView mapView = root.findViewById(R.id.map);

        // Initialize custom Map class
        mainMap = new Map(requireContext(), this, mapView, buttonLayers);

        locationsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), NearbyLocActivity.class);
            startActivity(intent);
        });


        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == mainMap.REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mainMap.setupMyLocationOverlay();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mainMap.mapView != null)
            mainMap.mapView.onResume();
        if (mainMap.locationOverlay != null)
            mainMap.locationOverlay.enableMyLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mainMap.mapView != null)
            mainMap.mapView.onPause();
        if (mainMap.locationOverlay != null)
            mainMap.locationOverlay.disableMyLocation();
    }
}
