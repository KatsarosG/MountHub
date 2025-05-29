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
import androidx.core.content.ContextCompat;
import android.widget.Button;
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
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.osmdroid.views.MapView;

public class HomeFragment extends Fragment implements MapListener {

    private static final double ZOOM_BOUND = 16.15;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView mapView;
    private MyLocationNewOverlay locationOverlay;
    private final List<Marker> allMarkers = new ArrayList<>();
    private Map mainMap;
    DatabaseManager databaseManager;
    boolean displayingPins = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Configuration.getInstance().load(requireContext(), PreferenceManager.getDefaultSharedPreferences(requireContext()));
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        Button buttonLayers = root.findViewById(R.id.button4);
        MapView mapView = root.findViewById(R.id.map);
        mainMap = new Map(requireContext(), this, mapView, buttonLayers);

        // databaseManager init
        databaseManager = new DatabaseManager(requireContext());

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
            mapView.addMapListener(this);
        if (mainMap.locationOverlay != null)
            mainMap.locationOverlay.enableMyLocation();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mainMap.mapView != null)
            mainMap.mapView.onPause();
            mapView.removeMapListener(this); 
        if (mainMap.locationOverlay != null)
            mainMap.locationOverlay.disableMyLocation();
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
            Map<String, List<?>> points = databaseManager.pointsNear((float) mapCenter.getLatitude(), (float) mapCenter.getLongitude());

            // load trails on map
            for (Trail trail : (List<Trail>)points.get("trails")) {
                Marker marker = getTrailMarker(trail.getRouteLine().get(0));

                mapView.getOverlays().add(marker);
                allMarkers.add(marker);
            }

            // load locations on map
            for (Location location : (List<Location>)points.get("locations")) {
                Marker marker = getLocationMarker(location.getCoordinates(), location.getID());

                mapView.getOverlays().add(marker);
                allMarkers.add(marker);
            }


            mapView.invalidate();
        }

        return false;
    }

    @NonNull
    private Marker getTrailMarker(Coordinate point) {
        Marker marker = new Marker(mapView);
        marker.setPosition(new GeoPoint(point.getLatitude(), point.getLongitude()));
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setColor(Color.BLUE);
        drawable.setStroke(6, Color.WHITE); // White border
        drawable.setSize(60, 60); // Size in pixels

        marker.setIcon(drawable);
        marker.setInfoWindow(new TrailActionsPopup(mapView));

        return marker;
    }

    @NonNull
    private Marker getLocationMarker(Coordinate point, int locationId) {
        Marker marker = new Marker(mapView);
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
}