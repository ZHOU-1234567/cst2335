package com.algonquin.cst2335;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
/**
 * ViewModel associated with the FavoriteSongsActivity.
 * It is responsible for preparing and managing the data for the FavoriteSongsActivity,
 * including the handling of favorite song data.
 * This ViewModel exposes a MutableLiveData ArrayList of Song objects
 * which the activity can observe for changes.
 */
public class FavoriteSongViewModel extends ViewModel {
    /**
     * MutableLiveData object containing a list of Song objects.
     */
    public MutableLiveData<ArrayList<Song>> songs = new MutableLiveData<>();
}
