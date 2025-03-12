package com.example.lab11;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.lab11.interfaces.TraineeCallback;
import com.example.lab11.interfaces.TraineeService;
import com.example.lab11.models.Trainee;
import com.example.lab11.utils.DialogUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class TraineesActivity extends AppCompatActivity {

    ListView lvTrainees;
    TraineeService traineeService;
    Button btnCreate, btnUpdate, btnDelete;
    TraineesAdapter adapter;
    ActivityResultLauncher<Intent> createActivityResultLauncher, updateActivityResultLauncher;

    private void performDeleteTrainee() {
        Trainee trainee = (Trainee) adapter.getItem(adapter.selectingIndex);

        Call<Trainee> deleteCall = traineeService.deleteTrainee(trainee.getId());

        deleteCall.enqueue(new Callback<Trainee>() {
            @Override
            public void onResponse(Call<Trainee> call, Response<Trainee> response) {

                if (response.body() == null) {
                    return;
                }

                getLatestTrainees(trainees -> {
                    // Update UI or notify adapter here
                    adapter.setTrainees(trainees);
                    adapter.notifyDataSetChanged(); // Assuming you have a RecyclerView or ListView adapter
                });
                Toast.makeText(TraineesActivity.this, "Delete success!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Trainee> call, Throwable t) {
                Toast.makeText(TraineesActivity.this, "Delete error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getLatestTrainees(TraineeCallback callback) {
        Call<Trainee[]> call = traineeService.getAllTrainees();

        call.enqueue(new Callback<Trainee[]>() {
            @Override
            public void onResponse(Call<Trainee[]> call, Response<Trainee[]> response) {
                if (response.body() == null) {
                    callback.onTraineesLoaded(new ArrayList<>()); // Return empty list if no data
                    return;
                }

                ArrayList<Trainee> adapterTrainees = new ArrayList<>(Arrays.asList(response.body()));
                callback.onTraineesLoaded(adapterTrainees); // Pass data back via callback
            }

            @Override
            public void onFailure(Call<Trainee[]> call, Throwable t) {
                // Handle failure
                callback.onTraineesLoaded(new ArrayList<>()); // Return empty list on failure
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trainees);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lvTrainees = findViewById(R.id.lvTrainees);
        btnCreate = findViewById(R.id.btnCreate);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        traineeService = TraineeRepository.getTraineeService();

        Call<Trainee[]> call = traineeService.getAllTrainees();

        call.enqueue(new Callback<Trainee[]>() {
            @Override
            public void onResponse(Call<Trainee[]> call, Response<Trainee[]> response) {

                if (response.body() == null) {
                    return;
                }

                ArrayList<Trainee> trainees = new ArrayList<>(Arrays.asList(response.body()));

                adapter = new TraineesAdapter(TraineesActivity.this, trainees);

                lvTrainees.setAdapter(adapter);

                lvTrainees.setOnItemClickListener((adapterView, view, i, l) -> {
                    adapter.setSelectedItemIndex(i);
                });
            }

            @Override
            public void onFailure(Call<Trainee[]> call, Throwable t) {
                Toast.makeText(TraineesActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        createActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        getLatestTrainees(trainees -> {
                            // Update UI or notify adapter here
                            adapter.setTrainees(trainees);
                            adapter.notifyDataSetChanged(); // Assuming you have a RecyclerView or ListView adapter
                        });
                    }
                });

        updateActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        getLatestTrainees(trainees -> {
                            // This will be called after the trainees are loaded
                            adapter.setTrainees(trainees);
                            // Update UI or notify adapter here
                            adapter.notifyDataSetChanged(); // Assuming you have a RecyclerView or ListView adapter
                        });
                    }
                });

        btnCreate.setOnClickListener(view -> {
            Intent intent = new Intent(TraineesActivity.this, MainActivity.class);

            createActivityResultLauncher.launch(intent);
        });

        btnUpdate.setOnClickListener(view -> {

            Trainee trainee = (Trainee) adapter.getItem(adapter.selectingIndex);

            Intent intent = new Intent(TraineesActivity.this, MainActivity.class);

            Bundle bundle = new Bundle();

            bundle.putLong("id", trainee.getId());
            bundle.putString("name", trainee.getName());
            bundle.putString("email", trainee.getEmail());
            bundle.putString("phone", trainee.getPhone());
            bundle.putString("gender", trainee.getGender());

            intent.putExtras(bundle);

            startActivity(intent);
        });

        btnDelete.setOnClickListener(view -> {

            DialogUtils.showConfirmationDialog(TraineesActivity.this, "Confirm", "Delete this trainee?",
                    this::performDeleteTrainee);

        });

    }
}