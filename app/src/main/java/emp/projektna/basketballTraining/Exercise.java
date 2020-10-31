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
}
