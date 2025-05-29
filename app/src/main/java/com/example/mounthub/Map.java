package com.example.mounthub;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class Map {

    private Context context;
    private MapView mapView;
    private MyLocationNewOverlay locationOverlay;


    public Map(Context context, MapView mapView) {
        this.context = context;
        this.mapView = mapView;
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
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }
}

