package emp.projektna.basketballTraining.SearchUser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import emp.projektna.basketballTraining.R;
import emp.projektna.basketballTraining.SearchUser.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private Context mContext;
    private List<User> mUsers;

    private FirebaseUser firebaseUser;
    public UserAdapter(Context mContext, List<User> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.user_search_row, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final User user = mUsers.get(position);

        holder.btn_follow.setVisibility(View.VISIBLE);

        holder.username.setText(user.getUsername());

        Glide.with(mContext).load(user.getImageUrl()).into(holder.avatar);

        isFollowing(user.getId(), holder.btn_follow);


        if (user.getId().equals(firebaseUser.getUid()))
            holder.btn_follow.setVisibility(View.GONE);




        holder.btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionReference reference = FirebaseFirestore.getInstance().collection("Follow");
                if (holder.btn_follow.getText().toString().equals("follow")) {
                    //Povezave
                    reference.document(firebaseUser.getUid()).collection("following").document(user.getId()).set(new HashMap<>());
                    FirebaseFirestore.getInstance().collection("Follow").document(user.getId()).collection("followers").document(firebaseUser.getUid()).set(new HashMap<>());

                    holder.btn_follow.setText("following");
                }
                else {
                    // Povezave
                    reference.document(firebaseUser.getUid()).collection("following").document(user.getId()).delete();
                    reference.document(user.getId()).collection("followers").document(firebaseUser.getUid()).delete();

                    holder.btn_follow.setText("follow");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public CircleImageView avatar;
        public Button btn_follow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            avatar = itemView.findViewById(R.id.user_avatar);
            btn_follow = itemView.findViewById(R.id.btn_follow);

        }
    }

    private void isFollowing (String userId, Button button) {
        FirebaseFirestore.getInstance().collection("Follow").document(firebaseUser.getUid()).collection("following").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult().getDocuments().toString().contains(userId))
                    button.setText("following");
                else
                    button.setText("follow");
            }
        });

    }


}
