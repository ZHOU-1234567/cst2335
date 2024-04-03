package com.algonquin.cst2335;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;

/**
 * ViewModel for managing and storing the list of term definitions.
 * This class extends the Android ViewModel class and is used to hold and manage UI-related data in a lifecycle-conscious way.
 * It allows data to survive configuration changes such as screen rotations.
 */
public class DefinitionViewModel extends ViewModel {
    /**
     * LiveData for an ArrayList of TermDefinition objects.
     * MutableLiveData is used here so that we can update the list of definitions and any active observers will be notified of changes.
     */
    public MutableLiveData<ArrayList<TermDefinition>> definitions = new MutableLiveData<>();
}
