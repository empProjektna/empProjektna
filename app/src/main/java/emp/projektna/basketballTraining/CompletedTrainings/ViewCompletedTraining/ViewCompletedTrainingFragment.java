package emp.projektna.basketballTraining.CompletedTrainings.ViewCompletedTraining;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import emp.projektna.basketballTraining.R;


public class ViewCompletedTrainingFragment extends Fragment {

    private String trainingID;
    private String trainingName;
    private TextView date;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private RecyclerView recyclerView;
    private AdapterViewExercise adapterExercise;
    private ArrayList<ModelViewExercise> modelExerciseArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_completed_training, container, false);

        Toolbar toolbar = view.findViewById(R.id.view_completed_training_toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        date = view.findViewById(R.id.view_date);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapterExercise = new AdapterViewExercise(getActivity(), modelExerciseArrayList, this);

        recyclerView.setAdapter(adapterExercise);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            trainingID = bundle.getString("trainingID");
            trainingName = bundle.getString("trainingName");
            toolbar.setTitle(trainingName);

            db.collection("CompletedTrainings").document(trainingID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot snapshot = task.getResult();
                    if(task.isSuccessful() && snapshot.contains("date")) {
                        date.setText(snapshot.getString("date"));
                        populateRecyclerView(recyclerView, adapterExercise, snapshot.getString("trainingID"));
                    }
                }
            });


        }

        return view;
    }

    public void  populateRecyclerView(RecyclerView recyclerView, AdapterViewExercise adapterExercise, String id) {
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
                                            //Boolean timer = document.getBoolean("TIMER");
                                            Boolean isShooting = document.getBoolean("isShooting");
                                            ArrayList<Integer> positions = (ArrayList<Integer>) document.get("POSITIONS");
                                            String type = document.getString("TYPE");
                                            ModelViewExercise modelExercise = new ModelViewExercise(name, length, description, sets, isShooting, positions, type, trainingID);

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
}