package emp.projektna.basketballTraining.CompletedTrainings.ViewCompletedTraining;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import emp.projektna.basketballTraining.R;


public class ViewCompletedTrainingFragment extends Fragment {

    private String trainingID;
    private String trainingName;
    private TextView date;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

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

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            trainingID = bundle.getString("trainingID");
            trainingName = bundle.getString("trainingName");
            toolbar.setTitle(trainingName);

            db.collection("CompletedTrainings").document(trainingID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful() && task.getResult().contains("date")) {
                        date.setText(task.getResult().getString("date"));
                    }
                }
            });


        }

        return view;
    }
}