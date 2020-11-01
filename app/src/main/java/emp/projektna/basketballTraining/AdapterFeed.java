package emp.projektna.basketballTraining;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterFeed extends RecyclerView.Adapter<AdapterFeed.MyViewHolder> {

    Context context;

    ArrayList<ModelFeed> modelFeedArrayList = new ArrayList<>();


    public AdapterFeed(Context context, ArrayList<ModelFeed> modelFeedArrayList) {
        this.context = context;
        this.modelFeedArrayList = modelFeedArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_feed, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final ModelFeed modelFeed = modelFeedArrayList.get(position);

        holder.tv_name.setText(modelFeed.getName());
        holder.tv_comments.setText(modelFeed.getComments() + " comments");
        holder.tv_likes.setText(modelFeed.getLikes() + " likes");
        holder.tv_time.setText(modelFeed.getTime());
        holder.tv_threes.setText(String.valueOf(modelFeed.getThrees()));
        holder.tv_free_throws.setText(String.valueOf(modelFeed.getFree_throws()));
        holder.tv_duration.setText(modelFeed.getDuration());

    }

    @Override
    public int getItemCount() {
        return modelFeedArrayList.size();
    }


    public class  MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name, tv_time, tv_likes, tv_comments, tv_free_throws, tv_threes, tv_duration;
        ImageView imgView_profilePic;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgView_profilePic = (ImageView)itemView.findViewById(R.id.row_profile_image);

            tv_name = (TextView)itemView.findViewById(R.id.display_name);
            tv_time = (TextView)itemView.findViewById(R.id.display_time);
            tv_likes = (TextView)itemView.findViewById(R.id.likes);
            tv_comments = (TextView)itemView.findViewById(R.id.comments);
            tv_free_throws = (TextView)itemView.findViewById(R.id.free_throws);
            tv_threes = (TextView)itemView.findViewById(R.id.threes);
            tv_duration = (TextView)itemView.findViewById(R.id.duration);

        }
    }
}
