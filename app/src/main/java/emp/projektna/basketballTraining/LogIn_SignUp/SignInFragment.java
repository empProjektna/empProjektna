package emp.projektna.basketballTraining.LogIn_SignUp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import emp.projektna.basketballTraining.R;
import emp.projektna.basketballTraining.SecondMainActivity;


public class SignInFragment extends Fragment {

    private com.shobhitpuri.custombuttons.GoogleSignInButton googleSignInButton;


    private GoogleSignInClient mGoogleSignInClient;

    private String TAG = "MainActivity";
    private FirebaseAuth mAuth;


    private Button btnSignIn;

    private EditText tf_email;
    private EditText tf_password;

    private LinearLayout email_layout, password_layout;

    private LinearLayout layout_signUp;

    private int RC_SIGN_IN = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(getContext(), SecondMainActivity.class);
            getActivity().finish();
            startActivity(intent);
        }
        super.onCreate(savedInstanceState);
        googleSignInButton = view.findViewById(R.id.google_sign_in_button);

        //Buttons connections with layout
        btnSignIn = view.findViewById(R.id.btn_sign_in);


        //EditText connections with layout
        tf_email = view.findViewById(R.id.tf_email);
        tf_password = view.findViewById(R.id.tf_password);

        //EditView connections with layout
        layout_signUp = view.findViewById(R.id.layout_sign_up);

        email_layout = view.findViewById(R.id.email_layout);

        password_layout = view.findViewById(R.id.password_layout);
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        mGoogleSignInClient = GoogleSignIn.getClient(getContext(),googleSignInOptions);

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
                    Toast.makeText(getContext(), "Empty field!", Toast.LENGTH_SHORT).show();
                else {
                    signIn();
                }
            }
        });

        tf_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                email_layout.setSelected(hasFocus);
            }
        });

        tf_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                password_layout.setSelected(hasFocus);
            }
        });



        layout_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment someFragment = new SignUpFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, someFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });
        return view;
    }

    private void signIn() {
        mAuth.signInWithEmailAndPassword(tf_email.getText().toString(), tf_password.getText().toString()).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user,false);
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(getContext(), "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    updateUI(null,false);
                }
            }
        });
    }

    private  void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
            Toast.makeText(getActivity(),"Signed in with Google", Toast.LENGTH_LONG).show();
            FireBaseGoogleAuth(googleSignInAccount);
        }
        catch (ApiException e){
            Toast.makeText(getActivity(),"ERROR!", Toast.LENGTH_LONG).show();
            FireBaseGoogleAuth(null);
        }
    }

    private void FireBaseGoogleAuth(GoogleSignInAccount account) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //Toast.makeText(MainActivity.this,"Successful", Toast.LENGTH_LONG).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user, true);
                }
                else {
                    Toast.makeText(getActivity(),"Not successful", Toast.LENGTH_LONG).show();
                    updateUI(null, true);
                }
            }
        });
    }

    private  void updateUI(FirebaseUser firebaseUser, boolean googleSignIn) {
        if(googleSignIn) {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
            if(account != null)
            {
                String name = account.getDisplayName();

                Toast.makeText(getActivity(), "Welcome "+ name, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), SecondMainActivity.class);
                Bundle bundle = new Bundle();

                // Check if first google login and set name in FireStore
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Users").
                        document(FirebaseAuth.getInstance().getUid())
                        .get().
                        addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                             @Override
                             public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                 if (task.isSuccessful()) {
                                     DocumentSnapshot documentSnapshot = task.getResult();
                                     if (!documentSnapshot.exists()) {
                                         Map<String, Object> dbInput = new HashMap<>();
                                         dbInput.put("Name", name);
                                         dbInput.put("Url", account.getPhotoUrl().toString());
                                         dbInput.put("BirthDate", "");
                                         dbInput.put("Gender", "");
                                         dbInput.put("Height", "");
                                         dbInput.put("Weight", "");
                                         db.collection("Users").document(FirebaseAuth.getInstance().getUid()).set(dbInput);
                                         bundle.putBoolean("first", true);
                                     }
                                     else
                                         bundle.putBoolean("first", false);
                                 }

                                 intent.putExtras(bundle);
                                 getActivity().finish();
                                 startActivity(intent);
                             }
                         }
                );

            }
            else{
                Toast.makeText(getActivity(),"Login failed", Toast.LENGTH_LONG).show();
            }
        }
        else if(firebaseUser != null){
            Toast.makeText(getActivity(), "Welcome", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getActivity(), SecondMainActivity.class);
            getActivity().finish();
            startActivity(intent);
        }
        else{
            Toast.makeText(getActivity(), "Login failed", Toast.LENGTH_LONG).show();
        }

    }


}
