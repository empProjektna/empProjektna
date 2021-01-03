package emp.projektna.basketballTraining.Trainings;

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
import emp.projektna.basketballTraining.Trainings.UseTraining.UseTrainingFragment;

public class AdapterTrainings extends RecyclerView.Adapter<AdapterTrainings.MyViewHolder> {

    Context context;

    ArrayList<ModelTrainings> modelTrainingsArrayList = new ArrayList<>();


    public AdapterTrainings(Context context, ArrayList<ModelTrainings> modelTrainingsArrayList) {
        this.context = context;
        this.modelTrainingsArrayList = modelTrainingsArrayList;
    }

    @NonNull
    @Override
    public AdapterTrainings.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_training, parent, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTrainings.MyViewHolder holder, int position) {
        final ModelTrainings modelTrainings = modelTrainingsArrayList.get(position);
        holder.tv_name.setText(modelTrainings.getName());
        holder.tv_exercises.setText(String.valueOf(modelTrainings.getExercises()));
        holder.trainingID = modelTrainings.getTrainingID();
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
        String trainingID;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.tv_training_name);
            tv_exercises = (TextView) itemView.findViewById(R.id.exercises);

        }
    }

}
