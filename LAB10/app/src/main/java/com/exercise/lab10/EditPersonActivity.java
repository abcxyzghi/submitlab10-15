package com.exercise.lab10;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

public class EditPersonActivity extends AppCompatActivity {
    private EditText edtFirstName;
    private EditText edtLastName;

    private Button btnSave;
    private int personId;           // ID of the person being edited

    private Intent intent;          // Intent to receive data from previous activity
    private AppDatabase database;   // Database instance

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_edit_person);        // Set the layout

        Toolbar toolbar = findViewById(R.id.toolbarEdit);   // Find toolbar by its ID
        setSupportActionBar(toolbar);                       // Set the toolbar

        // Enable "back" navigation in the toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Edit");         // Set toolbar title
        }

        initViews(); // Initialize the views
        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app-database").build();

        intent = getIntent(); // Get the intent passed to this activity
        if (intent != null && intent.hasExtra(Constants.UPDATE_Person_Id)) {            // Check if the intent has the update ID
            btnSave.setText("Update");                                                  // Change button text to "Update"
            personId = intent.getIntExtra(Constants.UPDATE_Person_Id, -1);    // Get the person ID

            // Load the person data from the database in a background thread
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    Person person = database.personDao().loadPersonById(personId);       // Load the person by ID
                    populateUI(person);                                                  // Populate the UI with the person's data
                }
            });
        }
    }

    // Populate the EditTexts with the person's data
    private void populateUI(Person person) {
        if (person == null) {
            return; // If person is null, return early
        }

        edtFirstName.setText(person.getFirstName()); // Set the first name
        edtLastName.setText(person.getLastName());   // Set the last name
    }

    // Initialize the views in the layout
    private void initViews() {
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() { // handle the save button's click
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });
    }

    // Save button's click event logic
    public void onSaveButtonClicked() {
        String firstName = edtFirstName.getText() != null ? edtFirstName.getText().toString() : ""; // Get first name from EditText
        String lastName = edtLastName.getText() != null ? edtLastName.getText().toString() : "";    // Get last name from EditText

        if (firstName.isEmpty() || lastName.isEmpty()) {  // Check if both fields are filled
            Toast.makeText(this, "Please enter both first name and last name.", Toast.LENGTH_SHORT).show();
            return;
        }

        final Person person = new Person(firstName, lastName); // Create a new Person object

        // Save or update the person in a background thread
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (getIntent() != null && !getIntent().hasExtra(Constants.UPDATE_Person_Id)) {
                    database.personDao().insert(person);    // Insert a new person
                } else {
                    person.setUid(personId);                // Set the person's ID
                    database.personDao().update(person);    // Update the existing person
                }
                finish();                                   // Finish the activity after saving
            }
        });
    }

    // Handle "back" navigation in the toolbar
    @Override
    public boolean onSupportNavigateUp() {
        finish();   // Finish the activity when navigating up
        return true;
    }

    // Handle menu item clicks
    @Override
    public  boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();   // Finish activity if "back" button is clicked
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


