package example.movies.backend;

import java.util.ArrayList;

/**
 * Created by pinar on 27.05.2016.
 */
public class Movies {

    String id;
    String title;
    String releasedYear;
    String rating;
    String genre;
    String director;
    ArrayList<Actors> actorList = new ArrayList<Actors>();

    public ArrayList<Actors> getActorList() {
        return actorList;
    }

    public void setActorList(ArrayList<Actors> actorList) {
        this.actorList = actorList;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleasedYear() {
        return releasedYear;
    }

    public void setReleasedYear(String releasedYear) {
        this.releasedYear = releasedYear;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

}
