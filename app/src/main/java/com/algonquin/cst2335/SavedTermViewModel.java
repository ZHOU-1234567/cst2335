package com.algonquin.cst2335;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;

/**
 * ViewModel for managing and storing the list of saved terms.
 * This class extends the Android ViewModel class and is used to hold and manage UI-related data
 * in a lifecycle-conscious way, allowing the data to survive configuration changes such as screen rotations.
 * It holds a list of saved terms represented as strings, wrapped in MutableLiveData to observe changes.
 */
public class SavedTermViewModel extends ViewModel {
    /**
     * LiveData for an ArrayList of String objects representing saved terms.
     * MutableLiveData is used so that changes to the list of saved terms can be observed and reflected in the UI.
     */
    public MutableLiveData<ArrayList<String>> savedTerms = new MutableLiveData<>();
}
