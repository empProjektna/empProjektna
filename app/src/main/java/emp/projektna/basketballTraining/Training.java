package emp.projektna.basketballTraining;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Training {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Exercise> exercises = new ArrayList<>();

    public void addTraining(Exercise exercise){
        exercises.add(exercise);
    }

    public int numberOfExercises(){
        return exercises.size();
    }

    public void deleteAllExercises() {
        exercises.clear();
    }

    public void deleteExerciseAtIndex(int i) {
        exercises.remove(i);
    }

    public Exercise getExerciseWithIndex(int i){
        return exercises.get(i);
    }

    public void saveTraining(){
        Map<String, Object> databaseInput = new HashMap<>();
        for (Exercise exercise: exercises) {
            databaseInput.put("NAME", exercise.getName());
            databaseInput.put("LENGTH", exercise.getLength());
            databaseInput.put("DESCRIPTION", exercise.getDescription());
            databaseInput.put("POSITION", exercise.getPosition());
            databaseInput.put("REPEATS", exercise.getRepeats());
            databaseInput.put("TIMER", exercise.isTimer());
        }
            this.deleteAllExercises();
        // TODO

/*
        db.collection("Trainings").document("Training").set(databaseInput)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });*/
    }

}
