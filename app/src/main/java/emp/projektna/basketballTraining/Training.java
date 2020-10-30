package emp.projektna.basketballTraining;

import java.util.ArrayList;
import java.util.List;

public class Training {

    private List<Exercise> exercises = new ArrayList<>();

    public void addTraining(Exercise exercise){
        exercises.add(exercise);
    }
}
