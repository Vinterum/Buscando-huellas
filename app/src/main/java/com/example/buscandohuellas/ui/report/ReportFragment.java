package com.example.buscandohuellas.ui.report;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.buscandohuellas.databinding.FragmentReportChooseBinding;

public class ReportFragment extends Fragment {

    private FragmentReportChooseBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ReportViewModel notificationsViewModel =
                new ViewModelProvider(this).get(ReportViewModel.class);

        binding = FragmentReportChooseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.reportButton.setOnClickListener(view -> {
            NavDirections navDirections = ReportFragmentDirections.toRegisterPetForm();
            Navigation.findNavController(view).navigate(navDirections);
        });

        binding.sightingButton.setOnClickListener(view -> {
            NavDirections navDirections = ReportFragmentDirections.toSightingForm();
            Navigation.findNavController(view).navigate(navDirections);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}