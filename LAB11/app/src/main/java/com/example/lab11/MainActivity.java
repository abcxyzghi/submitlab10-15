package com.example.lab11;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab11.interfaces.TraineeService;
import com.example.lab11.models.Trainee;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText etName, etEmail, etPhone, etGender;
    Button btnSave;
    TraineeService traineeService;

    private long currentId = 0;

    private boolean validateInputs() {

        boolean isValidated = true;

        if (etName.getText().toString().isEmpty()) {
            etName.setError("Name is required");
            isValidated = false;
        }

        if (etEmail.getText().toString().isEmpty()) {
            etEmail.setError("Email is required");
            isValidated = false;
        }

        if (etPhone.getText().toString().isEmpty()) {
            etPhone.setError("Phone is required");
            isValidated = false;
        }

        if (etGender.getText().toString().isEmpty()) {
            etGender.setError("Gender is required");
            isValidated = false;
        }

        return isValidated;
    }

    Trainee getTraineeFromForm() {
        return new Trainee(etName.getText().toString(), etEmail.getText().toString(),
                etPhone.getText().toString(), etGender.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Binding
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etGender = findViewById(R.id.etGender);
        btnSave = findViewById(R.id.btnSave);

        Intent updateIntent = getIntent();

        Bundle bundle = updateIntent.getExtras();

        if (bundle != null) {
            long id = bundle.getLong("id");
            String name = bundle.getString("name");
            String email = bundle.getString("email");
            String phone = bundle.getString("phone");
            String gender = bundle.getString("gender");

            currentId = id;
            etName.setText(name);
            etEmail.setText(email);
            etPhone.setText(phone);
            etGender.setText(gender);
        }

        traineeService = TraineeRepository.getTraineeService();

        btnSave.setOnClickListener(view -> {
            save();
        });

    }

    private void save() {
        boolean isValidated = validateInputs();

        if (!isValidated) {
            return;
        }

        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String phone = etPhone.getText().toString();
        String gender = etGender.getText().toString();

        Trainee trainee = new Trainee(name, email, phone, gender);

        try {

            boolean isUpdate = currentId != 0;

            if (!isUpdate) {
                //Create
                Call<Trainee> createCall = traineeService.createTrainee(trainee);

                createCall.enqueue(new Callback<Trainee>() {
                    @Override
                    public void onResponse(Call<Trainee> call, Response<Trainee> response) {
                        if (response.body() != null) {
                            Toast.makeText(MainActivity.this, "Create successfully", Toast.LENGTH_SHORT).show();

                            Intent returnIntent = new Intent();
                            setResult(RESULT_OK, returnIntent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<Trainee> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Create fail", Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }

            //Update
            Call<Trainee> updateCall = traineeService.updateTrainee(currentId, getTraineeFromForm());

            updateCall.enqueue(new Callback<Trainee>() {
                @Override
                public void onResponse(Call<Trainee> call, Response<Trainee> response) {
                    if (response.body() != null) {
                        Toast.makeText(MainActivity.this, "Save successfully", Toast.LENGTH_SHORT).show();
                        Intent returnIntent = new Intent();
                        setResult(RESULT_OK, returnIntent);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Trainee> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Update fail", Toast.LENGTH_SHORT).show();
                }
            });


        } catch (Exception ex) {
            Log.d("Error", Objects.requireNonNull(ex.getMessage()));
        }
    }

}