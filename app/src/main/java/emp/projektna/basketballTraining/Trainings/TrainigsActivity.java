package emp.projektna.basketballTraining.Trainings;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import emp.projektna.basketballTraining.R;

public class TrainigsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainigs);



        getSupportFragmentManager().beginTransaction().replace(R.id.trainings_container, new TrainingsFragment()).commit();
    }

    public void setFragment(Fragment frag){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.trainings_container, frag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}