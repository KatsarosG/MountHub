package com.example.mounthub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.example.mounthub.Coordinate;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;





public class WeatherFragment extends Fragment {

    public WeatherFragment() {
        // Necessary empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Connect fragment with XML layout
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        // Call weather API
        String apiKey = "6bb5bbc8a8adc9e045ac5e03e7489f0a"; // Replace with your key
        double latitude = Coordinate.currentLatitude;
        double longitude = Coordinate.currentLongitude;
        String units = "metric";
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude +
                "&lon=" + longitude + "&appid=" + apiKey + "&units=" + units;


        new FetchWeatherTask().execute(apiUrl);

        ImageButton closeButton = view.findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });


        return view;
    }
    private class FetchWeatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String apiUrl = params[0];
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                return result.toString();
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null && getView() != null) {
                try {
                    JSONObject json = new JSONObject(result);
                    JSONObject main = json.getJSONObject("main");
                    JSONArray weatherArray = json.getJSONArray("weather");
                    JSONObject weather = weatherArray.getJSONObject(0);

                    String cityName = json.getString("name");
                    String temp = main.getString("temp");
                    String description = weather.getString("description");
                    String iconCode = weather.getString("icon");

                    TextView cityText = getView().findViewById(R.id.city_text);
                    TextView tempText = getView().findViewById(R.id.temp_text);
                    TextView descText = getView().findViewById(R.id.desc_text);
                    ImageView iconView = getView().findViewById(R.id.weather_icon);

                    cityText.setText("Location: " + cityName);
                    tempText.setText("Temperature: " + temp + "°C");
                    descText.setText("Description: " + description);

                    // Loading picture from URL (π.χ. http://openweathermap.org/img/wn/10d@2x.png)
                    String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";

                    new Thread(() -> {
                        try {
                            InputStream in = new java.net.URL(iconUrl).openStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(in);
                            requireActivity().runOnUiThread(() -> iconView.setImageBitmap(bitmap));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(requireContext(), "Unable to fetch weather data", Toast.LENGTH_SHORT).show();
            }

        }

    }
}


