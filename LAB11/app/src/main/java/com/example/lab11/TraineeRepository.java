package com.example.lab11;

import com.example.lab11.interfaces.TraineeService;

public class TraineeRepository {

    public static TraineeService getTraineeService() {
        return APIClient.getClient().create(TraineeService.class);
    }
}
