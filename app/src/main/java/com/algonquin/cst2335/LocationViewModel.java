package com.algonquin.cst2335;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class LocationViewModel extends ViewModel {
    public MutableLiveData<ArrayList<Location>> locations = new MutableLiveData<>();
}
