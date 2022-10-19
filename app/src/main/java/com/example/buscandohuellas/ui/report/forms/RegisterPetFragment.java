package com.example.buscandohuellas.ui.report.forms;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.buscandohuellas.R;
import com.example.buscandohuellas.databinding.FragmentRegisterPetBinding;
import com.example.buscandohuellas.ui.report.ReportViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class RegisterPetFragment extends Fragment {

    //Datos
    String nombre;
    String raza;
    String sexo;
    String tamano;
    String color;
    String edad;
    String detallesAp;
    String detallesSalud;
    String comportamiento;
    String contacto;
    String imageUrl;

    //Upload image variables
    private ImageView dogImage;
    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    boolean imgBool = false;
    boolean dbImgBool = true;

    //Spinners variables
    TextView textView;
    Dialog dialog;
    AutoCompleteTextView spinnerSexo;
    AutoCompleteTextView spinnerTamano;
    AutoCompleteTextView spinnerColor;
    AutoCompleteTextView spinnerEdad;
    AutoCompleteTextView spinnerComportamiento;
    boolean sexoBool = false;
    boolean tamanoBool = false;
    boolean colorBool = false;
    boolean edadBool = false;
    boolean comportamientoBool = false;

    private FragmentRegisterPetBinding binding;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "Register Database";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ReportViewModel notificationsViewModel =
                new ViewModelProvider(this).get(ReportViewModel.class);

        binding = FragmentRegisterPetBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initializeSpinners();

        //Image
        dogImage = binding.dogImage;
        storage = FirebaseStorage.getInstance();

        // After clicking on text we will have
        // to choose whether to
        // select image from camera and gallery
        dogImage.setOnClickListener(view -> {
            choosePicture();
        });

        //Select image button onClickListener
        dogImage.setOnClickListener(view -> {
            RegisterPetFragmentPermissionsDispatcher.choosePictureWithPermissionCheck(RegisterPetFragment.this);
        });

        //Botón registro
        binding.RegistraMascotaBoton.setOnClickListener(view -> {
            if (binding.nombreMascota.getText().toString().length() > 0
                    && binding.ssRaza.getText().toString().length() > 0
                    && binding.detallesApariencia.getText().toString().length() > 0
                    && binding.detallesSalud.getText().toString().length() > 0
                    && sexoBool && tamanoBool && colorBool && edadBool && comportamientoBool && imgBool && dbImgBool) {
                addDogToDB();
                NavDirections navDirections = RegisterPetFragmentDirections.registerToLocationForm();
                Navigation.findNavController(view).navigate(navDirections);
            } else {
                Toast.makeText(getActivity(), "Llena todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            }
        });

        //initialize searchable spinner for raza
        textView = binding.ssRaza;
        textView.setOnClickListener(view -> {
            //initialize dialog
            dialog = new Dialog(getActivity());
            //set custom dialog
            dialog.setContentView(R.layout.ss_dialog);
            //set custom width and height
            dialog.getWindow().setLayout(800, 950);
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

        //check if option was selected on spinners
        spinnerSexo.setOnItemClickListener((p, v, pos, id) -> {
            sexo = (String) p.getItemAtPosition(pos);
            sexoBool = true;
        });
        spinnerTamano.setOnItemClickListener((p, v, pos, id) -> {
            tamano = (String) p.getItemAtPosition(pos);
            tamanoBool = true;
        });
        spinnerColor.setOnItemClickListener((p, v, pos, id) -> {
            color = (String) p.getItemAtPosition(pos);
            colorBool = true;
        });
        spinnerEdad.setOnItemClickListener((p, v, pos, id) -> {
            edad = (String) p.getItemAtPosition(pos);
            edadBool = true;
        });
        spinnerComportamiento.setOnItemClickListener((p, v, pos, id) -> {
            comportamiento = (String) p.getItemAtPosition(pos);
            comportamientoBool = true;
        });

        return root;
    }

    private void initializeSpinners() {
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
    }

    //Upload picture
    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            (ActivityResultCallback<ActivityResult>) result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        imageUri = data.getData();
                        imageUri.getLastPathSegment();
                        uploadPicture();
                        if (dbImgBool) {
                            dogImage.setImageURI(imageUri);
                        }
                    }
                }
            }
    );


    //Permission dispatcher
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        dbImgBool = true;
        startActivityIntent.launch(intent);
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showRationaleForGallery(final PermissionRequest request) {
        new AlertDialog.Builder(getActivity())
                .setMessage(R.string.mensaje_permitir)
                .setPositiveButton(R.string.permitir, (dialog, button) -> request.proceed())
                .setNegativeButton(R.string.cancelar, (dialog, button) -> request.cancel())
                .show();
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void onCameraDenied() {
        Toast.makeText(getActivity(), R.string.permiso_denegado, Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    void onCameraNeverAskAgain() {
        Toast.makeText(getActivity(), R.string.no_preguntar, Toast.LENGTH_SHORT).show();
    }


    private void uploadPicture() {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setTitle("Subiendo Imágen...");
        pd.show();
        storageReference = storage.getReference()
                .child("images")
                .child(System.currentTimeMillis() + "");
        storageReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    pd.dismiss();
                    imgBool = true;
                    Toast.makeText(RegisterPetFragment.this.getActivity().getApplicationContext(), "Imagen subida", Toast.LENGTH_LONG).show();
                    String imageUrl = taskSnapshot.getStorage().getDownloadUrl().toString();
                })
                .addOnFailureListener(e -> {
                    dbImgBool = false;
                    pd.dismiss();
                    Toast.makeText(RegisterPetFragment.this.getActivity().getApplicationContext(), "Resolución muy alta, use otra imágen", Toast.LENGTH_LONG).show();
                })
                .addOnProgressListener(snapshot -> {
                    double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    pd.setMessage("Percentage: " + (int) progressPercent + "%");
                });
    }

    private void addDogToDB() {
        nombre = binding.nombreMascota.getText().toString();
        raza = binding.ssRaza.getText().toString();
        detallesAp = binding.detallesApariencia.getText().toString();
        detallesSalud = binding.detallesSalud.getText().toString();
        contacto = binding.contacto.getText().toString();
        Map<String, Object> dog = new HashMap<>();
        dog.put("nombre", nombre);
        dog.put("raza", raza);
        dog.put("sexo", sexo);
        dog.put("tamano", tamano);
        dog.put("color", color);
        dog.put("edad", edad);
        dog.put("detalles_ap", detallesAp);
        dog.put("detalles_salud", detallesSalud);
        dog.put("comportamiento", comportamiento);
        dog.put("contacto", contacto);
        dog.put("imageUrl", imageUrl);
        dog.put("timestamp", Timestamp.now());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();

            db.collection("Usuarios").document(uid).collection("MisPerros").document(nombre).set(dog)
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