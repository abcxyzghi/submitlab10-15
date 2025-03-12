package com.exercise.lab10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class PersonActivity extends AppCompatActivity {
    private FloatingActionButton fabAdd;    // Floating action button for adding new persons
    private RecyclerView recyclerView;      // RecyclerView to display the list of persons
    private PersonAdapter adapter;          // Adapter for binding person data to RecyclerView
    private AppDatabase database;           // Room database instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar); // Set the toolbar
        setSupportActionBar(toolbar);   // Set support for the toolbar

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle("Home");
        }

        fabAdd = findViewById(R.id.fab);

        // start EditPersonActivity
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PersonActivity.this, EditPersonActivity.class));
            }
        });
        // Set up the RecyclerView
        recyclerView = findViewById(R.id.rvPerson);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PersonAdapter(this); // Initialize the adapter
        recyclerView.setAdapter(adapter);         // Set the adapter on the RecyclerView

        // Build Room database
        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app-database").build();

        // Set up swipe actions on the RecyclerView for deleting items
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // Not supporting drag and drop
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();     // Get the swiped item's position
                        List<Person> tasks = adapter.getTasks();            // Get the list of persons
                        database.personDao().delete(tasks.get(position));   // Delete the swiped person
                        retrieveTasks();                                    // Refresh the list
                    }
                });
            }
        }).attachToRecyclerView(recyclerView);                              // Attach ItemTouchHelper to the RecyclerView
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveTasks(); // Retrieve tasks when the activity is resumed
    }

    // Method to retrieve all persons from the database and update the RecyclerView
    private void retrieveTasks() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<Person> persons = database.personDao().getAll(); // Get all persons from the database
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setTasks(persons);                          // Update the adapter on the UI thread
                    }
                });
            }
        });
    }
}
