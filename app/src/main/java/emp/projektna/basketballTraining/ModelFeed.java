package emp.projektna.basketballTraining;

public class ModelFeed {

    int id, comments, likes;
    String name, time;

    public ModelFeed(int id, int comments, String name, String time, int likes) {
        this.id = id;
        this.comments = comments;
        this.name = name;
        this.time = time;
        this.likes = likes;
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
