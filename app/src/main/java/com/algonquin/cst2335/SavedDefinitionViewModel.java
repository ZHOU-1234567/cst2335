package com.algonquin.cst2335;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class SavedDefinitionViewModel extends ViewModel {
    public MutableLiveData<ArrayList<TermDefinition>> definitions = new MutableLiveData<>();
}
