package emp.projektna.basketballTraining.Trainings;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import emp.projektna.basketballTraining.R;

public class TrainigsActivity extends AppCompatActivity {
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainigs);
        toolbar = findViewById(R.id.past_trainings_toolbar);


        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        getSupportFragmentManager().beginTransaction().replace(R.id.trainings_container, new TrainingsFragment()).commit();
    }
}