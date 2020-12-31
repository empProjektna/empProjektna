package emp.projektna.basketballTraining.Trainings;

public class ModelTrainings {
    String name, trainingID;
    int exercises;

    public String getName() {
        return name;
    }

    public String getTrainingID() { return trainingID; }

    public int getExercises() {
        return exercises;
    }

    public ModelTrainings(String name, int exercises, String trainingID) {
        this.name = name;
        this.exercises = exercises;
        this.trainingID = trainingID;
    }
}
