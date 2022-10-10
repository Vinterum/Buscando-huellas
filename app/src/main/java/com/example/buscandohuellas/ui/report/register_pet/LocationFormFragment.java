package com.example.buscandohuellas.ui.report.register_pet;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.buscandohuellas.R;
import com.example.buscandohuellas.databinding.FragmentLocationFormBinding;
import com.example.buscandohuellas.databinding.FragmentRegisterPetBinding;

public class LocationFormFragment extends Fragment {

    private FragmentLocationFormBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLocationFormBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }
}