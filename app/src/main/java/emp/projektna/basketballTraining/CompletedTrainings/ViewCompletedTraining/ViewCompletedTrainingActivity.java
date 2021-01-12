package emp.projektna.basketballTraining.CompletedTrainings.ViewCompletedTraining;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import emp.projektna.basketballTraining.R;
import emp.projektna.basketballTraining.Trainings.TrainingsFragment;

public class ViewCompletedTrainingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_completed_training);
        Bundle bundle = getIntent().getExtras();
        if( bundle != null && bundle.getString("trainingID") != null && bundle.getString("trainingName") != null) {
            Fragment fragment = new ViewCompletedTrainingFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.trainings_container, fragment).commit();
        }
    }
}