package com.algonquin.cst2335;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;

import com.algonquin.cst2335.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Toolbar toolbar = binding.materialToolbar;
        setSupportActionBar(toolbar);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
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

        if (id == R.id.sunrise_and_sunset_lookup) {
            startActivity(new Intent(MainActivity.this, SunriseAndSunsetActivity.class));
        } else if (id == R.id.recipe_search) {
            startActivity(new Intent(MainActivity.this, RecipeSearchActivity.class));
        } else if (id == R.id.dictionary) {
            startActivity(new Intent(MainActivity.this, DictionaryActivity.class));
        } else if (id == R.id.deezer_song_search) {
            startActivity(new Intent(MainActivity.this, DeezerSongSearchActivity.class));
        }

        return true;
    }
}