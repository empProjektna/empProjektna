package emp.projektna.basketballTraining.LogIn_SignUp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import emp.projektna.basketballTraining.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SignInFragment()).commit();
    }
}