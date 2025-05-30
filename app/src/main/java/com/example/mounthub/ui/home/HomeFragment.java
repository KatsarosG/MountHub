package com.example.mounthub.ui.home;

import com.example.mounthub.AddTrailActivity;
import com.example.mounthub.LocationHandler;

import com.example.mounthub.Coordinate;
import com.example.mounthub.DatabaseManager;
import com.example.mounthub.Location;
import com.example.mounthub.LocationManage;

import com.example.mounthub.MainActivity;
import com.example.mounthub.R;
import com.example.mounthub.Trail;
import com.example.mounthub.TrailActionsPopup;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import org.osmdroid.config.Configuration;

import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import org.osmdroid.views.overlay.Marker;

import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;
import com.example.mounthub.Map;

public class HomeFragment extends Fragment implements MapListener {

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView mapView;
    private MyLocationNewOverlay locationOverlay;
    private static final double ZOOM_BOUND = 16.15;
    private final List<Marker> allMarkers = new ArrayList<>();
    private Map mainMap;
    DatabaseManager databaseManager;
    boolean displayingPins = false;
   
    private boolean pinMode = false, editMode = false;
    public  boolean recordMode = false;

    private LocationHandler locationHandler;

    public List<GeoPoint> markedTrail = new ArrayList<>();
    public List<Marker> trailMarkers = new ArrayList<>();
    public List<Polyline> trailPolylines = new ArrayList<>();
    public boolean recordPaused = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Configuration.getInstance().load(requireContext(), PreferenceManager.getDefaultSharedPreferences(requireContext()));
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        Button buttonLayers = root.findViewById(R.id.button4);
        MapView mapView = root.findViewById(R.id.map);
        mainMap = new Map(requireContext(), this, mapView, buttonLayers);

        setupMapClickListener();
        locationHandler = new LocationHandler();

        // databaseManager init
        databaseManager = new DatabaseManager(requireContext());

        mainMap.mapView.setMapListener(new org.osmdroid.events.MapListener() {
            @Override
            public boolean onScroll(org.osmdroid.events.ScrollEvent event) {
                GeoPoint center = (GeoPoint) mainMap.mapView.getMapCenter();
                Coordinate.currentLatitude = center.getLatitude();
                Coordinate.currentLongitude = center.getLongitude();
                return true;
            }

            @Override
            public boolean onZoom(org.osmdroid.events.ZoomEvent event) {
                GeoPoint center = (GeoPoint) mainMap.mapView.getMapCenter();
                Coordinate.currentLatitude = center.getLatitude();
                Coordinate.currentLongitude = center.getLongitude();
                return true;
            }
        });

        return root;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupMapClickListener() {
        // put pin
        mainMap.mapView.setOnTouchListener((v, event) -> {
//            Log.d("pinMode", String.valueOf(pinMode));
            if (event.getAction() != MotionEvent.ACTION_DOWN) {
                return false;
            }

            if (pinMode) {
                // Convert screen coordinates to geographic coordinates
                GeoPoint geoPoint = (GeoPoint) mainMap.mapView.getProjection().fromPixels(
                        (int) event.getX(),
                        (int) event.getY()
                );

                // Create a new pin
                Marker newLocPin = new Marker(mainMap.mapView);
                newLocPin.setPosition(geoPoint);

                // Add the newLocPin to the map
                mainMap.mapView.getOverlays().add(newLocPin);

                // Refresh the map
                mainMap.mapView.invalidate();

                // Exit pin adding mode
                pinMode = false;

                // Show next popup
                locationHandler.insertPin(requireContext(), newLocPin, this);

                return true;
            }

            if (editMode) {
                // Convert screen coordinates to geographic coordinates
                GeoPoint geoPoint = (GeoPoint) mainMap.mapView.getProjection().fromPixels(
                        (int) event.getX(),
                        (int) event.getY()
                );


                markedTrail.add(geoPoint);

                // Create a new pin and place it on map
                Marker newLocPin =  getTrailMarker(new Coordinate(geoPoint.getLatitude(), geoPoint.getLongitude()));
                mainMap.mapView.getOverlays().add(newLocPin);

                // make polyline and add to map
                Polyline dynamicLine = new Polyline();
                dynamicLine.setPoints(markedTrail);
                dynamicLine.getOutlinePaint().setColor(Color.RED);
                dynamicLine.getOutlinePaint().setStrokeWidth(15.0f);
                mainMap.mapView.getOverlays().add(dynamicLine);

                // Refresh the map
                mainMap.mapView.invalidate();

                // add marker and polyline to lists for later removal
                trailMarkers.add(newLocPin);
                trailPolylines.add(dynamicLine);

                return true;
            }

            return false;
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == mainMap.REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mainMap.setupMyLocationOverlay();
            }
        }
    }

    public boolean onZoom(ZoomEvent event) {
//        Log.d("MapUpdate", "Map zoomed. New zoom level: " + event.getZoomLevel());
//        Log.d("MapUpdate", "Map center after zoom: " + mapCenter.getLatitude() + ", " + mapCenter.getLongitude());
//        Log.d("MapUpdate", "Map zoomed. New zoom level: " + event.getZoomLevel());
        GeoPoint mapCenter = (GeoPoint) mainMap.mapView.getMapCenter();

        double zoomLevel = event.getZoomLevel();

        // clear markers
        if (zoomLevel < ZOOM_BOUND && displayingPins) {
            for (Marker marker : allMarkers) {
                mainMap.mapView.getOverlays().remove(marker);
            }
            displayingPins = false;
            return false;
        }

        // show markers
        if (zoomLevel >= ZOOM_BOUND && !displayingPins) {
            displayingPins = true;
//            Log.d("MapUpdate", "Map zoomed. New zoom level: " + event.getZoomLevel());
            java.util.Map<String, List<?>> points = databaseManager.pointsNear((float) mapCenter.getLatitude(), (float) mapCenter.getLongitude());

            // load trails on map
            for (Trail trail : (List<Trail>)points.get("trails")) {
                Marker marker = getTrailMarker(trail.getRouteLine().get(0));

                mainMap.mapView.getOverlays().add(marker);
                allMarkers.add(marker);
            }

            // load locations on map
            for (Location location : (List<Location>)points.get("locations")) {
                Marker marker = getLocationMarker(location.getCoordinates(), location.getId());

                mainMap.mapView.getOverlays().add(marker);
                allMarkers.add(marker);
            }


            mainMap.mapView.invalidate();
        }

        return false;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mainMap.mapView != null) {
            mainMap.mapView.onResume();
            mainMap.mapView.addMapListener(this);
        }
        if (mainMap.locationOverlay != null)
            mainMap.locationOverlay.enableMyLocation();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mainMap.mapView != null) {
            mainMap.mapView.onPause();
            mainMap.mapView.removeMapListener(this);
        }
        if (mainMap.locationOverlay != null)
            mainMap.locationOverlay.disableMyLocation();
    }

    // MapListener methods
    @Override
    public boolean onScroll(ScrollEvent event) {
        //TODO(opt): implement
        return false;
    }

    @NonNull
    private Marker getTrailMarker(Coordinate point) {
        Marker marker = new Marker(mainMap.mapView);
        marker.setPosition(new GeoPoint(point.getLatitude(), point.getLongitude()));
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setColor(Color.BLUE);
        drawable.setStroke(6, Color.WHITE);
        drawable.setSize(60, 60);

        marker.setIcon(drawable);
        marker.setInfoWindow(new TrailActionsPopup(mainMap.mapView));

        return marker;
    }

    @NonNull
    private Marker getLocationMarker(Coordinate point, int locationId) {
        Marker marker = new Marker(mainMap.mapView);
        marker.setPosition(new GeoPoint(point.getLatitude(), point.getLongitude()));
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

        marker.setRelatedObject(locationId);
        marker.setOnMarkerClickListener((marker1, mapView) -> {
            Intent intentObj = new Intent(requireContext(), LocationManage.class);
            intentObj.putExtra("locationId", (int) marker1.getRelatedObject());
            startActivity(intentObj);

            return true;
        });

        return marker;
    }

    public void startAddLocationMode() {
        if (!pinMode) {
            locationHandler.startAddLoc(requireContext());
            pinMode = true;
        }
    }

    public void startAddTrailMode() {
        editMode = true;

        AlertDialog addTrailDialog = new AlertDialog.Builder(requireContext())
                .setTitle("Add TrailPoints")
                .setMessage("Plan a route by putting pins on the map")
                .setPositiveButton("Submit", (dialog, which) -> {
                    submitTrail();
                    editMode = false; // exit
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    editMode = false;

                    // Remove all trail polylines from map
                    for (Polyline polyline : trailPolylines) {
                        mainMap.mapView.getOverlays().remove(polyline);
                    }

                    // Clear the lists
                    trailPolylines.clear();

                    // Clear the trail points list
                    markedTrail.clear();

                    mainMap.mapView.invalidate();
                })
                .setCancelable(false) // Prevent dismissing by tapping outside
                .show();

        // Configure dialog window
        Window window = addTrailDialog.getWindow();
        if (window != null) {
            // remove backdrop
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

            // Make background interactable
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

            // Position dialog at bottom of screen
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.BOTTOM;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.y = 50; // Add some margin from bottom
            window.setAttributes(params);

        }
    }

    public void recordMode() {
        recordMode = true;

        AlertDialog addTrailDialog = new AlertDialog.Builder(requireContext())
                .setTitle("Recording...")
                .setMessage("Just walk🤙")
                .setPositiveButton("Submit", (dialog, which) -> {
                    // Handle submit action
                    submitTrail();
                    recordMode = false; // exit
                })
                .setNeutralButton(recordPaused ? "Continue" : "Paused", (dialog, which) -> {
                    toggleRecording();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    recordMode = false;

//                    Log.d("MapUpdate", "Map zoomed. New zoom level: " + trailPolylines);

                    // Remove all trail polylines from map
                    for (Polyline polyline : trailPolylines) {
                        mainMap.mapView.getOverlays().remove(polyline);
                    }

                    // Clear the lists
                    trailMarkers.clear();
                    trailPolylines.clear();

                    // Clear the trail points list
                    markedTrail.clear();

                    mainMap.mapView.invalidate();
                })
                .setCancelable(false) // Prevent dismissing by tapping outside
                .show();

        // Configure dialog window
        Window window = addTrailDialog.getWindow();
        if (window != null) {
            // remove backdrop
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

            // Make background interactable
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

            // Position dialog at bottom of screen
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.BOTTOM;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.y = 50; // Add some margin from bottom
            window.setAttributes(params);

        }
    }

    private void toggleRecording() {
        recordMode = !recordMode;
    }

    private void submitTrail() {
        Intent intentObj = new Intent(requireContext(), AddTrailActivity.class);
        intentObj.putParcelableArrayListExtra("route", (ArrayList<GeoPoint>) markedTrail);

        startActivity(intentObj);
    }
}