package emp.projektna.basketballTraining;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ModelExercise {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private String name;
    private int length;
    private int position;
    private String description;
    private int repeats;
    private boolean timer;


    public ModelExercise(String name, int length, int position, String description, int repeats, Boolean timer) {
        this.name = name;
        this.length = length;
        this.position = position;
        this.description = description;
        this.repeats = repeats;
        this.timer = timer;
    }


    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }

    public int getPosition() {
        return position;
    }

    public String getDescription() {
        return description;
    }

    public int getRepeats() {
        return repeats;
    }

    public boolean isTimer() {
        return timer;
    }

    boolean success = false;

    public boolean uploadToFirestore(String id) {
            Map<String, Object> databaseInput = new HashMap<>();
            databaseInput.put("NAME", name);
            databaseInput.put("LENGTH", length);
            databaseInput.put("DESCRIPTION", description);
            databaseInput.put("POSITION", position);
            databaseInput.put("REPEATS", repeats);
            databaseInput.put("TIMER", timer);

            String uniqueID = String.valueOf(System.currentTimeMillis());
            db.collection("Trainigs").document(firebaseAuth.getUid()).collection(uniqueID).document(id).set(databaseInput).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    success = true;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                success = false;
                }
            });
            return success;
        }



}
