package com.exercise.lab10;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// RecyclerView adapter to display Person data in a list
public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.MyViewHolder> {
    private Context context;            // Context from the activity using this adapter
    private List<Person> personList;    // List of persons to be displayed

    public PersonAdapter(Context context) {
        this.context = context;         // Set the context
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_person,
                viewGroup, false);  // Inflate the layout for each item in the RecyclerView
        return new MyViewHolder(view);         // Return a new view holder instance
    }

    // Bind data (Person) to the views in the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull PersonAdapter.MyViewHolder holder, int i) {
        holder.firstName.setText(personList.get(i).getFirstName());  // Set first name text
        holder.lastName.setText(personList.get(i).getLastName());    // Set last name text
    }

    // Get the number of items in the list
    @Override
    public int getItemCount() {
        if (personList == null) {
            return 0;               // if the list is empty
        }
        return personList.size();   // Otherwise, return the size of the list
    }

    // Method to set the list of persons and notify the adapter of data changes
    public void setTasks(List<Person> personList) {
        this.personList = personList;
        notifyDataSetChanged();         // Notify the adapter
    }

    // Method to get the list of persons
    public List<Person> getTasks() {
        return personList;
    }

    // ViewHolder class to manage the views for each item in the RecyclerView
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView firstName, lastName;
        ImageView editImage;

        MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            firstName = itemView.findViewById(R.id.firstName);  // Find the first name view
            lastName = itemView.findViewById(R.id.lastName);

            editImage = itemView.findViewById(R.id.editIcon);   // Find the edit icon
            editImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int elementId = personList.get(getAdapterPosition()).getUid();   // Get the person ID
                    Intent i = new Intent(context, EditPersonActivity.class);        // Create intent for the edit activity
                    i.putExtra(Constants.UPDATE_Person_Id, elementId);               // Pass the person ID to the edit activity
                    context.startActivity(i);                                        // Start the EditPersonActivity
                }
            });
        }
    }
}
