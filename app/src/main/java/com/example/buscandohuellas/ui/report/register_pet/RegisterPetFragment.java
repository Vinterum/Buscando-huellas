package com.example.buscandohuellas.ui.report.register_pet;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.buscandohuellas.R;
import com.example.buscandohuellas.databinding.FragmentRegisterPetBinding;
import com.example.buscandohuellas.ui.report.ReportViewModel;

public class RegisterPetFragment extends Fragment {

    private FragmentRegisterPetBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ReportViewModel notificationsViewModel =
                new ViewModelProvider(this).get(ReportViewModel.class);

        binding = FragmentRegisterPetBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        String[] type = new String[] {"pug", "caniche", "pastor alemán", "mixto"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.drop_down_item, type);
        AutoCompleteTextView autoCompleteTextView = binding.filledRaza;
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), autoCompleteTextView.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}