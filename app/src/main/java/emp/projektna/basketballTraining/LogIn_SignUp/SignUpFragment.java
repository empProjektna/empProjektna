package emp.projektna.basketballTraining.LogIn_SignUp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import emp.projektna.basketballTraining.R;
import emp.projektna.basketballTraining.SecondMainActivity;


public class SignUpFragment extends Fragment {
    private FirebaseAuth mAuth;

    private EditText tf_email;
    private EditText tf_password;
    private EditText tf_password2;
    private LinearLayout email_layout, password_layout, password_layout2, layout_signIn;
    private Button btnSignUp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        mAuth = FirebaseAuth.getInstance();
        tf_email = view.findViewById(R.id.tf_email_sign_up);
        tf_password = view.findViewById(R.id.tf_password_sign_up);
        tf_password2 = view.findViewById(R.id.tf_password_sign_up2);


        email_layout = view.findViewById(R.id.email_layout);
        password_layout = view.findViewById(R.id.password_layout);
        password_layout2 = view.findViewById(R.id.password_layout2);



        layout_signIn = view.findViewById(R.id.layout_sign_in);

        Toolbar toolbar = view.findViewById(R.id.sign_up_toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
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

        tf_password2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                password_layout2.setSelected(hasFocus);
            }
        });

        btnSignUp = view.findViewById(R.id.btn_sign_up);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!tf_password.getText().toString().equals(tf_password2.getText().toString())){
                    Toast.makeText(getContext(), "Passwords don't match!", Toast.LENGTH_SHORT).show();
                }
                else if (tf_password.getText().toString().length() < 8)
                    Toast.makeText(getContext(), "Min password length is 8!", Toast.LENGTH_SHORT).show();
                else {
                    Log.e("SignUp", tf_password.getText().toString());
                    //SIGN UP
                    mAuth.createUserWithEmailAndPassword(tf_email.getText().toString(), tf_password.getText().toString())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
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
                                        Intent intent = new Intent(getContext(), SecondMainActivity.class);
                                        getActivity().finish();
                                        startActivity(intent);

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });
                }
            }
        });

        layout_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        return view;
    }
}