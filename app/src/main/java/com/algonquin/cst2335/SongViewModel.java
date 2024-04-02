package com.algonquin.cst2335;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class SongViewModel extends ViewModel {
    /**
     * MutableLiveData object containing a list of Song objects.
     */
    public MutableLiveData<ArrayList<Song>> songs = new MutableLiveData<>();
}
