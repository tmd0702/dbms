package MovieManager;
import java.util.*;
import Utils.Utils;
import javafx.scene.image.Image;

public class MovieManager {
    private ArrayList<Movie> movieList;
    private Image imageNotFound;
    public MovieManager() {
        this.movieList = new ArrayList<Movie>();
        this.imageNotFound = new Image("https://westsiderc.org/wp-content/uploads/2019/08/Image-Not-Available.png");
    }
    public Image getImageNotFound() {
        return this.imageNotFound;
    }
    public ArrayList<Movie> getMovieList() {
        return this.movieList;
    }
    public ArrayList<Movie> getCurrentlyPlayingMovieList() {
//        ArrayList<Movie> currentlyPlayingMovieList = new ArrayList<Movie>();
//        for (Movie movie : this.movieList) {
//            long diff = Utils.getDiffBetweenDates(movie.getReleaseDate(), new Date());
//            if (diff < 7 && diff >= 0) {
//                currentlyPlayingMovieList.add(movie);
//            }
//        }
//        return currentlyPlayingMovieList;
        ArrayList<Movie> currentlyPlayingMovieList = new ArrayList<Movie>();
        for (int i=0; i<Math.round(movieList.size() / 2);++i) {
            currentlyPlayingMovieList.add(movieList.get(i));
        }
        return currentlyPlayingMovieList;
    }
    public ArrayList<Movie> getComingSoonMovieList() {
//        ArrayList<Movie> comingSoonMovieList = new ArrayList<Movie>();
//        for (Movie movie: this.movieList) {
//            long diff = Utils.getDiffBetweenDates(movie.getReleaseDate(), new Date());
//            if (diff < 0) {
//                comingSoonMovieList.add(movie);
//            }
//        }
//        return comingSoonMovieList;
        ArrayList<Movie> comingSoonMovieList = new ArrayList<Movie>();
        for (int i=Math.round(movieList.size() / 2); i < movieList.size(); ++i) {
            comingSoonMovieList.add(movieList.get(i));
        }
        return comingSoonMovieList;
    }
    public MovieManager(ArrayList<Movie> movieList) {
        this.movieList = movieList;
    }
    public Movie getMovieById(String id) {
        Movie movie = null;
        for (Movie m : this.movieList) {
            if (m.getId().equals(id)) {
                movie = m;
                break;
            }
        }
        return movie;
    }
    public void setMovieList(ArrayList<Movie> movieList) {
        this.movieList = movieList;
    }
    public void addMovie(Movie movie) {
        try {
            this.movieList.add(movie);
        } catch (Exception e) {
            System.out.println(e);
            throw(e);
        }
    }

}
