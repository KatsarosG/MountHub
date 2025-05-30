package com.example.mounthub;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import androidx.core.content.ContextCompat;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.List;

public class Map {
    public static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    public MyLocationNewOverlay locationOverlay;
    private final Context context;
    private final Fragment fragment;
    public String background;
    private List<Location> locationList;
    private List<Trail> trailList;
    private final Button buttonLayers;
    public MapView mapView;

    public Map(Context context, Fragment fragment, MapView mapView, Button buttonLayers) {
        this.context = context;
        this.fragment = fragment;
        this.background = "Default";
        this.mapView = mapView;
        this.mapView.setMultiTouchControls(true);
        this.mapView.setTilesScaledToDpi(true);
        this.buttonLayers = buttonLayers;
        layerButtonSetup();
        setInitialMapView();
    }

    private void layerButtonSetup() {
        //On Click of Layer Button:
        buttonLayers.setOnClickListener(v -> {
            android.widget.PopupMenu popup = new android.widget.PopupMenu(context, v);
            popup.getMenu().add("OpenStreetMap");
            popup.getMenu().add("OpenTopoMap");
            popup.getMenu().add("Cyclosm");

            // On Click of Layer list item:
            popup.setOnMenuItemClickListener(item -> {
                String title = item.getTitle().toString();
                switch (title) {
                    case "OpenStreetMap":
                        selectOSM();
                        break;

                    case "OpenTopoMap":
                        selectOTM();
                        break;

                    case "Cyclosm":
                        selectCOSM();
                        break;
                }
                mapView.invalidate(); // Refresh the map
                return true;
            });
            popup.show();
        });
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

    public MyLocationNewOverlay setupMyLocationOverlay() {
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

        return locationOverlay;
    }

    public MyLocationNewOverlay returnLocation() {
        locationOverlay = new MyLocationNewOverlay(
                new GpsMyLocationProvider(context),
                mapView
        );

        Drawable arrowIconDrawable = ContextCompat.getDrawable(context, R.drawable.arrow_icon);
        if (arrowIconDrawable != null) {
            Bitmap arrowIconBitmap = drawableToBitmap(arrowIconDrawable);
            locationOverlay.setDirectionArrow(arrowIconBitmap, arrowIconBitmap);
        }

        locationOverlay.enableMyLocation();
        locationOverlay.enableFollowLocation();
        mapView.getOverlays().add(locationOverlay);

        return locationOverlay;
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

    private void selectOSM() {
        mapView.setTileSource(TileSourceFactory.MAPNIK);
    }

    private void selectOTM() {
        XYTileSource openTopoMap = new XYTileSource(
                "OpenTopoMap",
                0, 19, 256, ".png",
                new String[]{"https://a.tile.opentopomap.org/"}, "OpenTopoMap");
        mapView.setTileSource(openTopoMap);
    }
    private void selectCOSM() {
        XYTileSource cyclosm = new XYTileSource(
                "Cyclosm",
                0, 17, 256, ".png",
                new String[]{"https://a.tile-cyclosm.openstreetmap.fr/cyclosm/"}, "Cyclosm");
        mapView.setTileSource(cyclosm);
    }
}