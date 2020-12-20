package emp.projektna.basketballTraining;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private EditText tf_email;
    private EditText tf_password;
    private EditText tf_password2;

    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        tf_email = findViewById(R.id.tf_email_sign_up);
        tf_password = findViewById(R.id.tf_password_sign_up);
        tf_password2 = findViewById(R.id.tf_password_sign_up2);

        btnSignUp = findViewById(R.id.btn_sign_up);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!tf_password.getText().toString().equals(tf_password2.getText().toString())){
                    Toast.makeText(SignUpActivity.this, "Passwords don't match!", Toast.LENGTH_SHORT).show();
                }
                else if (tf_password.getText().toString().length() < 8)
                    Toast.makeText(SignUpActivity.this, "Min password length is 8!", Toast.LENGTH_SHORT).show();
                else {
                    Log.e("SignUp", tf_password.getText().toString());
                    //SIGN UP
                    mAuth.createUserWithEmailAndPassword(tf_email.getText().toString(), tf_password.getText().toString())
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Map<String, Object> dbInput = new HashMap<>();
                                        dbInput.put("Name", tf_email.getText().toString().split("@")[0].replace(".", " "));
                                        dbInput.put("Url", "https://firebasestorage.googleapis.com/v0/b/empprojektna-6387a.appspot.com/o/avatar.jpg?alt=media&token=7d1428fa-916c-4836-a0fe-a30009ba6452");
                                        dbInput.put("BirthDate", "");
                                        dbInput.put("Gender", "");
                                        dbInput.put("Height", "");
                                        dbInput.put("Weight", "");
                                        FirebaseFirestore.getInstance().collection("Users").document(mAuth.getUid()).set(dbInput);
                                        Intent intent = new Intent(SignUpActivity.this, SecondMainActivity.class);
                                        finish();
                                        startActivity(intent);

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(SignUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });
                }
            }
        });

    }
}