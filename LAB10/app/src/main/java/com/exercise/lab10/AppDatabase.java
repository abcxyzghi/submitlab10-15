package com.exercise.lab10;

import androidx.room.Database;
import androidx.room.RoomDatabase;

// Database class annotated with @Database, specifying the entity (Person) and the version
@Database(entities = {Person.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PersonDao personDao();  // Abstract method to access the PersonDao
}
