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
import androidx.room.Room;

import com.algonquin.cst2335.databinding.ActivityDictionaryBinding;
import com.algonquin.cst2335.databinding.DefinitionBinding;
import com.algonquin.cst2335.databinding.TermBinding;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DictionaryActivity extends AppCompatActivity {
    private ActivityDictionaryBinding binding;
    private ArrayList<TermDefinition> myDefinitions;
    private ArrayList<String> savedTerms;
    private RecyclerView.Adapter<MyRowHolder> myAdapter;
    private RecyclerView.Adapter<MySavedTermRowHolder> mySavedTermAdapter;
    private TermDefinitionDAO dDAO;

    private static final String apiUrlBase = "https://api.dictionaryapi.dev/api/v2/entries/en/";

    protected void updateSearchResult(String term) {
        // Build the API query URL with parameters
        Uri.Builder builder = Uri.parse(apiUrlBase).buildUpon();
        builder.appendEncodedPath(term);
        String url = builder.build().toString();

        // Request a string response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONArray meanings = response.getJSONObject(i).getJSONArray("meanings");
                    for (int j = 0; j < meanings.length(); j++) {
                        JSONArray definitions = meanings.getJSONObject(j).getJSONArray("definitions");
                        for (int k = 0; k < definitions.length(); k++) {
                            TermDefinition termDefinition = new TermDefinition(binding.searchTermInput.getText().toString(), definitions.getJSONObject(k).getString("definition"));
                            myDefinitions.add(termDefinition);
                        }
                    }
                }
                myAdapter.notifyItemInserted(myDefinitions.size() - 1);
                binding.definitionsRecyclerView.scrollToPosition(Objects.requireNonNull(binding.definitionsRecyclerView.getAdapter()).getItemCount() - 1);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, error -> Toast.makeText(DictionaryActivity.this, R.string.api_error, Toast.LENGTH_SHORT).show());

        // Add a request (in this example, called stringRequest) to your RequestQueue.
        VolleySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDictionaryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Toolbar toolbar = binding.dictionaryToolbar;
        setSupportActionBar(toolbar);

        TermDefinitionDatabase db = Room.databaseBuilder(getApplicationContext(), TermDefinitionDatabase.class, "definition.db").build();
        dDAO = db.dDAO();

        DefinitionViewModel definitionModel = new ViewModelProvider(this).get(DefinitionViewModel.class);
        myDefinitions = definitionModel.definitions.getValue();
        if (myDefinitions == null) {
            definitionModel.definitions.setValue(myDefinitions = new ArrayList<>());
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> runOnUiThread(() -> binding.definitionsRecyclerView.setAdapter(myAdapter)));
        }

        SavedTermViewModel savedTermViewModel = new ViewModelProvider(this).get(SavedTermViewModel.class);
        savedTerms = savedTermViewModel.savedTerms.getValue();
        if (savedTerms == null) {
            savedTermViewModel.savedTerms.setValue(savedTerms = new ArrayList<>());
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                savedTerms.addAll(dDAO.getAllTerms());
                runOnUiThread(() -> binding.savedTermsRecyclerView.setAdapter(mySavedTermAdapter));
            });
        }

        // The SharedPreferences saves the user's search term
        SharedPreferences prefs = getSharedPreferences("DictionaryData", Context.MODE_PRIVATE);
        binding.searchTermInput.setText(prefs.getString("Term", ""));

        // Lookup button listener
        binding.searchButton.setOnClickListener(click -> {
            // Retrieve the term to search
            String term = binding.searchTermInput.getText().toString();

            // Clear existing data
            int size = myDefinitions.size();
            myDefinitions.clear();
            myAdapter.notifyItemRangeRemoved(0, size);

            // Call API to update term search results
            updateSearchResult(term);

            // Enable Save button
            binding.saveButton.setEnabled(true);
        });

        // Save button listener
        binding.saveButton.setOnClickListener(click -> {
            // Retrieve the term to search
            String term = binding.searchTermInput.getText().toString();

            if (!savedTerms.contains(term)) {
                savedTerms.add(term);
                mySavedTermAdapter.notifyItemInserted(savedTerms.size() - 1);
                binding.savedTermsRecyclerView.scrollToPosition(Objects.requireNonNull(binding.savedTermsRecyclerView.getAdapter()).getItemCount() - 1);

                // Insert definitions to database
                for (int i = 0; i < myDefinitions.size(); i++) {
                    TermDefinition definition = new TermDefinition(binding.searchTermInput.getText().toString(), myDefinitions.get(i).getDefinition());
                    Executor thread = Executors.newSingleThreadExecutor();
                    thread.execute(() -> definition.id = dDAO.insertDefinition(definition));
                }
            }

            binding.saveButton.setEnabled(false);
        });

        binding.savedTermsRecyclerView.setAdapter(mySavedTermAdapter = new RecyclerView.Adapter<MySavedTermRowHolder>() {
            @NonNull
            @Override
            public MySavedTermRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                TermBinding binding = TermBinding.inflate(getLayoutInflater());
                return new MySavedTermRowHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MySavedTermRowHolder holder, int position) {
                String term = savedTerms.get(position);
                holder.savedTermText.setText(term);
                holder.itemView.setOnClickListener(v -> {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, SavedDefinitionsActivity.class);
                    intent.putExtra("term", term);
                    context.startActivity(intent);
                });
            }

            @Override
            public int getItemCount() {
                return savedTerms.size();
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        binding.savedTermsRecyclerView.setLayoutManager(llm);

        binding.definitionsRecyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                DefinitionBinding binding = DefinitionBinding.inflate(getLayoutInflater());
                return new MyRowHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                holder.definitionText.setText(myDefinitions.get(position).getDefinition());
            }

            @Override
            public int getItemCount() {
                return myDefinitions.size();
            }
        });

        LinearLayoutManager myllm = new LinearLayoutManager(this);
        myllm.setStackFromEnd(true);
        binding.definitionsRecyclerView.setLayoutManager(myllm);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dictionary_menu, menu);
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

        if (id == R.id.dictionary_help) {
            AlertDialog.Builder builder = new AlertDialog.Builder(DictionaryActivity.this);
            builder.setMessage(R.string.dictionary_help_message).setTitle(R.string.dictionary_help_title).setPositiveButton(R.string.ok, (dialog, cl) -> {
            }).create().show();
        } else if (id == R.id.dictionary_terms) {
            if (binding.savedTermsRecyclerView.getVisibility() == View.GONE) {
                binding.savedTermsRecyclerView.setVisibility(View.VISIBLE);
                item.setTitle(R.string.hide_terms);
            } else {
                binding.savedTermsRecyclerView.setVisibility(View.GONE);
                item.setTitle(R.string.show_terms);
            }
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences("DictionaryData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Term", binding.searchTermInput.getText().toString());
        editor.apply();
    }

    static class MyRowHolder extends RecyclerView.ViewHolder {
        TextView definitionText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            definitionText = itemView.findViewById(R.id.definitionText);
        }
    }

    static class MySavedTermRowHolder extends RecyclerView.ViewHolder {
        TextView savedTermText;

        public MySavedTermRowHolder(@NonNull View itemView) {
            super(itemView);
            savedTermText = itemView.findViewById(R.id.savedTermText);
        }
    }
}
