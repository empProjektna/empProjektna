package emp.projektna.basketballTraining;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class NewsFeedActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button btnSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        mAuth = FirebaseAuth.getInstance();
        btnSignOut = findViewById(R.id.btn_sign_out);

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(NewsFeedActivity.this,"Signed out", Toast.LENGTH_LONG).show();
                btnSignOut.setVisibility(View.INVISIBLE);
            }
        });
    }


}