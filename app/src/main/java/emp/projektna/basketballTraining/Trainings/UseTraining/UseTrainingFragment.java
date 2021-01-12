package emp.projektna.basketballTraining.Trainings.UseTraining;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import emp.projektna.basketballTraining.AddTraining.ModelExercise;
import emp.projektna.basketballTraining.R;
public class UseTrainingFragment extends Fragment {
    private RecyclerView recyclerView;
    private String trainingID, trainingName;
    private AdapterUseExercise adapterUseExercise;
    private TextView dateTV;
    private ArrayList<ModelExercise> modelExerciseArrayList = new ArrayList<>();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private Spinner privacySpinner;
    Map<String, Map<String, String>> inputedDataShots = new HashMap<>();
    Map<String, Map<String, String>> inputedDataScored = new HashMap<>();
    String dateText = "";
    private static final int TARGET_FRAGMENT_REQUEST_CODE = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_use_training, container, false);

        Toolbar toolbar = view.findViewById(R.id.use_training_toolbar);
        privacySpinner = (Spinner) view.findViewById(R.id.use_training_spinner);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            trainingID = bundle.getString("trainingID");
            trainingName = bundle.getString("trainingName");
            toolbar.setTitle(trainingName);
        }
        dateTV = view.findViewById(R.id.use_training_date_picker);

        dateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar date;
                    final Calendar currentDate = Calendar.getInstance();
                    date = Calendar.getInstance();
                    new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            date.set(year, monthOfYear, dayOfMonth);
                            dateText = dayOfMonth + ". " + (monthOfYear + 1) +  ". " + year + ", ";
                            new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    date.set(Calendar.MINUTE, minute);
                                    dateText += (hourOfDay < 10 ? "0" : "") + hourOfDay + ":" + (minute < 10 ? "0" : "") + minute;
                                    dateTV.setText(dateText);
                                }
                            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
                        }
                    }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();


            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.use_training_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapterUseExercise = new AdapterUseExercise(getActivity(), modelExerciseArrayList, this);

        recyclerView.setAdapter(adapterUseExercise);

        populateRecyclerView(recyclerView, adapterUseExercise, trainingID);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.btn_use_training_save) {
                    if (dateTV.getText().equals(""))
                        Toast.makeText(getContext(), "Select date!", Toast.LENGTH_SHORT).show();
                    else {
                        Map<String, Object> dbInput = new HashMap<>();
                        dbInput.put("date", dateTV.getText().toString());
                        dbInput.put("userID", firebaseAuth.getUid());
                        dbInput.put("shots", inputedDataShots);
                        dbInput.put("scored", inputedDataScored);
                        dbInput.put("public", privacySpinner.getSelectedItem().toString().equals("Everyone"));
                        dbInput.put("trainingID", trainingID);
                        db.collection("CompletedTrainings").document().set(dbInput).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                                    getFragmentManager().popBackStack();
                                }
                                else
                                    Toast.makeText(getContext(), "Error saving!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                return false;
            }

        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.privacy, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        privacySpinner.setAdapter(adapter);


        privacySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView selectedText = (TextView) parent.getChildAt(0);
                if (selectedText != null) {
                    selectedText.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    public void  populateRecyclerView(RecyclerView recyclerView, AdapterUseExercise adapterExercise, String id) {
        db.collection("Trainings").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    Object exerciseIds = task.getResult().get("exercises");
                    modelExerciseArrayList.clear();
                    if (exerciseIds != null) {
                        for (String exerciseId : (ArrayList<String>) exerciseIds) {
                            db.collection("Exercises").document(exerciseId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document != null) {
                                            String name = document.getString("NAME");
                                            Long length = (Long) document.get("LENGTH");
                                            String description = document.getString("DESCRIPTION");
                                            Long sets = (Long) document.get("SETS");
                                            Boolean isShooting = document.getBoolean("isShooting");
                                            ArrayList<Integer> positions = (ArrayList<Integer>) document.get("POSITIONS");
                                            String type = document.getString("TYPE");
                                            ModelExercise modelExercise = new ModelExercise(name, length, description, sets, isShooting, positions, type);

                                            modelExerciseArrayList.add(modelExercise);
                                            adapterExercise.notifyDataSetChanged();
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != Activity.RESULT_OK ) {
            return;
        }
        if(requestCode == TARGET_FRAGMENT_REQUEST_CODE ) {
            inputedDataShots.put(String.valueOf(data.getIntExtra("position", 0)), (HashMap<String, String>) data.getSerializableExtra("shots"));
            inputedDataScored.put(String.valueOf(data.getIntExtra("position", 0)), (HashMap<String, String>) data.getSerializableExtra("scored"));
        }
    }

    public static Intent newIntent(Map<String, String> shots, Map<String, String> scored, int position) {
        Intent intent = new Intent();
        intent.putExtra("shots", (Serializable) shots);
        intent.putExtra("scored", (Serializable) scored);
        intent.putExtra("position", position);
        Log.e("bla", String.valueOf(position));
        return intent;
    }
}