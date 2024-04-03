package com.algonquin.cst2335;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.algonquin.cst2335.databinding.ActivityFavoriteSongsBinding;
import com.algonquin.cst2335.databinding.SongBinding;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * This activity displays a list of favorite songs saved by the user.
 * It utilizes a Room database to retrieve and display the saved song information.
 */
public class FavoriteSongsActivity extends AppCompatActivity {
    private ActivityFavoriteSongsBinding binding;
    private SongDAO sDAO;
    private ArrayList<Song> songs;
    private RecyclerView.Adapter<FavoriteSongsActivity.MyRowHolder> myAdapter;

    /**
     * Initializes the activity, sets up the Room database and DAO for song retrieval,
     * and configures the RecyclerView to display the list of favorite songs.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down, this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle). Otherwise, it is null.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Room database and DAO
        SongDatabase db = Room.databaseBuilder(getApplicationContext(), SongDatabase.class, "song.db").build();
        sDAO = db.sDAO();
        // Inflate layout using data binding
        binding = ActivityFavoriteSongsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        FavoriteSongViewModel songModel = new ViewModelProvider(this).get(FavoriteSongViewModel.class);
        songs = songModel.songs.getValue();
        if (songs == null) {
            songModel.songs.setValue(songs = new ArrayList<>());
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> runOnUiThread(() -> binding.favoriteSongsRecyclerView.setAdapter(myAdapter)));
        }

        binding.favoriteSongsRecyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<FavoriteSongsActivity.MyRowHolder>() {
            // Inflate view for each item in RecyclerView
            @NonNull
            @Override
            public FavoriteSongsActivity.MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                SongBinding binding = SongBinding.inflate(getLayoutInflater());
                return new MyRowHolder(binding.getRoot());
            }

            // Bind data to ViewHolder
            @Override
            public void onBindViewHolder(@NonNull FavoriteSongsActivity.MyRowHolder holder, int position) {
                holder.songTextView.setText(songs.get(position).getTitle());
                holder.itemView.setOnClickListener(v -> {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, SongDetails.class);
                    intent.putExtra("Song", songs.get(position));
                    intent.putExtra("hideDelete", false);
                    intent.putExtra("hideSave", true);
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
        binding.favoriteSongsRecyclerView.setLayoutManager(llm);
    }
    /**
     * Called when the activity starts interacting with the user.
     * It refreshes the list of favorite songs from the database.
     */
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        songs.clear();
        myAdapter.notifyDataSetChanged();
        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> songs.addAll(sDAO.getAllSongs()));
        FavoriteSongViewModel songModel = new ViewModelProvider(this).get(FavoriteSongViewModel.class);
        songModel.songs.setValue(songs);
        myAdapter.notifyDataSetChanged();
    }
    /**
     * ViewHolder class for displaying each favorite song in the RecyclerView.
     */
    static class MyRowHolder extends RecyclerView.ViewHolder {
        TextView songTextView;

        /**
         * Constructor for MyRowHolder.
         *
         * @param itemView The view of the RecyclerView item.
         */
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            songTextView = itemView.findViewById(R.id.songTextView);
        }
    }
}