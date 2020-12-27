package emp.projektna.basketballTraining.AddTraining;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class Training {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<ModelExercise> exercises;

    public void addExercise(ModelExercise exercise){
        exercises.add(exercise);
    }

    public ModelExercise getExercise (int i) {
        return exercises.get(i);
    }

    public boolean exerciseExists(int i) {
        return i <= exercises.size();
    }

    public boolean isEmpty() {
        return exercises.size() == 0;
    }

    public List<ModelExercise> getExercises() {
        return exercises;
    }

    public void removeExercise(int i) {
        exercises.remove(i);
    }

     /*
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
                });
    }
*/
}
