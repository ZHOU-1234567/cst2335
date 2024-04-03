package com.algonquin.cst2335;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.algonquin.cst2335.databinding.ActivitySunriseAndSunsetBinding;
import com.algonquin.cst2335.databinding.LocationBinding;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * This activity displays sunrise and sunset information for given locations.
 */
public class SunriseAndSunsetActivity extends AppCompatActivity {
    private ActivitySunriseAndSunsetBinding binding;
    private ArrayList<Location> locations;
    private RecyclerView.Adapter<MyRowHolder> myAdapter;
    private LocationDAO lDAO;

    private static final String apiUrlBase = "https://api.sunrisesunset.io/json";

    protected void updateSunriseSunset(String lat, String lng) {
        // Build the API query URL with parameters
        Uri.Builder builder = Uri.parse(apiUrlBase).buildUpon();
        builder.appendQueryParameter("lat", lat);
        builder.appendQueryParameter("lng", lng);
        builder.appendQueryParameter("timezone", "UTC");
        builder.appendQueryParameter("date", "today");
        String url = builder.build().toString();

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            Log.e("HttpClient", "success! response: " + response.toString());
            try {
                binding.sunriseTextView.setText(String.format("%s: %s", getResources().getString(R.string.sunrise), response.getJSONObject("results").getString("sunrise")));
                binding.sunsetTextView.setText(String.format("%s: %s", getResources().getString(R.string.sunset), response.getJSONObject("results").getString("sunset")));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, error -> {
            Toast.makeText(SunriseAndSunsetActivity.this, R.string.api_error, Toast.LENGTH_SHORT).show();
            Log.e("HttpClient", "error: " + error.toString());
        });

        // Add a request (in this example, called stringRequest) to your RequestQueue.
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * Called when the activity is created.
     * Initializes the UI components and sets up event listeners.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Room database and DAO
        LocationDatabase db = Room.databaseBuilder(getApplicationContext(), LocationDatabase.class, "location.db").build();
        lDAO = db.lDAO();
        // Inflate layout using data binding
        binding = ActivitySunriseAndSunsetBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        // Set toolbar as action bar
        Toolbar toolbar = binding.sunriseSunsetToolbar;
        setSupportActionBar(toolbar);
        // Initialize ViewModel to manage location data
        LocationViewModel locationModel = new ViewModelProvider(this).get(LocationViewModel.class);
        locations = locationModel.locations.getValue();
        // Load locations from database if not already loaded
        if (locations == null) {
            locationModel.locations.setValue(locations = new ArrayList<>());
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                locations.addAll(lDAO.getAllLocations());
                runOnUiThread(() -> binding.recyclerView.setAdapter(myAdapter));
            });
        }

        // The SharedPreferences saves the user's search term
        SharedPreferences prefs = getSharedPreferences("SunriseSunsetData", Context.MODE_PRIVATE);
        binding.latitudeInput.setText(prefs.getString("Latitude", ""));
        binding.longitudeInput.setText(prefs.getString("Longitude", ""));

        // Lookup button listener
        binding.lookupButton.setOnClickListener(click -> {
            // Retrieve latitude and longitude
            String lat = binding.latitudeInput.getText().toString();
            String lng = binding.longitudeInput.getText().toString();

            // Clear the result textview content
            binding.sunriseTextView.setText("");
            binding.sunsetTextView.setText("");

            // Call API to update sunrise and sunset info
            updateSunriseSunset(lat, lng);
        });

        // Add to Favorites button listener
        binding.addToFavoritesButton.setOnClickListener(click -> {
            Location location = new Location(binding.latitudeInput.getText().toString(), binding.longitudeInput.getText().toString());
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> location.id = lDAO.insertLocation(location));
            locations.add(location);
            myAdapter.notifyItemInserted(locations.size() - 1);
            binding.recyclerView.scrollToPosition(Objects.requireNonNull(binding.recyclerView.getAdapter()).getItemCount() - 1);
        });
        // Set up RecyclerView and its adapter
        binding.recyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            // Inflate view for each item in RecyclerView
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LocationBinding binding = LocationBinding.inflate(getLayoutInflater());
                return new MyRowHolder(binding.getRoot());
            }

            // Bind data to ViewHolder
            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                holder.latitudeText.setText(locations.get(position).getLatitude());
                holder.longitudeText.setText(locations.get(position).getLongitude());
            }

            // Get total number of items in RecyclerView
            @Override
            public int getItemCount() {
                return locations.size();
            }
        });
        // Configure RecyclerView layout manager
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(llm);
    }

    /**
     * Called to create options menu.
     *
     * @param menu The options menu in which you place your items.
     * @return Returns true for the menu to be displayed; if you return false it will not be shown.
     */
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sunrise_sunset_menu, menu);
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            //noinspection RestrictedApi
            m.setOptionalIconsVisible(true);
        }
        return true;
    }

    /**
     * Called when an item in the options menu is selected.
     *
     * @param item The menu item that was selected.
     * @return Returns true to indicate that the event was handled, false otherwise.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sunrise_sunset_help) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SunriseAndSunsetActivity.this);
            builder.setMessage(R.string.sunrise_sunset_help_message).setTitle(R.string.sunrise_sunset_help_title).setPositiveButton(R.string.ok, (dialog, cl) -> {
            }).create().show();
        } else if (id == R.id.sunrise_sunset_locations) {
            if (binding.recyclerView.getVisibility() == View.GONE) {
                binding.recyclerView.setVisibility(View.VISIBLE);
                item.setTitle(R.string.hide_locations);
            } else {
                binding.recyclerView.setVisibility(View.GONE);
                item.setTitle(R.string.show_locations);
            }
        }
        return true;
    }

    /**
     * Called when the activity is paused.
     * Saves latitude and longitude inputs to SharedPreferences.
     */
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences("SunriseSunsetData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Latitude", binding.latitudeInput.getText().toString());
        editor.putString("Longitude", binding.longitudeInput.getText().toString());
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences prefs = getSharedPreferences("SunriseSunsetData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Latitude", binding.latitudeInput.getText().toString());
        editor.putString("Longitude", binding.longitudeInput.getText().toString());
        editor.apply();
    }

    /**
     * ViewHolder class for RecyclerView items.
     */
    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView latitudeText;
        TextView longitudeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(clk -> {
                int position = getAbsoluteAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder(SunriseAndSunsetActivity.this);
                builder.setMessage(R.string.sunrise_sunset_confirm_message).setTitle(R.string.question)
                        .setNegativeButton(R.string.no, (dialog, cl) -> {
                        })
                        .setPositiveButton(R.string.yes, (dialog, cl) -> {
                            Location removedLocation = locations.get(position);
                            Executor thread = Executors.newSingleThreadExecutor();
                            thread.execute(() -> lDAO.deleteLocation(removedLocation));
                            locations.remove(position);
                            myAdapter.notifyItemRemoved(position);
                            binding.recyclerView.scrollToPosition(Objects.requireNonNull(binding.recyclerView.getAdapter()).getItemCount() - 1);
                            Snackbar.make(latitudeText, getResources().getString(R.string.sunrise_sunset_deletion_confirmation) + position, Snackbar.LENGTH_LONG).setAction(R.string.undo, click -> {
                                locations.add(position, removedLocation);
                                thread.execute(() -> lDAO.insertLocation(removedLocation));
                                myAdapter.notifyItemInserted(position);
                                binding.recyclerView.scrollToPosition(binding.recyclerView.getAdapter().getItemCount() - 1);
                            }).show();
                        }).create().show();
                return false;
            });

            itemView.setOnClickListener(clk -> {
                int position = getAbsoluteAdapterPosition();
                Location selectedLocation = locations.get(position);
                Executor thread = Executors.newSingleThreadExecutor();
                thread.execute(() -> updateSunriseSunset(selectedLocation.getLatitude(), selectedLocation.getLongitude()));
            });
            latitudeText = itemView.findViewById(R.id.latitudeText);
            longitudeText = itemView.findViewById(R.id.longitudeText);
        }
    }
}
