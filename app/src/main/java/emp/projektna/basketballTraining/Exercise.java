package emp.projektna.basketballTraining;

public class Exercise {
    private String name;
    private int length;
    private int position;
    private String description;
    private int repeats;
    private boolean timer;

    public Exercise(String name, int length, int position, String description, int repeats, Boolean timer){
        this.name = name;
        this.length = length;
        this.position = position;
        this.description = description;
        this.repeats = repeats;
        this.timer = timer;
    }
}
