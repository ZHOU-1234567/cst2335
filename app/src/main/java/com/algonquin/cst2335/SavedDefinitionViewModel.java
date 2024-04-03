package com.algonquin.cst2335;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
/**
 * ViewModel for the SavedDefinitionsActivity.
 * It holds and manages UI-related data for the activity in a lifecycle-conscious way,
 * allowing the data to survive configuration changes such as screen rotations.
 * Specifically, this ViewModel stores a list of saved term definitions.
 */
public class SavedDefinitionViewModel extends ViewModel {
    /**
     * MutableLiveData containing a list of TermDefinition objects.
     * This allows the UI to observe changes to the list of saved definitions.
     */
    public MutableLiveData<ArrayList<TermDefinition>> definitions = new MutableLiveData<>();
}
