package com.exercise.lab12.ui.home;

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
import com.exercise.lab12.databinding.FragmentHomeBinding;
import com.exercise.lab12.ui.viewmodel.SharedViewModel;
// Fragment for the Home screen
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding; // Binding for UI elements

    private SharedViewModel sharedViewModel; // ViewModel for sharing data

    public static HomeFragment newInstance() {
        return new HomeFragment(); // Create a new instance of this fragment
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false); // Inflate the layout for this fragment
        View root = binding.getRoot(); // Get the root view

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class); // Get shared ViewModel

        // Observe shared data and update the UI when data changes
        sharedViewModel.getSharedData().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer data) {
                binding.textHome.setText(String.valueOf(data));
            }
        });

        return root; // Return the root view
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Clean up the binding when the view is destroyed
    }

}