package com.exercise.lab12;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.exercise.lab12.databinding.FragmentSecondBinding;
// Fragment for navigating back from SecondFragment to FirstFragment
public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding; // Binding for UI elements


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false); // Inflate the fragment's layout
        return binding.getRoot(); // Return the root view

    }

    // When the view is created, set up navigation between fragments
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set click listener for button to navigate to the SecondFragment
        binding.buttonSecond.setOnClickListener(v ->
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;    // Clean up the binding when the view is destroyed
    }

}