package com.example.mounthub;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.core.content.ContextCompat;

import com.example.mounthub.R;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.Date;
import java.util.List;

public class Map {
    public static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    public MyLocationNewOverlay locationOverlay;
    private final Context context;
    private final Fragment fragment;
    public String background;
    private List<Location> locationList;
    private List<Trail> trailList;
    public MapView mapView;

    public Map(Context context, Fragment fragment, MapView mapView) {
        this.context = context;
        this.fragment = fragment;
        this.background = "Default";
        this.mapView = mapView;
        this.mapView.setMultiTouchControls(true);
        this.mapView.setTilesScaledToDpi(true);
        setInitialMapView();
    }
    private void setInitialMapView() {
        // Set default center location (e.g., user's last known location or a fixed location)
        GeoPoint defaultLocation = new GeoPoint(38.548165, 22.051305);
        mapView.getController().setCenter(defaultLocation);
        mapView.getController().setZoom(8.0);
        requestPermissionsIfNecessary(fragment);
        // Optionally, you can adjust zoom based on the user's location or other criteria
    }
    private void requestPermissionsIfNecessary(Fragment fragment) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            fragment.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
        } else {
            setupMyLocationOverlay();
        }
    }

    public void setupMyLocationOverlay() {
        locationOverlay = new MyLocationNewOverlay(
                new GpsMyLocationProvider(context),
                mapView
        );
        // Get the default arrow icon (Vector Drawable)
        Drawable arrowIconDrawable = ContextCompat.getDrawable(context, R.drawable.arrow_icon);

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
}