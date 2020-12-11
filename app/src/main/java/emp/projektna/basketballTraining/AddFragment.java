package emp.projektna.basketballTraining;

import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.LongFunction;

public class AddFragment extends Fragment {


    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private RecyclerView recyclerView;
    private AdapterExercise adapterExercise;
    private ArrayList<ModelExercise> modelExerciseArrayList = new ArrayList<>();
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
        Training training = new Training();

        Button addExercise = (Button) view.findViewById(R.id.btn_exercise_add);

        EditText trainingName = (EditText) view.findViewById(R.id.edit_training_name);
        Toolbar toolbar = view.findViewById(R.id.add_training_toolbar);

        addExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment someFragment = new AddExercise();
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
                        else {
                            Map<String, Object> query = new HashMap<>();
                            query.put("TRAINING_NAME",trainingName.getText().toString());
                            db.collection("Trainings").document(firebaseAuth.getUid()).collection(ID).document("Name").set(query).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        db.collection("Trainings").document(firebaseAuth.getUid()).collection(id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    modelExerciseArrayList.clear();
                    for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        if (!document.contains("TRAINING_NAME")) {
                            ModelExercise modelExercise = new ModelExercise(document.getString("NAME"), Objects.requireNonNull(document.getLong("LENGTH")).intValue(),
                                    Objects.requireNonNull(document.getLong("POSITION")).intValue(), document.getString("DESCRIPTION"), Objects.requireNonNull(document.getLong("REPEATS")).intValue(), document.getBoolean("TIMER"));
                            modelExerciseArrayList.add(modelExercise);
                        }
                    }
                    adapterExercise.notifyDataSetChanged();
                }
            }
        });



    }
}
