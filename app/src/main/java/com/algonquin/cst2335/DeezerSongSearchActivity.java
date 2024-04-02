package com.algonquin.cst2335;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
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

import com.algonquin.cst2335.databinding.ActivityDeezerSongSearchBinding;
import com.algonquin.cst2335.databinding.SongBinding;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DeezerSongSearchActivity extends AppCompatActivity {
    private ActivityDeezerSongSearchBinding binding;
    private String tracklistURL;
    private ArrayList<Song> songs;
    private RecyclerView.Adapter<DeezerSongSearchActivity.MyRowHolder> myAdapter;
    private static final String apiUrlBase = "https://api.deezer.com/search/artist/";

    protected void getTracklistURL(String artist, final VolleyCallBack callBack) {
        // Build the API query URL with parameters
        Uri.Builder builder = Uri.parse(apiUrlBase).buildUpon();
        builder.appendQueryParameter("q", artist);
        String url = builder.build().toString();

        // Clear the existing tracklist URL
        tracklistURL = "";
        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                tracklistURL = response.getJSONArray("data").getJSONObject(0).getString("tracklist");
            } catch (JSONException e) {
                Toast.makeText(DeezerSongSearchActivity.this, R.string.api_error, Toast.LENGTH_SHORT).show();
            }
            callBack.onSuccess();
        }, error -> Toast.makeText(DeezerSongSearchActivity.this, R.string.api_error, Toast.LENGTH_SHORT).show());

        // Add a request (in this example, called stringRequest) to your RequestQueue.
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    protected void getSongs(String url) {
        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONArray data = response.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject songObj = data.getJSONObject(i);
                    JSONObject albumObj = songObj.getJSONObject("album");
                    Song song = new Song(songObj.getString("title"), songObj.getInt("duration"), albumObj.getString("title"), albumObj.getString("cover_medium"));
                    songs.add(song);
                }
                myAdapter.notifyItemInserted(songs.size() - 1);
                binding.songRecyclerView.scrollToPosition(Objects.requireNonNull(binding.songRecyclerView.getAdapter()).getItemCount() - 1);
            } catch (JSONException e) {
                Toast.makeText(DeezerSongSearchActivity.this, R.string.api_error, Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(DeezerSongSearchActivity.this, R.string.api_error, Toast.LENGTH_SHORT).show());

        // Add a request (in this example, called stringRequest) to your RequestQueue.
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate layout using data binding
        binding = ActivityDeezerSongSearchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        // Set toolbar as action bar
        Toolbar toolbar = binding.DeezerToolbar;
        setSupportActionBar(toolbar);

        SongViewModel songModel = new ViewModelProvider(this).get(SongViewModel.class);
        songs = songModel.songs.getValue();
        if (songs == null) {
            songModel.songs.setValue(songs = new ArrayList<>());
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> runOnUiThread(() -> binding.songRecyclerView.setAdapter(myAdapter)));
        }

        // The SharedPreferences saves the user's search term
        SharedPreferences prefs = getSharedPreferences("DeezerData", Context.MODE_PRIVATE);
        binding.artistInputText.setText(prefs.getString("Artist", ""));

        // Lookup button listener
        binding.searchButton.setOnClickListener(click -> {
            // Retrieve latitude and longitude
            String artist = binding.artistInputText.getText().toString();

            // Remove existing data in RecyclerView
            int size = songs.size();
            songs.clear();
            myAdapter.notifyItemRangeRemoved(0, size);
            // Call API to fetch artist's songs
            getTracklistURL(artist, () -> getSongs(tracklistURL));
        });

        binding.songRecyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<DeezerSongSearchActivity.MyRowHolder>() {
            // Inflate view for each item in RecyclerView
            @NonNull
            @Override
            public DeezerSongSearchActivity.MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                SongBinding binding = SongBinding.inflate(getLayoutInflater());
                return new MyRowHolder(binding.getRoot());
            }

            // Bind data to ViewHolder
            @Override
            public void onBindViewHolder(@NonNull DeezerSongSearchActivity.MyRowHolder holder, int position) {
                holder.songTextView.setText(songs.get(position).getTitle());
                holder.itemView.setOnClickListener(v -> {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, SongDetails.class);
                    intent.putExtra("Song", songs.get(position));
                    intent.putExtra("hideDelete", true);
                    intent.putExtra("hideSave", false);
                    context.startActivity(intent);
                });
            }

            // Get total number of items in RecyclerView
            @Override
            public int getItemCount() {
                return songs.size();
            }
        });
        // Configure RecyclerView layout manager
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(false);
        binding.songRecyclerView.setLayoutManager(llm);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.deezer_menu, menu);
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            //noinspection RestrictedApi
            m.setOptionalIconsVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.deezer_help) {
            AlertDialog.Builder builder = new AlertDialog.Builder(DeezerSongSearchActivity.this);
            builder.setMessage(R.string.deezer_help_message).setTitle(R.string.deezer_help_title).setPositiveButton(R.string.ok, (dialog, cl) -> {
            }).create().show();
        } else if (id == R.id.deezer_favorite_songs) {
            Intent intent = new Intent(this, FavoriteSongsActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences("DeezerData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Artist", binding.artistInputText.getText().toString());
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences prefs = getSharedPreferences("DeezerData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Artist", binding.artistInputText.getText().toString());
        editor.apply();
    }

    static class MyRowHolder extends RecyclerView.ViewHolder {
        TextView songTextView;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            songTextView = itemView.findViewById(R.id.songTextView);
        }
    }
}
