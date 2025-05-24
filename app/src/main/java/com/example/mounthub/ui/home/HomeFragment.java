package com.example.mounthub.ui.home;
import com.example.mounthub.Map;
import com.example.mounthub.R;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;

public class HomeFragment extends Fragment {
    //private MapView mapView;
    private Map mainMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Configuration.getInstance().load(requireContext(), PreferenceManager.getDefaultSharedPreferences(requireContext()));
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        Button buttonLayers = root.findViewById(R.id.button4);
        MapView mapView = root.findViewById(R.id.map);
        mainMap = new Map(requireContext(), this, mapView, buttonLayers);

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
