package emp.projektna.basketballTraining.CompletedTrainings;

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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import emp.projektna.basketballTraining.R;


public class CompletedTrainingsFragment extends Fragment {


    private RecyclerView recyclerView;
    private AdapterCompletedTrainings adapterTrainings;
    private ArrayList<ModelCompletedTrainings> modelTrainingsArrayList = new ArrayList<>();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_completed_trainings, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapterTrainings = new AdapterCompletedTrainings(getActivity(), modelTrainingsArrayList);

        recyclerView.setAdapter(adapterTrainings);

        populateRecyclerView();

        return view;
    }


    public void  populateRecyclerView() {
        db.collection("CompletedTrainings")
                .whereEqualTo("userID", firebaseAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        modelTrainingsArrayList.clear();
                        if (task.isSuccessful()) {
                            modelTrainingsArrayList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                db.collection("Trainings").document(document.getString("trainingID"))
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task2) {

                                                if (task.isSuccessful()) {
                                                    Log.e("bla", String.valueOf(task2.getResult().exists()));
                                                        ModelCompletedTrainings modelTrainings = new ModelCompletedTrainings(task2.getResult().getString("Name"),((ArrayList) task2.getResult().get("exercises")).size(), document.getId(), document.getString("date"));
                                                        modelTrainingsArrayList.add(modelTrainings);
                                                        adapterTrainings.notifyDataSetChanged();
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
    }
}