package emp.projektna.basketballTraining.AddTraining;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import emp.projektna.basketballTraining.R;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_exercise, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final ModelExercise modelExercise = modelExerciseArrayList.get(position);

        holder.exerciseName.setText(modelExercise.getName());
        holder.number.setText((position + 1) + ".");
        holder.length.setText(String.valueOf(modelExercise.getLength()));
        holder.type.setText(modelExercise.getType());
    }

    @Override
    public int getItemCount() {
        return modelExerciseArrayList.size();
    }


    public class  MyViewHolder extends RecyclerView.ViewHolder{
        TextView exerciseName, number, length, type;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseName = (TextView)itemView.findViewById(R.id.tv_exercise_name);
            number = (TextView)itemView.findViewById(R.id.tv_number);
            length = (TextView)itemView.findViewById(R.id.length);
            type = (TextView)itemView.findViewById(R.id.tv_exercise_type);
        }
    }
}
