package emp.projektna.basketballTraining.CompletedTrainings.ViewCompletedTraining;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import emp.projektna.basketballTraining.R;

public class AdapterViewExercise extends RecyclerView.Adapter<AdapterViewExercise.MyViewHolder> {

    Context context;

    ArrayList<ModelViewExercise> modelExerciseArrayList = new ArrayList<>();

    Fragment fragment;


    public AdapterViewExercise(Context context, ArrayList<ModelViewExercise> modelFeedArrayList, Fragment fragment) {
        this.context = context;
        this.modelExerciseArrayList = modelFeedArrayList;
        this.fragment = fragment;
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

        final ModelViewExercise modelExercise = modelExerciseArrayList.get(position);

        holder.exerciseName.setText(modelExercise.getName());
        holder.number.setText((position + 1) + ".");
        holder.length.setText(String.valueOf(modelExercise.getLength()));
        holder.type.setText(modelExercise.getType());
        if (holder.type.getText().toString().equals("Shooting")) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment newFragment = ViewPositionFragment.getInstance();
                    newFragment.setTargetFragment(fragment, 1);

                    Bundle args = new Bundle();
                    // args.putIntegerArrayList("selected", holder.positions);
                    args.putString("ID", modelExercise.getID());
                    args.putInt("index", position);
                    newFragment.setArguments(args);
                    newFragment.show(fragment.getFragmentManager(), "dialog");

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return modelExerciseArrayList.size();
    }


    public class  MyViewHolder extends RecyclerView.ViewHolder{
        TextView exerciseName, number, length, type;
        //String ID;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseName = (TextView)itemView.findViewById(R.id.tv_exercise_name);
            number = (TextView)itemView.findViewById(R.id.tv_number);
            length = (TextView)itemView.findViewById(R.id.length);
            type = (TextView)itemView.findViewById(R.id.tv_exercise_type);
        }
    }
}
