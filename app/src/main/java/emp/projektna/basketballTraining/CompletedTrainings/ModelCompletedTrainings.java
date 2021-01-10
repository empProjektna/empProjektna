package emp.projektna.basketballTraining.CompletedTrainings;

public class ModelCompletedTrainings {
    String name, trainingID, date;
    int exercises;

    public String getName() {
        return name;
    }

    public String getTrainingID() { return trainingID; }

    public int getExercises() {
        return exercises;
    }

    public String getDate() {
        return date;
    }

    public ModelCompletedTrainings(String name, int exercises, String trainingID, String date) {
        this.name = name;
        this.exercises = exercises;
        this.trainingID = trainingID;
        this.date = date;
    }


}
