package com.example.lab11.interfaces;

import com.example.lab11.models.Trainee;

import java.util.ArrayList;

public interface TraineeCallback {
    void onTraineesLoaded(ArrayList<Trainee> trainees);
}
