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

/**
 * The main activity of the application, serving as the primary user interface.
 * This activity presents options to navigate to different features of the app,
 * such as Sunrise and Sunset Lookup, Recipe Search, Dictionary, and Deezer Song Search.
 */

public class MainActivity extends AppCompatActivity {

    /**
     * Initializes the activity. Sets up the user interface with a toolbar
     * and navigation options to various features of the app.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           being previously shut down, this Bundle contains the data
     *                           it most recently supplied in onSaveInstanceState(Bundle).
     *                           Otherwise, it is null.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Toolbar toolbar = binding.materialToolbar;
        setSupportActionBar(toolbar);
    }
    /**
     * Initializes the contents of the Activity's standard options menu.
     * This menu is populated with items defined in a menu resource file.
     *
     * @param menu The options menu in which items are placed.
     * @return true for the menu to be displayed; if false, it will not be shown.
     */
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
    /**
     * Handles action bar item clicks. The action bar will automatically
     * handle clicks on the Home/Up button, as long as a parent activity is specified in AndroidManifest.xml.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to proceed, true to consume it here.
     */
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