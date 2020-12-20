package emp.projektna.basketballTraining.SearchUser;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import emp.projektna.basketballTraining.R;

public class SearchUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        getSupportFragmentManager().beginTransaction().replace(R.id.search_fragment_frame, new SearchUserFragment()).commit();
    }
}