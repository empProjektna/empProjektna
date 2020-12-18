package emp.projektna.basketballTraining;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class EditProfileFragment extends Fragment {

    private DatePickerDialog datePickerDialog;

    private static final int PICK_IMAGE = 100;

    private Uri imageUri;
    private ImageView imageView;

    private Toolbar toolbar;

    private EditText profileName, weight, height;

    private TextView birthDate;
    private Spinner genderSpinner;

    String _gender, _name, _weight, _height, _birthDate, _imageUrl;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    public EditProfileFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        toolbar = view.findViewById(R.id.edit_profile_toolbar);


        TextView birthDate = (TextView) view.findViewById(R.id.edit_profile_date_picker);


        EditText profileName = (EditText) view.findViewById(R.id.profile_name);
        EditText weight = (EditText) view.findViewById(R.id.edit_profile_weight_et);
        EditText height = (EditText) view.findViewById(R.id.edit_profile_height_et);




        genderSpinner = (Spinner) view.findViewById(R.id.edit_profile_spinner);
        imageView = (ImageView)view.findViewById(R.id.edit_profile_avatar);

        db.collection("Users").document(firebaseAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    profileName.setText(documentSnapshot.getString("Name"));
                    weight.setText(documentSnapshot.getString("Weight"));
                    height.setText(documentSnapshot.getString("Height"));
                    birthDate.setText(documentSnapshot.getString("BirthDate"));
                    _gender = documentSnapshot.getString("Gender");
                    genderSpinner.setSelection(_gender.equals("Male") ? 0 : 1);
                    _imageUrl = documentSnapshot.getString("Url");
                    Glide.with(getContext()).load(_imageUrl).into(imageView);
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day, month, year;
                if (!birthDate.getText().toString().equals("")) {
                    String[] datum = birthDate.getText().toString().split("/");
                    day = Integer.parseInt(datum[0]);
                    month = Integer.parseInt(datum[1]) - 1;
                    year = Integer.parseInt(datum[2]);
                }
                else {
                    day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                    month =  Calendar.getInstance().get(Calendar.MONTH);
                    year = Calendar.getInstance().get(Calendar.YEAR);;
                }

                datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        birthDate.setText(dayOfMonth  + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });


        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit_profile_toolbar_save:
                        // slika
                        if (imageUri != null) {
                            final ProgressDialog progressDialog = new ProgressDialog(getContext());
                            progressDialog.setTitle("Uploading...");
                            progressDialog.show();

                            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
                            ref.putFile(imageUri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            progressDialog.dismiss();
                                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    Map<String, Object> databaseInput = mapForUpload(profileName.getText().toString(), height.getText().toString(), weight.getText().toString(), birthDate.getText().toString(), genderSpinner.getSelectedItem().toString());
                                                    databaseInput.put("Url", uri.toString());

                                                    db.collection("Users").document(firebaseAuth.getUid()).set(databaseInput);
                                                }
                                            });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                        }
                                    })
                                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                                    .getTotalByteCount());
                                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                                        }
                                    });
                        }
                        else {
                            Map<String, Object> databaseInput = mapForUpload(profileName.getText().toString(), height.getText().toString(), weight.getText().toString(), birthDate.getText().toString(), genderSpinner.getSelectedItem().toString());
                            databaseInput.put("Url", _imageUrl);
                            db.collection("Users").document(firebaseAuth.getUid()).set(databaseInput);
                        }
                }
                return true;
            }
        });






        return view;
    }

    Map<String, Object> mapForUpload (String name, String height, String weight, String birthDate, String gender) {
        Map<String, Object> databaseInput = new HashMap<>();
        databaseInput.put("Name",name);
        databaseInput.put("Height", height);
        databaseInput.put("Weight", weight);
        databaseInput.put("BirthDate", birthDate);
        databaseInput.put("Gender", gender);

        return databaseInput;
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

}