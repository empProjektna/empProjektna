package emp.projektna.basketballTraining.Trainings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import emp.projektna.basketballTraining.R;


public class TrainingsFragment extends Fragment {
    Toolbar toolbar;
    private RecyclerView recyclerView;
    private AdapterTrainings adapterTrainings;
    private ArrayList<ModelTrainings> modelTrainingsArrayList = new ArrayList<>();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trainings, container, false);

        toolbar = view.findViewById(R.id.past_trainings_toolbar);


        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapterTrainings = new AdapterTrainings(getActivity(), modelTrainingsArrayList);

        recyclerView.setAdapter(adapterTrainings);

        populateRecyclerView();

        return view;
    }

    // TODO: iz baze črpaj podatke in nafilaj RecyclerView
    public void  populateRecyclerView() {
        db.collection("Trainings")
                .whereEqualTo("userID", firebaseAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        modelTrainingsArrayList.clear();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ModelTrainings modelTrainings = new ModelTrainings(document.getString("Name"),((ArrayList) document.get("exercises")).size(), document.getId());
                                modelTrainingsArrayList.add(modelTrainings);
                                adapterTrainings.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }
}