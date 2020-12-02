package emp.projektna.basketballTraining;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Training {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    static private Map<String, List<ModelExercise>> trainigs = new HashMap<>();


    public void addTraining(String id){
        List<ModelExercise> exercises = new ArrayList<>();
        trainigs.put(id, exercises);
    }

    public void addExercise(ModelExercise exercise, String id){
        List<ModelExercise> exercises = trainigs.get(id);
        exercises.add(exercise);
        trainigs.put(id, exercises);
    }

    public String out(String id) {
        return String.valueOf(trainigs.get(id).size());
    }

   /* public int numberOfTrainings(){
        return trainigs.size();
    }

    public void deleteAllExercises() {
        trainigs.clear();
    }

    public void deleteExerciseAtIndex(int i) {
        trainigs.remove(i);
    }

    public Exercise getExerciseWithIndex(int i){
        return trainigs.get(i);
    }
    */


    public void saveTraining(){

        /*
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
*/
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
