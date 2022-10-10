package com.example.buscandohuellas.ui.report.register_pet;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.buscandohuellas.R;
import com.example.buscandohuellas.databinding.FragmentRegisterPetBinding;
import com.example.buscandohuellas.ui.report.ReportViewModel;

public class RegisterPetFragment extends Fragment {

    TextView textView;
    Dialog dialog;
    AutoCompleteTextView spinnerSexo;
    AutoCompleteTextView spinnerTamano;
    AutoCompleteTextView spinnerColor;
    AutoCompleteTextView spinnerEdad;
    AutoCompleteTextView spinnerComportamiento;
    private FragmentRegisterPetBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ReportViewModel notificationsViewModel =
                new ViewModelProvider(this).get(ReportViewModel.class);

        binding = FragmentRegisterPetBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //initialize searchable spinner for raza
        textView = binding.ssRaza;
        textView.setOnClickListener(view -> {
            //initialize dialog
            dialog = new Dialog(getActivity());
            //set custom dialog
            dialog.setContentView(R.layout.ss_dialog);
            //set custom width and height
            dialog.getWindow().setLayout(800,950);
            //set transparent background
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            //initialize and assign variable
            EditText editText = dialog.findViewById(R.id.edit_text);
            ListView listView = dialog.findViewById(R.id.list_view);
            //initialize and set array adapter
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.lista_razas, R.layout.ss_list_item);
            listView.setAdapter(adapter);

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    adapter.getFilter().filter(charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            listView.setOnItemClickListener((adapterView, view1, i, l) -> {
                //set selected item on text view
                textView.setText(adapter.getItem(i));
                //dismiss dialog
                dialog.dismiss();
            });
        });

        ArrayAdapter<CharSequence> adapterSexo = ArrayAdapter.createFromResource(getActivity(), R.array.lista_sexo, R.layout.drop_down_item);
        ArrayAdapter<CharSequence> adapterTamano = ArrayAdapter.createFromResource(getActivity(), R.array.lista_tamaño, R.layout.drop_down_item);
        ArrayAdapter<CharSequence> adapterColor = ArrayAdapter.createFromResource(getActivity(), R.array.lista_color, R.layout.drop_down_item);
        ArrayAdapter<CharSequence> adapterEdad = ArrayAdapter.createFromResource(getActivity(), R.array.lista_edad, R.layout.drop_down_item);
        ArrayAdapter<CharSequence> adapterComportamiento = ArrayAdapter.createFromResource(getActivity(), R.array.lista_comportamiento, R.layout.drop_down_item);

        spinnerSexo = binding.filledSexo;
        spinnerTamano = binding.filledTamano;
        spinnerColor = binding.filledColor;
        spinnerEdad = binding.filledEdad;
        spinnerComportamiento = binding.filledComportamiento;

        spinnerSexo.setAdapter(adapterSexo);
        spinnerTamano.setAdapter(adapterTamano);
        spinnerColor.setAdapter(adapterColor);
        spinnerEdad.setAdapter(adapterEdad);
        spinnerComportamiento.setAdapter(adapterComportamiento);

        spinnerSexo.setOnItemClickListener((adapterView, view, i, l) ->
                Toast.makeText(getActivity(), spinnerSexo.getText().toString(), Toast.LENGTH_SHORT).show());
        spinnerTamano.setOnItemClickListener((adapterView, view, i, l) ->
                Toast.makeText(getActivity(), spinnerTamano.getText().toString(), Toast.LENGTH_SHORT).show());
        spinnerColor.setOnItemClickListener((adapterView, view, i, l) ->
                Toast.makeText(getActivity(), spinnerColor.getText().toString(), Toast.LENGTH_SHORT).show());
        spinnerEdad.setOnItemClickListener((adapterView, view, i, l) ->
                Toast.makeText(getActivity(), spinnerEdad.getText().toString(), Toast.LENGTH_SHORT).show());
        spinnerComportamiento.setOnItemClickListener((adapterView, view, i, l) ->
                Toast.makeText(getActivity(), spinnerComportamiento.getText().toString(), Toast.LENGTH_SHORT).show());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}