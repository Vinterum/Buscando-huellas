package com.example.buscandohuellas.ui.find;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.buscandohuellas.R;
import com.example.buscandohuellas.databinding.FragmentFindBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class FindFragment extends Fragment implements OnMapReadyCallback {

    String TAG = "Find fragment mapa";

    private FragmentFindBinding binding;

    //map view variables
    private GoogleMap mMap;
    private Marker mMarker = null;
    private LatLng mLatLng;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FindViewModel dashboardViewModel =
                new ViewModelProvider(this).get(FindViewModel.class);

        binding = FragmentFindBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Initialize markers from the database
        getMarkersDB();

        return root;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(10f);
        mMap.setMaxZoomPreference(20f);

        LatLng puebla = new LatLng(19.041135850693273, -98.20591993210466);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(puebla));
    }

    public void addMarkers(LatLng latLng, String title){
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(title);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.addMarker(markerOptions);
    }


    private void getMarkersDB() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collectionGroup("DatosExtravio").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (document != null) {
                        double latitud = document.getDouble("latitud");
                        double longitud = document.getDouble("longitud");
                        String nombre = document.getString("nombre");
                        mLatLng = new LatLng(latitud, longitud);
                        addMarkers(mLatLng, nombre);
                    }
                }
            } else {
                Log.d(TAG, task.getException().getMessage()); //Never ignore potential errors!
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}