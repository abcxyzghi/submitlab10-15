package com.exercise.lab12.ui.dashboard;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.exercise.lab12.R;
import com.exercise.lab12.databinding.FragmentDashboardBinding;
import com.exercise.lab12.ui.viewmodel.SharedViewModel;
// Fragment for the Dashboard screen
public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;// Binding object for UI elements

    private SharedViewModel sharedViewModel; // ViewModel to share data across fragments

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    // Inflate the layout for this fragment
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false); // Inflate the fragment's layout
        View root = binding.getRoot(); // Get the root view

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel  .class);

        // Set click listener for a button to increment a number in ViewModel
        binding.btnUppNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedViewModel.incrementData();
            }
        });


        // Observe changes to the shared data in ViewModel and update the UI
        sharedViewModel.getSharedData().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer data) {
                binding.textDashboard.setText(String.valueOf(data));
            }
        });

        return root; // Return the root view

    }
}