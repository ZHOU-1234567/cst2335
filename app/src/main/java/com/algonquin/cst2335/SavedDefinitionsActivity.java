package com.algonquin.cst2335;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.algonquin.cst2335.databinding.ActivitySavedDefinitionsBinding;
import com.algonquin.cst2335.databinding.DefinitionBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SavedDefinitionsActivity extends AppCompatActivity {
    private ActivitySavedDefinitionsBinding binding;
    private ArrayList<TermDefinition> definitions;
    private RecyclerView.Adapter<SavedDefinitionsActivity.MyRowHolder> myAdapter;
    private TermDefinitionDAO dDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySavedDefinitionsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        String term = extras.getString("term");

        TermDefinitionDatabase db = Room.databaseBuilder(getApplicationContext(), TermDefinitionDatabase.class, "definition.db").build();
        dDAO = db.dDAO();

        SavedDefinitionViewModel savedDefinitionViewModel = new ViewModelProvider(this).get(SavedDefinitionViewModel.class);
        definitions = savedDefinitionViewModel.definitions.getValue();
        if (definitions == null) {
            savedDefinitionViewModel.definitions.setValue(definitions = new ArrayList<>());
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                definitions.addAll(dDAO.getDefinitions(term));
                runOnUiThread(() -> binding.savedDefinitionsRecyclerView.setAdapter(myAdapter));
            });
        }

        binding.savedDefinitionsRecyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<SavedDefinitionsActivity.MyRowHolder>() {
            @NonNull
            @Override
            public SavedDefinitionsActivity.MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                DefinitionBinding binding = DefinitionBinding.inflate(getLayoutInflater());
                return new SavedDefinitionsActivity.MyRowHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull SavedDefinitionsActivity.MyRowHolder holder, int position) {
                holder.definitionText.setText(definitions.get(position).getDefinition());
            }

            @Override
            public int getItemCount() {
                return definitions.size();
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        binding.savedDefinitionsRecyclerView.setLayoutManager(llm);
    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView definitionText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(clk -> {
                int position = getAbsoluteAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder(SavedDefinitionsActivity.this);
                builder.setMessage(R.string.dictionary_delete_confirm_message).setTitle(R.string.question)
                        .setNegativeButton(R.string.no, (dialog, cl) -> {
                        })
                        .setPositiveButton(R.string.yes, (dialog, cl) -> {
                            TermDefinition removedDefinition = definitions.get(position);
                            Executor thread = Executors.newSingleThreadExecutor();
                            thread.execute(() -> dDAO.deleteDefinition(removedDefinition));
                            definitions.remove(position);
                            myAdapter.notifyItemRemoved(position);
                            binding.savedDefinitionsRecyclerView.scrollToPosition(Objects.requireNonNull(binding.savedDefinitionsRecyclerView.getAdapter()).getItemCount() - 1);
                            Snackbar.make(itemView, getResources().getString(R.string.dictionary_delete_confirmation) + position, Snackbar.LENGTH_LONG).setAction("Undo", click -> {
                                definitions.add(position, removedDefinition);
                                thread.execute(() -> dDAO.insertDefinition(removedDefinition));
                                myAdapter.notifyItemInserted(position);
                                binding.savedDefinitionsRecyclerView.scrollToPosition(binding.savedDefinitionsRecyclerView.getAdapter().getItemCount() - 1);
                            }).show();
                        }).create().show();
            });

            definitionText = itemView.findViewById(R.id.definitionText);
        }
    }
}