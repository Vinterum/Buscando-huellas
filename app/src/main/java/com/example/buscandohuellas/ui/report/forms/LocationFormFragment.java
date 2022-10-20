package com.example.buscandohuellas.ui.report.forms;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.buscandohuellas.R;
import com.example.buscandohuellas.databinding.FragmentLocationFormBinding;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationFormFragment extends Fragment implements OnMapReadyCallback {

    //Datos
    String lugar;
    String detalles_ubi;
    String fecha;
    String nombre;
    double latitud;
    double longitud;
    String id;

    //binding
    private FragmentLocationFormBinding binding;

    //datapicker variables
    private DatePickerDialog datePickerDialog;
    private TextInputEditText editDate;

    //map search autocomplete variables
    private static final String TAG = "LocationForm";

    //map view variables
    private GoogleMap mMap;
    private Marker mMarker = null;
    private LatLng mLatLng;

    public static LocationFormFragment newInstance() {
        LocationFormFragment fragment = new LocationFormFragment();
        return fragment;
    }

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLocationFormBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        
        setupPlaces();

        initDatePicker();
        editDate = binding.editaFecha;
        editDate.setText(getTodaysDate());
        editDate.setOnClickListener(view -> datePickerDialog.show());

        binding.registraUbicacionBoton.setOnClickListener(view -> {
            if (binding.detallesUbicacion.getText().toString().length() > 0
                    && binding.lugarElegido.getText().toString().length() > 0) {
                addPlaceToDB();
                NavDirections navDirections = LocationFormFragmentDirections.locationToFind();
                Navigation.findNavController(view).navigate(navDirections);
            } else {
                Toast.makeText(getActivity(), "Llena todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    private void setupPlaces() {
        // Initialize the SDK
        Places.initialize(getContext(), getString(R.string.gmp_key));

        binding.buscaLugar.setOnClickListener(view -> {
            startAutocomplete();
        });
    }

    //Map search autocomplete helper functions
    private void startAutocomplete() {
        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        List<Place.Field> fields = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG);
        // Define Puebla's rectangular bound
        RectangularBounds bounds = RectangularBounds.newInstance(
                new LatLng(17.8609,-99.0705),
                new LatLng(20.8397,-96.7246));
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .setLocationRestriction(bounds)
                .setCountry("MX")
                .build(getContext());
        mapLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> mapLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            (ActivityResultCallback<ActivityResult>) result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Place place = Autocomplete.getPlaceFromIntent(data);
                        Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                        binding.lugarElegido.setText(place.getName());
                        mLatLng = place.getLatLng();
                        latitud = mLatLng.latitude;
                        longitud = mLatLng.longitude;
                        setMarker(mLatLng);
                    } else if (result.getResultCode() == AutocompleteActivity.RESULT_ERROR) {
                        // TODO: Handle the error.
                        Status status = Autocomplete.getStatusFromIntent(data);
                        Log.i(TAG, status.getStatusMessage());
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        // The user canceled the operation.
                        Log.i(TAG, "User canceled autocomplete");
                    }
                }
            }
    );

    //Map view helper functions
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(15f);
        mMap.setMaxZoomPreference(20f);

        // Add a marker in Puebla and move the camera
        LatLng puebla = new LatLng(19.041135850693273, -98.20591993210466);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(puebla));
    }

    public Marker addMarker(LatLng latLng, String title){
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(title);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        return mMap.addMarker(markerOptions);
    }

    private void setMarker(LatLng latLng){
        if(mMarker != null){
            mMarker.remove();
        }
        mMarker = addMarker(latLng, getString(R.string.markerText));
    }

    //Date picker helper functions
    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String date = makeDateString(day, month, year);
            editDate.setText(date);
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog, dateSetListener, year, month, day);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -10);
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
    }

    private String makeDateString(int day, int month, int year) {
        return day + "/" + month + "/" + year;
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void addPlaceToDB() {
        nombre = LocationFormFragmentArgs.fromBundle(getArguments()).getNombrePerro();
        lugar = binding.lugarElegido.getText().toString();
        detalles_ubi = binding.detallesUbicacion.getText().toString();
        fecha = binding.editaFecha.getText().toString();

        //Get user from database
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        id = nombre + uid;
        Map<String, Object> place = new HashMap<>();
        place.put("idPerro", id);
        place.put("nombre", nombre);
        place.put("lugar", lugar);
        place.put("detalles_ubi", detalles_ubi);
        place.put("fecha", fecha);
        place.put("latitud", latitud);
        place.put("longitud", longitud);
        place.put("timestamp", Timestamp.now());

        if (user != null) {
            db.collection("Usuarios").document(uid).collection("DatosExtravio").add(place)
                    .addOnSuccessListener(unused -> Log.d(TAG, "DocumentSnapshot added"))
                    .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}