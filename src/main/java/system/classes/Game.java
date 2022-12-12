package system.classes;

public class Game {
    String id;
    String name;
    String shortDesc;
    String imageUrl;

    public Game() {
    }

    public Game(String id, String name, String shortDesc, String imageUrl) {
        this.id = id;
        this.name = name;
        this.shortDesc = shortDesc;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", shortDesc='" + shortDesc + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
