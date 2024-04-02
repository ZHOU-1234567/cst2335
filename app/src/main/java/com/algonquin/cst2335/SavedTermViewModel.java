package com.algonquin.cst2335;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class SavedTermViewModel extends ViewModel {
    public MutableLiveData<ArrayList<String>> savedTerms = new MutableLiveData<>();
}
