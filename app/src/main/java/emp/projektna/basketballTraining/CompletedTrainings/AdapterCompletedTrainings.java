package emp.projektna.basketballTraining.CompletedTrainings;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import emp.projektna.basketballTraining.R;
import emp.projektna.basketballTraining.Trainings.TrainigsActivity;
import emp.projektna.basketballTraining.Trainings.UseTraining.UseTrainingFragment;

public class AdapterCompletedTrainings extends RecyclerView.Adapter<AdapterCompletedTrainings.MyViewHolder> {

    Context context;

    ArrayList<ModelCompletedTrainings> modelTrainingsArrayList = new ArrayList<>();


    public AdapterCompletedTrainings(Context context, ArrayList<ModelCompletedTrainings> modelTrainingsArrayList) {
        this.context = context;
        this.modelTrainingsArrayList = modelTrainingsArrayList;
    }

    @NonNull
    @Override
    public AdapterCompletedTrainings.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_training, parent, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCompletedTrainings.MyViewHolder holder, int position) {
        final ModelCompletedTrainings modelTrainings = modelTrainingsArrayList.get(position);
        holder.tv_name.setText(modelTrainings.getName());
        holder.tv_exercises.setText(String.valueOf(modelTrainings.getExercises()));
        holder.trainingID = modelTrainings.getTrainingID();
        holder.date.setText(modelTrainings.getDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new UseTrainingFragment();
                Bundle bundle = new Bundle();
                bundle.putString("trainingID", holder.trainingID);
                bundle.putString("trainingName", holder.tv_name.getText().toString());
                fragment.setArguments(bundle);
                ((TrainigsActivity) context).setFragment(fragment);
            }
        });
    }


    @Override
    public int getItemCount() {
        return modelTrainingsArrayList.size();
    }


    public static class  MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name;
        TextView tv_exercises;
        TextView date;
        String trainingID;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.tv_exercise_type);
            tv_name = (TextView) itemView.findViewById(R.id.tv_training_name);
            tv_exercises = (TextView) itemView.findViewById(R.id.exercises);
        }
    }

}
