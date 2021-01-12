package emp.projektna.basketballTraining.SearchUser;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import emp.projektna.basketballTraining.R;
import emp.projektna.basketballTraining.SearchUser.Adapter.UserAdapter;

public class SearchUserFragment extends Fragment {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;

    EditText searchBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_user, container, false);

        toolbar = view.findViewById(R.id.search_user_toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        searchBar = view.findViewById(R.id.search_bar);

        mUsers = new ArrayList<>();
        userAdapter = new UserAdapter(getContext(), mUsers);

        recyclerView.setAdapter(userAdapter);
        readUser();

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUsers(s.toString().toUpperCase());
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchUsers(s.toString());
                userAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }


    private void searchUsers(String s) {
        Query query = FirebaseFirestore.getInstance().collection("Users").orderBy("Name");

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                mUsers.clear();
                for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
                    if (snapshot.getString("Name").toLowerCase().contains(s)) {
                        User user = new User(snapshot.getId(), snapshot.getString("Name"), snapshot.getString("Url"));
                        mUsers.add(user);

                    }
                }
                userAdapter.notifyDataSetChanged();
            }
        });
    }

    private void readUser () {

        Query query = FirebaseFirestore.getInstance().collection("Users");

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                mUsers.clear();
                for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
                    User user = new User(snapshot.getId(), snapshot.getString("Name"), snapshot.getString("Url"));
                    mUsers.add(user);

                    userAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}