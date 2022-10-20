package com.example.buscandohuellas.ui.mypets;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.buscandohuellas.Adapters.RegisterPetAdapter;
import com.example.buscandohuellas.Dog;
import com.example.buscandohuellas.R;
import com.example.buscandohuellas.databinding.FragmentMyPetsBinding;
import com.example.buscandohuellas.databinding.FragmentRegisterPetBinding;
import com.example.buscandohuellas.ui.report.ReportFragmentDirections;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MyPetsFragment extends Fragment {

    private FragmentMyPetsBinding binding;
    RecyclerView registerRecyclerView;
    ArrayList<Dog> dogArrayList;
    RegisterPetAdapter registerPetAdapter;
    FirebaseFirestore db;
    ImageView add;

    public static MyPetsFragment newInstance() {
        return new MyPetsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMyPetsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        registerRecyclerView = binding.RegisterRV;
        registerRecyclerView.setHasFixedSize(true);
        registerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        db = FirebaseFirestore.getInstance();
        dogArrayList = new ArrayList<Dog>();
        registerPetAdapter = new RegisterPetAdapter(MyPetsFragment.this.getActivity(), dogArrayList);
        registerRecyclerView.setAdapter(registerPetAdapter);
        add = binding.add;

        //add dog button
        add.setOnClickListener(view -> {
            NavDirections navDirections = MyPetsFragmentDirections.mypetsToRegisterPetFragment();
            Navigation.findNavController(view).navigate(navDirections);
        });
        
        EventChangeListener();

        return root;
    }

    private void EventChangeListener() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            db.collection("Usuarios").document(uid).collection("MisPerros").addSnapshotListener((value, error) -> {
                if (error != null){
                    Log.e("Firestore error", error.getMessage());
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()){
                    if (dc.getType() == DocumentChange.Type.ADDED){
                        dogArrayList.add(dc.getDocument().toObject(Dog.class));
                    }
                    registerPetAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}