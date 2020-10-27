package emp.projektna.basketballTraining;

public class ModelFeed {

    int id, comments, likes, free_throws, threes;
    String name, time, duration;

    public ModelFeed(int id, int comments, int likes, int free_throws, int threes, String duration, String name, String time) {
        this.id = id;
        this.comments = comments;
        this.likes = likes;
        this.free_throws = free_throws;
        this.threes = threes;
        this.duration = duration;
        this.name = name;
        this.time = time;
    }

    public int getFree_throws() {
        return free_throws;
    }

    public void setFree_throws(int free_throws) {
        this.free_throws = free_throws;
    }

    public int getThrees() {
        return threes;
    }

    public void setThrees(int threes) {
        this.threes = threes;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
