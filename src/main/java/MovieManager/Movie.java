package MovieManager;

import com.example.GraphicalUserInterface.Main;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Movie {
    private String id, title, overview, movieStatus, posterPath, backdropPath, tagline, language, basePath = "https://www.themoviedb.org/t/p/original";
    private int duration, viewCount, voteCount;
    private ArrayList<String> genres;
    public float voteAverage;
    private Image backdropImage, posterImage;
    public double revenue;
    private Date releaseDate;
    public Movie() {
        this.tagline = "";
        this.language = "";
        this.voteCount = 0;
        this.voteAverage = 0;
        this.revenue = 0;
        this.genres = new ArrayList<String>();
        this.posterPath = "";
        this.backdropPath = "";
        this.id = "";
        this.title = "";
        this.overview = "";
        this.movieStatus = "";
        this.duration = 0;
        this.viewCount = 0;
        this.releaseDate = new Date();
    }
    public Image getPosterImage() {
        return this.posterImage;
    }
    public String getLanguage() {
        return this.language;
    }
    public Movie(String id, String title, String overview, String movieStatus, int duration, int viewCount, Date releaseDate, String posterPath, String backdropPath, String language) {
        this.language = language;
        this.id = id;
        this.backdropPath = backdropPath;
        this.genres = new ArrayList<String>();
        this.posterPath = posterPath;
        System.out.println(this.getPosterPath() + " " + title);
        this.title = title;
        this.overview = overview;
        this.movieStatus = movieStatus;
        this.duration = duration;
        this.viewCount = viewCount;
        this.releaseDate = releaseDate;
    }
    public void addGenre(String genre) {
        this.genres.add(genre);
    }
    public String getPosterPath() {
        return this.basePath + this.posterPath;
    }
    public Image getBackdropImage() {
        return this.backdropImage;
    }
    public ArrayList<String> getGenres() {
        return this.genres;
    }
    public void setBackdropImage(Image backdropImage) {
        this.backdropImage = backdropImage;
        if (getBackdropImage().getProgress() != 1 || getBackdropImage().isError()) {
            this.backdropImage = Main.getInstance().getProcessorManager().getMovieManagementProcessor().getMovieManager().getImageNotFound();
        }
    }

    public void setPosterImage(Image posterImage) {
        this.posterImage = posterImage;
        if (getPosterImage().getProgress() != 1 || getPosterImage().isError()) {
            this.posterImage = Main.getInstance().getProcessorManager().getMovieManagementProcessor().getMovieManager().getImageNotFound();
        }
    }

    public int getDuration() {
        return this.duration;
    }
    public int getViewCount() {
        return this.viewCount;
    }
    public String getBackdropPath() {
        return this.basePath + this.backdropPath;
    }
    public String getId() {
        return this.id;
    }
    public String getTitle() {
        return this.title;
    }
    public Date getReleaseDate() {
        return this.releaseDate;
    }
    public String getOverview() {
        return this.overview;
    }
    public void setMovieStatus(String status){this.movieStatus = status;}
    public String getMovieStatus(){return this.movieStatus;}
}
