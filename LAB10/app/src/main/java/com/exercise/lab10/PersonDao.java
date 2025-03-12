package com.exercise.lab10;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao // Annotation to mark this interface as a DAO for Room database
public interface PersonDao {

    @Query("SELECT * FROM person")
    List<Person> getAll(); // Method to return a list of all Person records

    @Query("SELECT * FROM person WHERE uid IN (:personId)")
    Person loadPersonById(int personId); // Method to load a person based on their ID

    @Query("SELECT * FROM person WHERE first_name LIKE :first AND last_name LIKE :last LIMIT 1")
    Person findByName(String first, String last); // Method to search for a person by first and last name

    @Insert
    void insert(Person person); // Insert a new person record

    @Update
    void update(Person person); // Update an existing person

    @Delete
    void delete(Person person); // Delete a person record from the database
}
