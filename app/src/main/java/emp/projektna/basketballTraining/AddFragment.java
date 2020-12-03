package emp.projektna.basketballTraining;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AddFragment extends Fragment {


    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private RecyclerView recyclerView;
    private AdapterExercise adapterExercise;
    private ArrayList<ModelExercise> modelExerciseArrayList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add, container, false);
        Training training = new Training();

        String ID = String.valueOf(System.currentTimeMillis());
        Button addExercise = (Button) view.findViewById(R.id.btn_exercise_add);

        Button tmp = (Button) view.findViewById(R.id.button);

        addExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment someFragment = new AddExcercise();
                Bundle args = new Bundle();
                args.putString("id", ID);
                someFragment.setArguments(args);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, someFragment); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });

        tmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


        recyclerView = (RecyclerView) view.findViewById(R.id.exercises_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapterExercise = new AdapterExercise(getActivity(), modelExerciseArrayList);

        recyclerView.setAdapter(adapterExercise);

        populateRecyclerView();

        return view;
    }

    public void  populateRecyclerView() {
        ModelExercise modelExercise= new ModelExercise("test1", 10,1, "blabla", 15, true);
        modelExerciseArrayList.add(modelExercise);
        ModelExercise modelExercise2 = new ModelExercise("test2", 10,1, "blabla", 15, true);
        modelExerciseArrayList.add(modelExercise2);
        adapterExercise.notifyDataSetChanged();
    }
}
