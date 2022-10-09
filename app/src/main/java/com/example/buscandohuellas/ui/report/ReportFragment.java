package com.example.buscandohuellas.ui.report;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.buscandohuellas.MainActivity;
import com.example.buscandohuellas.R;
import com.example.buscandohuellas.databinding.FragmentReportChooseBinding;

import java.util.ArrayList;

public class ReportFragment extends Fragment {

    private FragmentReportChooseBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ReportViewModel notificationsViewModel =
                new ViewModelProvider(this).get(ReportViewModel.class);

        binding = FragmentReportChooseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.reportButton.setOnClickListener(view -> {
            NavDirections navDirections = ReportFragmentDirections.registerPetForm();
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