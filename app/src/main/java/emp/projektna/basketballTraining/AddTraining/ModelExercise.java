package emp.projektna.basketballTraining.AddTraining;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModelExercise {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private String name;
    private Long length;
    private String description;
    private Long sets;
    private Boolean timer, isShooting;

    public ArrayList<Integer> getPositions() {
        return positions;
    }

    public void setPositions(ArrayList<Integer> positions) {
        this.positions = positions;
    }

    private ArrayList<Integer> positions;


    public ModelExercise(String name, Long length, String description, Long sets, Boolean timer, Boolean isShooting, ArrayList<Integer> positions) {
        this.name = name;
        this.length = length;
        this.description = description;
        this.sets = sets;
        this.timer = timer;
        this.isShooting = isShooting;
        this.positions = positions;
    }


    public String getName() {
        return name;
    }

    public long getLength() {
        return length;
    }

    public String getDescription() {
        return description;
    }

    public long getSets() {
        return sets;
    }

    public boolean isTimer() {
        return timer;
    }

    boolean success = false;

    public void uploadToFirestore(String id) {
            Map<String, Object> databaseInput = new HashMap<>();
            databaseInput.put("NAME", name);
            databaseInput.put("LENGTH", length);
            databaseInput.put("DESCRIPTION", description);
            databaseInput.put("SETS", sets);
            databaseInput.put("TIMER", timer);
            databaseInput.put("POSITIONS", positions);
            databaseInput.put("SHOOTING", isShooting);
            String uniqueID = String.valueOf(System.currentTimeMillis());
            db.collection("Exercises").document(uniqueID).set(databaseInput);
            db.collection("Trainings").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        ArrayList<String> array;
                        if (task.getResult().contains("exercises")) {
                            array = (ArrayList<String>) task.getResult().get("exercises");
                            array.add(uniqueID);
                            Map<String, Object> dbInput = new HashMap<String, Object>(){{put("exercises", array);}};
                            db.collection("Trainings").document(id).update(dbInput);
                        }
                        else {
                            array = new ArrayList<>();
                            array.add(uniqueID);
                            Map<String, Object> dbInput = new HashMap<>();
                            dbInput.put("exercises", array);
                            db.collection("Trainings").document(id).update(dbInput);
                            Log.e("bal", task.getResult().getData().toString());
                        }
                    }
                }
            });
        }



}
