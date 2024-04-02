package com.algonquin.cst2335;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
/**
 * ViewModel class for managing Location data and UI-related data in a lifecycle-conscious way.
 */
public class LocationViewModel extends ViewModel {
    /** MutableLiveData object containing a list of Location objects. */
    public MutableLiveData<ArrayList<Location>> locations = new MutableLiveData<>();
}
