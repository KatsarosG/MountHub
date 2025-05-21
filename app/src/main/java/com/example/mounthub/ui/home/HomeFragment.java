package com.example.mounthub.ui.home;
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
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView mapView;
    private MyLocationNewOverlay locationOverlay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Configuration.getInstance().load(requireContext(),
                PreferenceManager.getDefaultSharedPreferences(requireContext()));

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mapView = root.findViewById(R.id.map);
        mapView.setMultiTouchControls(true);

        Button buttonLayers = root.findViewById(R.id.button4);

        // Set initial zoom level and center the map
        setInitialMapView();

        mapView.setTilesScaledToDpi(true);

        requestPermissionsIfNecessary();

        buttonLayers.setOnClickListener(v -> {
            android.widget.PopupMenu popup = new android.widget.PopupMenu(requireContext(), v);
            popup.getMenu().add("Default");
            popup.getMenu().add("Satellite");
            popup.getMenu().add("Terrain");

            popup.setOnMenuItemClickListener(item -> {
                String title = item.getTitle().toString();
                switch (title) {
                    case "Default":
                        mapView.setTileSource(TileSourceFactory.MAPNIK);
                        break;

                    case "Satellite":
                        XYTileSource satellite = new XYTileSource(
                                "Satellite",
                                0, 19, 256, ".png",
                                new String[]{
                                       "https://a.tile.opentopomap.org/"
                                        // Δεν υπάρχει tileserver για satellite που να βολεύει αρα χρησιμοποιεί κατι άκυρο.
                                },
                                "Satellite View");
                        mapView.setTileSource(satellite);
                        break;

                    case "Terrain":
                        XYTileSource terrain = new XYTileSource(
                                "Terrain",
                                0, 17, 256, ".png",
                                new String[]{
                                        "https://a.tile-cyclosm.openstreetmap.fr/cyclosm/"
                                },
                                "Cyclosm");
                        mapView.setTileSource(terrain);
                        break;
                }
                mapView.invalidate(); // Refresh the map
                return true;
            });

            popup.show();
        });

        return root;
    }

    private void setInitialMapView() {
        // Set default center location (e.g., user's last known location or a fixed location)
        GeoPoint defaultLocation = new GeoPoint(38.548165, 22.051305);
        mapView.getController().setCenter(defaultLocation);
        mapView.getController().setZoom(8.0);

        // Optionally, you can adjust zoom based on the user's location or other criteria
    }

    private void setupMyLocationOverlay() {
        locationOverlay = new MyLocationNewOverlay(
                new GpsMyLocationProvider(requireContext()),
                mapView
        );
        // Get the default arrow icon (Vector Drawable)
        Drawable arrowIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.arrow_icon);

        // Change its color programmatically
        if (arrowIconDrawable != null) {
            Bitmap arrowIconBitmap = drawableToBitmap(arrowIconDrawable);
            locationOverlay.setDirectionArrow(arrowIconBitmap, arrowIconBitmap);  // Apply the colored arrow
        }
        locationOverlay.enableMyLocation();
        locationOverlay.enableFollowLocation();
        mapView.getOverlays().add(locationOverlay);
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        // Create a Bitmap with the same size as the drawable
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // Create a Canvas and draw the drawable onto the bitmap
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private void requestPermissionsIfNecessary() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE
            );
        } else {
            setupMyLocationOverlay();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupMyLocationOverlay();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) mapView.onResume();
        if (locationOverlay != null) locationOverlay.enableMyLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) mapView.onPause();
        if (locationOverlay != null) locationOverlay.disableMyLocation();
    }
}
