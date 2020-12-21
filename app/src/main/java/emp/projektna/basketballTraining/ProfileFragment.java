package emp.projektna.basketballTraining;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import emp.projektna.basketballTraining.EditProfile.EditProfileActivity;

public class ProfileFragment extends Fragment {

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private boolean allowRefresh = false;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ImageView imageView;
    private TextView profileName, followers, following;
    private MaterialCardView signOutView, pastTrainigsView;

    private String _imageUrl;
    private int followersCount, followingCount;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Toolbar toolbar = view.findViewById(R.id.profile_toolbar);

        imageView = view.findViewById(R.id.profile_avatar);

        profileName = view.findViewById(R.id.profile_name);
        followers = view.findViewById(R.id.profile_followers);
        following = view.findViewById(R.id.profile_following);

        signOutView = view.findViewById(R.id.sign_out_view);
        pastTrainigsView = view.findViewById(R.id.trainings_card_view);

        // Napolni ime in sliko
        db.collection("Users").document(firebaseAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    _imageUrl = documentSnapshot.getString("Url");
                    Glide.with(getContext()).load(_imageUrl).into(imageView);
                    profileName.setText(documentSnapshot.getString("Name"));

                }
            }
        });

        // Napolni štefilo followers
        db.collection("Follow").document(firebaseAuth.getUid()).collection("followers").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    if (task.getResult().isEmpty())
                        followersCount = 0;
                    else
                        followersCount = task.getResult().size();

                    followers.setText(String.valueOf(followersCount));
                }
            }
        });

        // Napolni štefilo following

        db.collection("Follow").document(firebaseAuth.getUid()).collection("following").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().getDocuments().size() == 0)
                        followingCount = 0;
                    else
                        followingCount = task.getResult().getDocuments().size();

                    following.setText(String.valueOf(followingCount));
                }
            }
        });




        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile_toolbar_edit:
                        Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_settings:
                        break;
                }
                return true;
            }
        });

        signOutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(getContext(), MainActivity.class);
                getActivity().finish();
                startActivity(intent);
            }
        });

        pastTrainigsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TrainigsActivity.class);
                startActivity(intent);
            }
        });




        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Initialize();
        if(allowRefresh){
            allowRefresh=false;
            db.collection("Users").document(firebaseAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        _imageUrl = documentSnapshot.getString("Url");
                        Glide.with(getContext()).load(_imageUrl).into(imageView);
                        profileName.setText(documentSnapshot.getString("Name"));

                    }
                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!allowRefresh)
            allowRefresh = true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_toolbar, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }


}
