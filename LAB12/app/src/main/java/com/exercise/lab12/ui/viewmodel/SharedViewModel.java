package com.exercise.lab12.ui.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

// ViewModel to share data across different fragments
public class SharedViewModel extends ViewModel {
    // Holds an integer value, initially 0
    private final MutableLiveData<Integer> sharedData = new MutableLiveData<>(0);
    // Getter to observe changes in shared data
    public MutableLiveData<Integer> getSharedData() {
        return sharedData;
    }
    // Method to increment the shared data by 1
    public void incrementData() {
        if (sharedData.getValue() != null) {
            sharedData.setValue((sharedData.getValue() + 1)); // Increment the value by 1
        } else {
            sharedData.setValue(1); // Set it to 1 if null
        }
    }
}
