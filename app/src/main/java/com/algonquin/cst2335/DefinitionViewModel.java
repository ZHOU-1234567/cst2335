package com.algonquin.cst2335;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class DefinitionViewModel extends ViewModel {
    public MutableLiveData<ArrayList<TermDefinition>> definitions = new MutableLiveData<>();
}
