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

public class AdapterExercise extends RecyclerView.Adapter<AdapterExercise.MyViewHolder> {

    Context context;

    ArrayList<ModelExercise> modelExerciseArrayList = new ArrayList<>();


    public AdapterExercise(Context context, ArrayList<ModelExercise> modelFeedArrayList) {
        this.context = context;
        this.modelExerciseArrayList = modelFeedArrayList;
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

        final ModelExercise modelExercise = modelExerciseArrayList.get(position);

        holder.exerciseName.setText(modelExercise.getName());

    }

    @Override
    public int getItemCount() {
        return modelExerciseArrayList.size();
    }


    public class  MyViewHolder extends RecyclerView.ViewHolder{
        TextView exerciseName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            exerciseName = (TextView)itemView.findViewById(R.id.et_exercise_name);

        }
    }
}
