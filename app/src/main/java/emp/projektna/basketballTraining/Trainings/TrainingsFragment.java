package emp.projektna.basketballTraining.Trainings;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import emp.projektna.basketballTraining.AdapterFeed;
import emp.projektna.basketballTraining.ModelFeed;
import emp.projektna.basketballTraining.R;


public class TrainingsFragment extends Fragment {

    private RecyclerView recyclerView;
    private AdapterFeed adapterFeed;
    private ArrayList<ModelFeed> modelFeedArrayList = new ArrayList<>();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trainings, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapterFeed = new AdapterFeed(getActivity(), modelFeedArrayList);

        recyclerView.setAdapter(adapterFeed);

        populateRecyclerView();

        return view;
    }

    // TODO: iz baze črpaj podatke in nafilaj RecyclerView
    public void  populateRecyclerView() {

        db.collection("Users").document(firebaseAuth.getUid()).collection("Trainings").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    List<DocumentSnapshot> snapshots = task.getResult().getDocuments();

                    for (DocumentSnapshot element : snapshots) {
                            String training = element.getId();
                                Log.e("blabla", training);

                        db.collection("Trainings").document(training).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                Log.e("kaka", String.valueOf(task.getResult().exists()));
                                if (task.isSuccessful()) {
                                    List<String> exercises = (List<String>) task.getResult().get("exercises");
                                    if (exercises != null) {
                                        Log.e("mama", exercises.toString());
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });



        ModelFeed modelFeed = new ModelFeed(1, 5, 100, 100, 60, "50m 16s","Andraž Anderle", "8:00");
        modelFeedArrayList.add(modelFeed);
        modelFeed = new ModelFeed(2, 10, 42, 100, 50, "60m 42s", "Aleksandar Georgiev", "8:00");
        modelFeedArrayList.add(modelFeed);
        modelFeed = new ModelFeed(2, 10, 42, 100, 50, "60m 42s", "Aleksandar Georgiev", "8:00");
        modelFeedArrayList.add(modelFeed);
        adapterFeed.notifyDataSetChanged();
    }
}