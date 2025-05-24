package com.example.mounthub.ui.home;
import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.ContentProviderCompat.requireContext;

import com.example.mounthub.Map;
import com.example.mounthub.R;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
//import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;

public class HomeFragment extends Fragment {
    //private MapView mapView;
    private Map mainMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Configuration.getInstance().load(requireContext(), PreferenceManager.getDefaultSharedPreferences(requireContext()));
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        Button buttonLayers = root.findViewById(R.id.button4);
        MapView mapView = root.findViewById(R.id.map);
        mainMap = new Map(requireContext(), this, mapView);

        buttonLayers.setOnClickListener(v -> {
            android.widget.PopupMenu popup = new android.widget.PopupMenu(requireContext(), v);
            popup.getMenu().add("Default");
            popup.getMenu().add("OpenTopoMap");
            popup.getMenu().add("Cyclosm");

            popup.setOnMenuItemClickListener(item -> {
                String title = item.getTitle().toString();
                switch (title) {
                    case "Default":
                        mainMap.mapView.setTileSource(TileSourceFactory.MAPNIK);
                        break;

                    case "OpenTopoMap":
                        XYTileSource satellite = new XYTileSource(
                                "OpenTopoMap",
                                0, 19, 256, ".png",
                                new String[]{
                                       "https://a.tile.opentopomap.org/"
                                        // Δεν υπάρχει tileserver για satellite που να βολεύει αρα χρησιμοποιεί κατι άκυρο.
                                },
                                "OpenTopoMap");
                        mainMap.mapView.setTileSource(satellite);
                        break;

                    case "Cyclosm":
                        XYTileSource terrain = new XYTileSource(
                                "Cyclosm",
                                0, 17, 256, ".png",
                                new String[]{
                                        "https://a.tile-cyclosm.openstreetmap.fr/cyclosm/"
                                },
                                "Cyclosm");
                        mainMap.mapView.setTileSource(terrain);
                        break;
                }
                mainMap.mapView.invalidate(); // Refresh the map
                return true;
            });

            popup.show();
        });

        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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
