package com.algonquin.cst2335;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.algonquin.cst2335.databinding.ActivitySongDetailsBinding;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Activity for displaying the details of a specific song.
 * In this activity, users can view song details, and depending on the provided intents,
 * they have the option to save or delete the song from their favorites.
 */
public class SongDetails extends AppCompatActivity {
    private SongDAO sDAO;

    /**
     * Initializes the activity. This method sets up the user interface for displaying song details.
     * It also configures the save and delete functionality based on the provided intents.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           being previously shut down, this Bundle contains the data
     *                           it most recently supplied in onSaveInstanceState(Bundle).
     *                           Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate layout using data binding
        com.algonquin.cst2335.databinding.ActivitySongDetailsBinding binding = ActivitySongDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        SongDatabase db = Room.databaseBuilder(getApplicationContext(), SongDatabase.class, "song.db").build();
        sDAO = db.sDAO();

        if (Objects.requireNonNull(getIntent().getExtras()).getBoolean("hideSave")) {
            binding.saveButton.setVisibility(View.GONE);
        }

        if (getIntent().getExtras().getBoolean("hideDelete")) {
            binding.deleteButton.setVisibility(View.GONE);
        }

        Song song = (Song) getIntent().getSerializableExtra("Song");
        assert song != null;
        binding.songTitleTextView.setText(song.getTitle());
        binding.songDurationTextView.setText(String.valueOf(song.getDuration()));
        binding.albumTitleTextView.setText(song.getAlbum_title());
        Picasso.get().load(song.getAlbum_cover()).into(binding.albumCoverImageView);

        binding.saveButton.setOnClickListener(clk -> {
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                List<Song> mySongs = sDAO.getSongs(song.getTitle());
                if (mySongs.isEmpty()) {
                    song.id = sDAO.insertSong(song);
                }
            });
            Toast.makeText(this, R.string.saved_successfully, Toast.LENGTH_SHORT).show();
        });

        binding.deleteButton.setOnClickListener(clk -> {
            Executor thread = Executors.newSingleThreadExecutor();
            AlertDialog.Builder builder = new AlertDialog.Builder(SongDetails.this);
            builder.setMessage(R.string.deezer_confirm_message).setTitle(R.string.question)
                    .setNegativeButton(R.string.no, (dialog, cl) -> {
                    })
                    .setPositiveButton(R.string.yes, (dialog, cl) -> {
                        thread.execute(() -> sDAO.deleteSong(song));
                        Snackbar.make(binding.deleteButton, getResources().getString(R.string.deezer_deletion_confirmation), Snackbar.LENGTH_LONG)
                                .setAction(R.string.undo, click -> thread.execute(() -> sDAO.insertSong(song)))
                                .show();
                    }).create().show();
        });
    }
}