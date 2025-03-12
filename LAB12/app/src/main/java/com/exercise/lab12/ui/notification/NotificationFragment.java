package com.exercise.lab12.ui.notification;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.exercise.lab12.R;
import com.exercise.lab12.databinding.FragmentNotificationBinding;
import com.exercise.lab12.ui.viewmodel.SharedViewModel;
// Fragment for the Notification screen
public class NotificationFragment extends Fragment {

    private FragmentNotificationBinding binding; // Binding for UI elements

    private SharedViewModel sharedViewModel; // ViewModel for shared data

    // Inflate the layout for this fragment
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater, container, false); // Inflate the layout
        View root = binding.getRoot(); // Get the root view
// Get shared ViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Observe shared data and update the UI when data changes
        sharedViewModel.getSharedData().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer data) {
                binding.textNotification.setText(String.valueOf(data));
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