package com.example.mounthub.ui.home;
import com.example.mounthub.Coordinate;
import com.example.mounthub.DatabaseManager;
import com.example.mounthub.Location;
import com.example.mounthub.LocationManage;
import com.example.mounthub.MainActivity;
import com.example.mounthub.R;
import com.example.mounthub.Trail;
import com.example.mounthub.TrailActionsPopup;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment implements MapListener {

    private static final double ZOOM_BOUND = 16.15;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView mapView;
    private MyLocationNewOverlay locationOverlay;
    private final List<Marker> allMarkers = new ArrayList<>();
    DatabaseManager databaseManager;
    boolean displayingPins = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Configuration.getInstance().load(requireContext(),
                PreferenceManager.getDefaultSharedPreferences(requireContext()));

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mapView = root.findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);

        // Set initial zoom level and center the map
        setInitialMapView();

        requestPermissionsIfNecessary();

        // databaseManager init
        databaseManager = new DatabaseManager(requireContext());

        return root;
    }

    private void setInitialMapView() {
        // Set default center location (e.g., user's last known location or a fixed location)
        GeoPoint defaultLocation = new GeoPoint(48.8583, 2.2944); // Eiffel Tower as an example
        mapView.getController().setCenter(defaultLocation);
        mapView.getController().setZoom(15.0); // Set the zoom level (e.g., 15.0 is a good default for street-level)

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
        if (mapView != null) {
            mapView.onResume();
            mapView.addMapListener(this); // Re-add listener on resume
        }
        if (locationOverlay != null) locationOverlay.enableMyLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
            mapView.removeMapListener(this); // Remove listener on pause
        }
        if (locationOverlay != null) locationOverlay.disableMyLocation();
    }

    // MapListener methods
    @Override
    public boolean onScroll(ScrollEvent event) {
        //TODO(opt): implement
        return false;
    }

    @Override
    public boolean onZoom(ZoomEvent event) {
//        Log.d("MapUpdate", "Map zoomed. New zoom level: " + event.getZoomLevel());
//        Log.d("MapUpdate", "Map center after zoom: " + mapCenter.getLatitude() + ", " + mapCenter.getLongitude());
//        Log.d("MapUpdate", "Map zoomed. New zoom level: " + event.getZoomLevel());
        GeoPoint mapCenter = (GeoPoint) mapView.getMapCenter();

        double zoomLevel = event.getZoomLevel();

        // clear markers
        if (zoomLevel < ZOOM_BOUND && displayingPins) {
            for (Marker marker : allMarkers) {
                mapView.getOverlays().remove(marker);
            }
            displayingPins = false;
            return false;
        }

        // show markers
        if (zoomLevel >= ZOOM_BOUND && !displayingPins) {
            displayingPins = true;
//            Log.d("MapUpdate", "Map zoomed. New zoom level: " + event.getZoomLevel());
            Map<String, List<?>> points = databaseManager.fetchMarkersNearLocation((float) mapCenter.getLatitude(), (float) mapCenter.getLongitude());

            // load trails on map
            for (Trail trail : (List<Trail>)points.get("trails")) {
                Marker marker = getMarker(trail.getRouteLine().get(0), 1);

                mapView.getOverlays().add(marker);
                allMarkers.add(marker);
            }

            // load locations on map
            for (Location location : (List<Location>)points.get("locations")) {
                Marker marker = getMarker(location.getCoordinates(), 0);

                mapView.getOverlays().add(marker);
                allMarkers.add(marker);
            }


            mapView.invalidate();
        }

        return false;
    }

    @NonNull
    private Marker getMarker(Coordinate point, int type) {
        Marker marker = new Marker(mapView);
        marker.setPosition(new GeoPoint(point.getLatitude(), point.getLongitude()));
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

        // Attach custom popup
        if (type == 1) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.OVAL);
            drawable.setColor(Color.BLUE);
            drawable.setStroke(6, Color.WHITE); // White border
            drawable.setSize(60, 60); // Size in pixels

            marker.setIcon(drawable);
            marker.setInfoWindow(new TrailActionsPopup(mapView));
        } else if (type == 0) {
            marker.setOnMarkerClickListener((marker1, mapView) -> {
                Intent intentObj = new Intent(requireContext(), LocationManage.class);
                startActivity(intentObj);

                return true;
            });
        }

        return marker;
    }
}