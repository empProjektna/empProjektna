package emp.projektna.basketballTraining.AddTraining;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import emp.projektna.basketballTraining.AdapterExercise;
import emp.projektna.basketballTraining.R;

public class AddFragment extends Fragment {


    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private RecyclerView recyclerView;
    private AdapterExercise adapterExercise;
    private ArrayList<ModelExercise> modelExerciseArrayList = new ArrayList<>();
    boolean first = true;
    String ID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ID = String.valueOf(System.currentTimeMillis());
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add, container, false);

        Button addExercise = (Button) view.findViewById(R.id.btn_exercise_add);

        EditText trainingName = (EditText) view.findViewById(R.id.edit_training_name);
        Toolbar toolbar = view.findViewById(R.id.add_training_toolbar);

        addExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                if (first) {
                    db.collection("Trainings").document(ID).set(new HashMap<String, Object>() {{
                        put("Name", "");
                    }});
                    first = false;
                }
                 */
                Fragment someFragment = new AddExerciseFragment();
                Bundle args = new Bundle();
                args.putString("id", ID);
                someFragment.setArguments(args);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, someFragment); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.save_training:
                        if (trainingName.getText().length() == 0)
                            Toast.makeText(getContext(), "Enter training name!", Toast.LENGTH_SHORT).show();
                        else if (modelExerciseArrayList.isEmpty())
                            Toast.makeText(getContext(), "Enter at least one exercise", Toast.LENGTH_SHORT).show();
                        else {
                            Map<String, Object> query = new HashMap<>();
                            query.put("Name",trainingName.getText().toString());
                            query.put("userID", firebaseAuth.getUid());
                            db.collection("Trainings").document(ID).update(query).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getContext(), "Saved!", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            //db.collection("Users").document(firebaseAuth.getUid()).collection("Trainigs").add(new HashMap<String, Object>() {{put("ID", ID);}});
                        }
                        break;
                    case R.id.discard_training:
                            db.collection("Trainings").document(firebaseAuth.getUid()).collection(ID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (DocumentSnapshot document : Objects.requireNonNull(task.getResult()))
                                            document.getReference().delete();
                                        ID = String.valueOf(System.currentTimeMillis());
                                        modelExerciseArrayList.clear();
                                        adapterExercise.notifyDataSetChanged();
                                        trainingName.setText("");
                                    }
                                }
                            });
                        break;
                }
                return true;

            }
        });


        recyclerView = (RecyclerView) view.findViewById(R.id.exercises_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapterExercise = new AdapterExercise(getActivity(), modelExerciseArrayList);

        recyclerView.setAdapter(adapterExercise);

        populateRecyclerView(recyclerView, adapterExercise, ID);

        return view;
    }

    public void  populateRecyclerView(RecyclerView recyclerView, AdapterExercise adapterExercise, String id) {
        db.collection("Trainings").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Object exerciseIds = task.getResult().get("exercises");
                    modelExerciseArrayList.clear();
                    if (exerciseIds != null) {
                        for (String exerciseId : (ArrayList<String>) exerciseIds) {
                            db.collection("Exercises").document(exerciseId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();

                                        String name = document.getString("NAME");
                                        Long length = (Long) document.get("LENGTH");
                                        String description = document.getString("DESCRIPTION");
                                        Long sets = (Long) document.get("SETS");
                                        Boolean timer = document.getBoolean("TIMER");
                                        Boolean isShooting = document.getBoolean("isShooting");
                                        ArrayList<Integer> positions = (ArrayList<Integer>) document.get("POSITIONS");
                                        ModelExercise modelExercise = new ModelExercise(name,length, description, sets,timer, isShooting, positions);

                                        modelExerciseArrayList.add(modelExercise);
                                        adapterExercise.notifyDataSetChanged();
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
    public void onResume() {
        super.onResume();
    }
}
