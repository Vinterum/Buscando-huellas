package com.example.buscandohuellas.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buscandohuellas.Adapters.PostAdapter;
import com.example.buscandohuellas.Adapters.RegisterPetAdapter;
import com.example.buscandohuellas.Dog;
import com.example.buscandohuellas.Place;
import com.example.buscandohuellas.databinding.FragmentHomeBinding;
import com.example.buscandohuellas.databinding.FragmentMyPetsBinding;
import com.example.buscandohuellas.ui.mypets.MyPetsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    RecyclerView registerRecyclerView;
    ArrayList<Dog> dogArrayList;
    //ArrayList<Place> placeArrayList;
    PostAdapter postAdapter;
    FirebaseFirestore db;
    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        registerRecyclerView = binding.homeRV;
        registerRecyclerView.setHasFixedSize(true);
        registerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        db = FirebaseFirestore.getInstance();
        dogArrayList = new ArrayList<Dog>();
        //placeArrayList = new ArrayList<Place>();
        postAdapter = new PostAdapter(HomeFragment.this.getActivity(), dogArrayList);
        registerRecyclerView.setAdapter(postAdapter);

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
                    postAdapter.notifyDataSetChanged();
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