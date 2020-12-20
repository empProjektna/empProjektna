package emp.projektna.basketballTraining.EditProfile;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import emp.projektna.basketballTraining.R;

public class EditProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportFragmentManager().beginTransaction().replace(R.id.edit_profile_fragment_container, new EditProfileFragment()).commit();
    }
}