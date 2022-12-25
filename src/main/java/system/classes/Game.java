package system.classes;

public class Game {
    String id;
    String name;
    String rating;

    String genre;
    String tag;

    String review;
    String desc;
    String shortDesc;
    String imageUrl;
    public Game() {
    }

    public Game(String id, String name, String rating, String genre, String tag, String review, String des, String shortDesc, String imageUrl) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.genre = genre;
        this.tag = tag;

        this.review = review;
        this.desc = des;
        this.shortDesc = shortDesc;
        this.imageUrl = imageUrl;
    }

    public Game(String id, String name, String rating, String shortDesc, String imageUrl) {
        this.id = id;
        this.name = name;
        this.rating = rating;
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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }





    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

