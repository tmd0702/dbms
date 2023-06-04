package Database;
import MovieManager.*;
import Utils.*;
import javafx.scene.image.Image;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MovieManagementProcessor extends Processor {
    private MovieManager movieManager;
    IdGenerator idGenerator;
    Processor scheduleManagementProcessor;

    public MovieManagementProcessor() throws Exception {
        super();
        setDefaultDatabaseTable("MOVIES");
        movieManager = new MovieManager();
        this.idGenerator = new IdGenerator();
        this.scheduleManagementProcessor = new ScheduleManagementProcessor();
    }
    public MovieManager getMovieManager() {
        return this.movieManager;
    }
    public String getMovieGenres(String queryCondition) {
        String movieGenres = "";
        String query =String.format("SELECT NAME FROM GENRES G JOIN MOVIE_GENRES MG ON G.ID = MG.GENRE_ID WHERE %s", queryCondition);
        try {
            Statement st = getConnector().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                if (movieGenres == "") {
                    movieGenres += rs.getString("NAME");
                } else {
                    movieGenres += ", " + rs.getString("NAME");
                }
            }
            st.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return movieGenres;
    }
    public int countData(String queryCondition) {
        return count(queryCondition, getDefaultDatabaseTable());
    }
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        Response response = select("*", from, quantity, queryCondition, sortQuery, getDefaultDatabaseTable());
        return response;
    }
    public void getMovies() {
        String query = "SELECT * FROM MOVIES LIMIT 30";// LIMIT 30";
        ArrayList<Movie> tmpList = new ArrayList<Movie>();
        try {
            Statement st = getConnector().createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                Movie movie = new Movie(rs.getString("ID"), rs.getString("TITLE"), rs.getString("OVERVIEW"), rs.getString("STATUS"), rs.getInt("DURATION"), rs.getInt("VIEW_COUNT"), rs.getDate("RELEASE_DATE"), rs.getString("POSTER_PATH"), rs.getString("BACKDROP_PATH"), rs.getString("LANGUAGE"));
                tmpList.add(movie);
            }
            ExecutorService service = Executors.newCachedThreadPool();
            for (Movie movie : tmpList) {
                service.execute(() -> {
                    movie.setBackdropImage(new Image(movie.getBackdropPath()));
                    movie.setPosterImage(new Image(movie.getPosterPath()));
                });

            }
            service.shutdown();
            try {
                service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + e);
            }
            System.out.println("Start loading genres");
            for (Movie movie : tmpList) {
                String getGenresSQL = String.format("SELECT G.NAME FROM GENRES G JOIN MOVIE_GENRES MG WHERE MG.MOVIE_ID = %s AND MG.GENRE_ID = G.ID", movie.getId());
                ResultSet genres = st.executeQuery(getGenresSQL);
                while (genres.next()) {
                    movie.addGenre(genres.getString("NAME"));
                }
                System.out.println(movie.getTitle() + " load genres done");
                genres.close();
            }
            // remake the release date
            String setReleasedate = "UPDATE MOVIES SET RELEASE_DATE = DATE_FORMAT(RELEASE_DATE ,'2023-%m-%d');";
            st.executeUpdate(setReleasedate);
            Collections.sort(tmpList, Comparator.comparingInt(Movie::getDuration));
            System.out.println(tmpList.size());
            for(Movie movie : tmpList) {
//                scheduleMovie(movie);
            }
            this.movieManager.setMovieList(tmpList);
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Response updateData(HashMap<String, String> columnValueMap, String queryCondition, boolean isCommit) {
        return update(columnValueMap, queryCondition, getDefaultDatabaseTable(), isCommit);
    }
    public void scheduleMovie(Movie movie) throws Exception {
        if (movie.getDuration() == 0) {
            System.out.println("Error: Invalid duration");
            throw new Exception("Error: Invalid duration");
        }
        Date movieShowDate = movie.getReleaseDate();
        ArrayList<ArrayList<String>> showTimesFetcher = main.getProcessorManager().getShowTimeManagementProcessor().getData(0, -1, "", "SHOW_TIMES.START_TIME ASC").getData();
        ArrayList<ArrayList<String>> cinemasFetcher = main.getProcessorManager().getCinemaManagementProcessor().getData(0, -1, "", "").getData();
        for(int i = 2; i < cinemasFetcher.size();++i) {
            String cinemaId = Utils.getRowValueByColumnName(i, "CINEMAS.ID", cinemasFetcher);
            ArrayList<ArrayList<String>> screenRoomsFetcher = main.getProcessorManager().getScreenRoomManagementProcessor().getData(0, -1, String.format("CINEMAS.ID = '%s'", Utils.getRowValueByColumnName(i, "CINEMAS.ID", cinemasFetcher )), "").getData();
            for (int j = 2; j < screenRoomsFetcher.size(); ++ j) {
                String screenRoomId = Utils.getRowValueByColumnName(j, "SCREEN_ROOMS.ID", screenRoomsFetcher);
                System.out.print(String.format("\nScheduling movie %s on cinema %s, screen room %s, release date ", movie.getId(), cinemaId, screenRoomId));
                for (int nDay = 0; nDay < Integer.parseInt(main.getConfig().get("MOVIE_SHOW_DURATION_DAYS_FROM_RELEASE_DATE")); ++nDay) {
                    String movieShowDateString = Utils.addDateByNDays(movieShowDate, nDay);
                    System.out.println(movieShowDateString);
                    for (int k = 2; k < showTimesFetcher.size(); ++k) {
                        String showTime = Utils.getRowValueByColumnName(k, "SHOW_TIMES.START_TIME", showTimesFetcher);
                        String showTimeId = Utils.getRowValueByColumnName(k, "SHOW_TIMES.ID", showTimesFetcher);
                        if (main.getProcessorManager().getScheduleManagementProcessor().isMovieSchedulingAvailable(movie.getId(), showTime, screenRoomId, cinemaId, movieShowDateString)) {
                            addFakeSchedules(showTimeId, movie.getId(), screenRoomId, movieShowDateString);
                        }
                    }
                }
            }
        }
    }
    public void addFakeSchedules(String showtimeID, String movieId, String screenRoomId, String showDate) throws Exception{
        HashMap<String, String> schedule = new HashMap<String, String>();
        schedule.put("ID", idGenerator.generateId(scheduleManagementProcessor.getDefaultDatabaseTable()));
        schedule.put("SHOW_TIME_ID", showtimeID);
        schedule.put("MOVIE_ID", movieId);
        schedule.put("SCREEN_ROOM_ID", screenRoomId);
        schedule.put("SHOW_DATE", showDate);
        Response response = scheduleManagementProcessor.insertData(schedule, true);
        if (response.getStatusCode() == StatusCode.OK) {
            System.out.println(String.format("Scheduled movie id %s success on showtime id %s with id %s", movieId, showtimeID, schedule.get("ID")));
        } else {
            System.out.println(response.getMessage());
        }
    }
    public String getPreviousID(String query, String id)
    {
        String previousID = "";
        try {
            Statement st = getConnector().createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                if(rs.getString("ID") == id)
                    rs.previous();
                previousID = rs.getString("ID");
                break;
            }

        }catch(Exception e){
            System.out.println(e);
        }
        return previousID;

    }
    public String getOneColumnData(String query, String id1, String id2, String column){
        String data = "";
        query = String.format(query, id1, id2);
        try{
            Statement st = getConnector().createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                data = rs.getString(column);
            }
        }catch (Exception e) {
            System.out.println(e);
        }
        return data;
    }
    public ArrayList<String> createList(String table, String condition){
        String query = String.format("SELECT * FROM %s %s;", table, condition);
        ArrayList<String> data = new ArrayList<String>();
        try{
            Statement st = getConnector().createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                data.add(rs.getString("ID"));
            }
        }catch (Exception e) {
            System.out.println(e);
        }
        return data;
    }
    public Response insertData(HashMap<String, String> columnValueMap, boolean isCommit) {
        return insert(columnValueMap, getDefaultDatabaseTable(), isCommit);
    }
    public Response deleteData(String queryCondition, boolean isCommit) {
        return delete(queryCondition, getDefaultDatabaseTable(), isCommit);
    }
}
