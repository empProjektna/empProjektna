package emp.projektna.basketballTraining.CompletedTrainings.ViewCompletedTraining;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ModelViewExercise {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private String name;
    private String type;
    private Long length;
    private String description;
    private Long sets;
    private Boolean isShooting;
    private String ID;

    public ArrayList<Integer> getPositions() {
        return positions;
    }

    public void setPositions(ArrayList<Integer> positions) {
        this.positions = positions;
    }

    private ArrayList<Integer> positions;


    public ModelViewExercise(String name, Long length, String description, Long sets, Boolean isShooting, ArrayList<Integer> positions, String type, String ID) {
        this.name = name;
        this.type = type;
        this.length = length;
        this.description = description;
        this.sets = sets;
        this.isShooting = isShooting;
        this.positions = positions;
        this.ID = ID;
    }


    public String getName() {
        return name;
    }

    public String getID() {
        return ID;
    }

    public String getType() {
        return type;
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

    boolean success = false;




}
