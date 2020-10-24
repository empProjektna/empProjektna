package emp.projektna.basketballTraining;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    private SignInButton googleSignInButton;


    private GoogleSignInClient mGoogleSignInClient;

    private String TAG = "MainActivity";
    private FirebaseAuth mAuth;

    private Button btnSignOut;
    private Button btnSignIn;

    private EditText tf_email;
    private EditText tf_password;

    private TextView tv_signUp;

    private int RC_SIGN_IN = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        googleSignInButton = findViewById(R.id.google_sign_in_button);

        //Buttons connections with layout
        btnSignIn = findViewById(R.id.btn_sign_in);
        btnSignOut = findViewById(R.id.btn_sign_out);

        //EditText connections with layout
        tf_email = findViewById(R.id.tf_email);
        tf_password = findViewById(R.id.tf_password);

        //EditView connections with layout
        tv_signUp = findViewById(R.id.tv_sign_up);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tf_email.getText().toString().matches("") || tf_password.getText().toString().matches(""))
                    Toast.makeText(MainActivity.this, "Empty field!", Toast.LENGTH_SHORT).show();
                else {
                    signIn();
                }
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(MainActivity.this,"Signed out", Toast.LENGTH_LONG).show();
                btnSignOut.setVisibility(View.INVISIBLE);
            }
        });

        tv_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);

            }
        });

    }

    private void signIn() {
        mAuth.signInWithEmailAndPassword(tf_email.getText().toString(), tf_password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mAuth.getCurrentUser();
                    checkSignInEmail(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(MainActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    checkSignInEmail(null);
                }
            }
        });
    }

    private  void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
            Toast.makeText(MainActivity.this,"Signed in with Google", Toast.LENGTH_LONG).show();
            FireBaseGoogleAuth(googleSignInAccount);
        }
        catch (ApiException e){
            Toast.makeText(MainActivity.this,"ERROR!", Toast.LENGTH_LONG).show();
            FireBaseGoogleAuth(null);
        }
    }

    private void FireBaseGoogleAuth(GoogleSignInAccount account) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this,"Successful", Toast.LENGTH_LONG).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    checkSignInGoogle(user);
                }
                else {
                    Toast.makeText(MainActivity.this,"Not successfull", Toast.LENGTH_LONG).show();
                    checkSignInGoogle(null);
                }
            }
        });
    }

    private void checkSignInEmail(FirebaseUser firebaseUser) {
        if(firebaseUser != null) {
            updateUI();
        }
        else{
            Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_LONG).show();
        }
    }

    private void checkSignInGoogle(FirebaseUser firebaseUser){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(account != null)
        {
            updateUI();
        }
        else {
            Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_LONG).show();
        }
    }
    private void updateUI() {
        Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_LONG).show();
        btnSignOut.setVisibility((View.VISIBLE));

    }
}