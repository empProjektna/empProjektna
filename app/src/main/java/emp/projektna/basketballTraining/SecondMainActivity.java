package emp.projektna.basketballTraining;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import emp.projektna.basketballTraining.AddTraining.AddFragment;
import emp.projektna.basketballTraining.EditProfile.EditProfileActivity;

public class SecondMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        Intent intent;
        Bundle  b = getIntent().getExtras();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FeedFragment()).commit();
        if (b != null && b.getBoolean("first")) {
            Log.e("blabla", "bla");
            intent = new Intent(SecondMainActivity.this, EditProfileActivity.class);
            startActivity(intent);
        }
    }




    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.nav_add:
                    selectedFragment = new AddFragment();
                    break;
                case R.id.nav_home:
                    selectedFragment = new FeedFragment();
                    break;
                case R.id.nav_profile:
                    selectedFragment = new ProfileFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

            return true;
        }
    };
}