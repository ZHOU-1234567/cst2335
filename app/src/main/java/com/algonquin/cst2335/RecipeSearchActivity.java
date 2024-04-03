package com.algonquin.cst2335;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity for searching recipes. This class represents a screen where users can interact with
 * the recipe search feature of the application.
 */

public class RecipeSearchActivity extends AppCompatActivity {
    /**
            * Called when the activity is starting. This is where most initialization should go:
            * calling setContentView(int) to inflate the activity's UI, using findViewById(int)
            * to programmatically interact with widgets in the UI, calling managedQuery(Uri, String[], String, String[], String),
     * etc.
     *
             * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
            *                           Otherwise, it is null. This Bundle is not guaranteed to be present, hence any
     *                           data restored should be checked for nullity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_search);
    }
}
