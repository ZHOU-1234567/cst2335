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

public class FavoriteSongsActivity extends AppCompatActivity {
    private ActivityFavoriteSongsBinding binding;
    private SongDAO sDAO;
    private ArrayList<Song> songs;
    private RecyclerView.Adapter<FavoriteSongsActivity.MyRowHolder> myAdapter;

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

    static class MyRowHolder extends RecyclerView.ViewHolder {
        TextView songTextView;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            songTextView = itemView.findViewById(R.id.songTextView);
        }
    }
}