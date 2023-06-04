package Database;

import Database.Processor;
import MovieManager.Movie;
import Utils.Response;
import Utils.Utils;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

public class ScheduleManagementProcessor extends Processor {
    public ScheduleManagementProcessor() {
        super();
        setDefaultDatabaseTable("SCHEDULES");
    }
    public Response insertData(HashMap<String, String> columnValueMap, boolean isCommit) {
        return insert(columnValueMap, getDefaultDatabaseTable(), isCommit);
    }
    public Response deleteData(String queryCondition, boolean isCommit) {
        return delete(queryCondition, getDefaultDatabaseTable(), isCommit);
    }
    public boolean isMovieScheduledTooMuchTimes(String movieId, String screenRoomId, String movieShowDateString) {
        ArrayList<ArrayList<String>> movieScheduledTimesFetcher = select("COUNT(*) AS COUNT", 0, -1, String.format("SCREEN_ROOM_ID = '%s' AND MOVIE_ID = '%s' AND SHOW_DATE = '%s'", screenRoomId, movieId, movieShowDateString), "", "SCHEDULES").getData();
        int movieScheduledTimes = Integer.parseInt(Utils.getRowValueByColumnName(2, "COUNT", movieScheduledTimesFetcher));
        if (movieScheduledTimes >= Integer.parseInt(main.getConfig().get("MAXIMUM_MOVIE_SHOW_TIMES_IN_ONE_SCREEN_ROOM"))) {
            System.out.println(String.format("Movie id %s has been scheduled 3 times, ignore scheduling", movieId));
            return true;
        } else {
            return false;
        }
    }
    public boolean isMovieScheduledTooSoonInScreenRoom(String movieId, String showTime, String screenRoomId, String movieShowDateString) {
        ArrayList<ArrayList<String>> previousScheduleShowTimeInScreenRoomFetcher = select("S.SHOW_DATE, ST.START_TIME", 0, 1, String.format("S.MOVIE_ID = '%s' AND ST.ID = S.SHOW_TIME_ID AND ST.START_TIME < '%s' AND S.SCREEN_ROOM_ID = '%s' AND S.SHOW_DATE = '%s'", movieId, showTime, screenRoomId, movieShowDateString), "ST.START_TIME DESC", "SCHEDULES S, SHOW_TIMES ST").getData();
        String previousScheduleShowTimeInScreenRoom = Utils.getRowValueByColumnName(2, "START_TIME", previousScheduleShowTimeInScreenRoomFetcher);
        if (previousScheduleShowTimeInScreenRoom == null) {
            return false;
        } else {
            long timeGap = LocalTime.parse(previousScheduleShowTimeInScreenRoom).until(LocalTime.parse(showTime), ChronoUnit.MINUTES);
            if (timeGap < Integer.parseInt(main.getConfig().get("MINIMUM_MINUTES_BETWEEN_SHOW_TIME_OF_SAME_MOVIE_IN_PARTICULAR_SCREEN_ROOM"))) {
                System.out.println(String.format("Movie id %s has been scheduled in this screen room (id %s) %d minutes ago, ignore scheduling", movieId, screenRoomId, timeGap));
                return true;
            } else {
                System.out.println(String.format("Movie id %s has been scheduled in this screen room (id %s) %d minutes ago, screen room accept scheduling", movieId, screenRoomId, timeGap));
                return false;
            }
        }
    }
    public boolean isMovieScheduledTooSoonInCinema(String movieId, String showTime, String screenRoomId, String cinemaId, String movieShowDateString) {
        ArrayList<ArrayList<String>> previousScheduleShowTimeInCinemaFetcher = select("S.SHOW_DATE, ST.START_TIME", 0, 1, String.format("S.MOVIE_ID = '%s' AND ST.ID = S.SHOW_TIME_ID AND ST.START_TIME < '%s' AND S.SCREEN_ROOM_ID <> '%s' AND S.SCREEN_ROOM_ID IN (SELECT ID FROM SCREEN_ROOMS WHERE CINEMA_ID = '%s') AND S.SHOW_DATE = '%s'", movieId, showTime, screenRoomId, cinemaId, movieShowDateString), "ST.START_TIME DESC", "SCHEDULES S, SHOW_TIMES ST").getData();
        String previousScheduleShowTimeInCinema = Utils.getRowValueByColumnName(2, "START_TIME", previousScheduleShowTimeInCinemaFetcher);
        if (previousScheduleShowTimeInCinema == null) {
            return false;
        } else {
            long timeGap = LocalTime.parse(previousScheduleShowTimeInCinema).until(LocalTime.parse(showTime), ChronoUnit.MINUTES);
            if (timeGap < Integer.parseInt(main.getConfig().get("MINIMUM_MINUTES_BETWEEN_SHOW_TIME_OF_SAME_MOVIE_IN_PARTICULAR_CINEMA"))) {
                System.out.println(String.format("Movie id %s has been scheduled in this cinema (id %s) %d minutes ago, ignore scheduling", movieId, cinemaId, timeGap));
                return true;
            } else {
                System.out.println(String.format("Movie id %s has been scheduled in this cinema (id %s) %d minutes ago, cinema accept scheduling", movieId, cinemaId, timeGap));
                return false;
            }
        }
    }
    public boolean isMovieScheduledInOtherScreenRooms(String movieId, String showTime, String screenRoomId, String cinemaId, String movieShowDateString) {
        ArrayList<ArrayList<String>> currentScheduleShowTimeInOtherScreenRoomsFetcher = select("S.SHOW_DATE, ST.START_TIME", 0, 1, String.format("S.MOVIE_ID = '%s' AND ST.ID = S.SHOW_TIME_ID AND ST.START_TIME = '%s' AND S.SCREEN_ROOM_ID <> '%s' AND S.SCREEN_ROOM_ID IN (SELECT ID FROM SCREEN_ROOMS WHERE CINEMA_ID = '%s') AND S.SHOW_DATE = '%s'", movieId, showTime, screenRoomId, cinemaId, movieShowDateString), "", "SCHEDULES S, SHOW_TIMES ST").getData();
        String currentScheduleShowTimeInOtherScreenRooms = Utils.getRowValueByColumnName(2, "START_TIME", currentScheduleShowTimeInOtherScreenRoomsFetcher);
        if (currentScheduleShowTimeInOtherScreenRooms == null) {
            return false;
        } else {
            System.out.println(String.format("Movie id %s has been scheduled in another screen room at %s on %s", movieId, showTime, movieShowDateString));
            return true;
        }
    }
    public boolean isMovieScheduledConflict(String movieId, String showTime, String screenRoomId, String movieShowDateString) {
        ArrayList<ArrayList<String>> nearestNextScheduleShowTimeFetcher = select("S.SHOW_DATE, ST.START_TIME", 0, 1, String.format("ST.ID = S.SHOW_TIME_ID AND ST.START_TIME >= '%s' AND S.SCREEN_ROOM_ID = '%s' AND S.SHOW_DATE = '%s'", showTime, screenRoomId, movieShowDateString), "ST.START_TIME ASC", "SCHEDULES S, SHOW_TIMES ST").getData();
        ArrayList<ArrayList<String>> nearestPrevScheduleShowTimeFetcher = select("M.DURATION AS MOVIE_DURATION, S.MOVIE_ID, S.SHOW_DATE, ST.START_TIME", 0, 1, String.format("ST.ID = S.SHOW_TIME_ID AND ST.START_TIME < '%s' AND S.SCREEN_ROOM_ID = '%s' AND S.SHOW_DATE = '%s' AND M.ID = S.MOVIE_ID", showTime, screenRoomId, movieShowDateString), "ST.START_TIME DESC", "SCHEDULES S, SHOW_TIMES ST, MOVIES M").getData();
        int movieDuration = Integer.parseInt(Utils.getRowValueByColumnName(2, "DURATION", select("DURATION", 0, -1, String.format("ID = '%s'", movieId), "", "MOVIES").getData()));
        System.out.println("Duration: " + movieDuration);
        String nearestNextScheduleShowTime = Utils.getRowValueByColumnName(2, "START_TIME", nearestNextScheduleShowTimeFetcher);
        String nearestPrevScheduleShowTime = Utils.getRowValueByColumnName(2, "START_TIME", nearestPrevScheduleShowTimeFetcher);
        boolean isConflict = false;
        if (nearestNextScheduleShowTime != null) {
            long maxDurationMinutesAvailable = LocalTime.parse(showTime).until(LocalTime.parse(nearestNextScheduleShowTime), ChronoUnit.MINUTES);

            if (maxDurationMinutesAvailable < movieDuration + Integer.parseInt(main.getConfig().get("MINIMUM_MINUTES_BETWEEN_MOVIE_PLAYS"))) {
                System.out.println(String.format("Movie id %s cannot be scheduled due to insufficient time range", movieId));
                isConflict = true;
            }
        }
        if (nearestPrevScheduleShowTime != null) {
            int prevMovieDuration = Integer.parseInt(Utils.getRowValueByColumnName(2, "MOVIE_DURATION", nearestPrevScheduleShowTimeFetcher));
            long maxDurationMinutesAvailable = LocalTime.parse(nearestPrevScheduleShowTime).until(LocalTime.parse(showTime), ChronoUnit.MINUTES);
            if (maxDurationMinutesAvailable < prevMovieDuration + Integer.parseInt(main.getConfig().get("MINIMUM_MINUTES_BETWEEN_MOVIE_PLAYS"))) {
                System.out.println(String.format("Movie id %s cannot be scheduled due to insufficient time range", movieId));
                isConflict = true;
            }
        }
        return isConflict;
    }
    public boolean isMovieSchedulingAvailable(String movieId, String showTime, String screenRoomId, String cinemaId, String movieShowDateString) {
        if (isMovieScheduledTooMuchTimes(movieId, screenRoomId, movieShowDateString) || isMovieScheduledConflict(movieId, showTime, screenRoomId, movieShowDateString) || isMovieScheduledInOtherScreenRooms(movieId, showTime, screenRoomId, cinemaId, movieShowDateString) || isMovieScheduledTooSoonInScreenRoom(movieId, showTime, screenRoomId, movieShowDateString) || isMovieScheduledTooSoonInCinema(movieId, showTime, screenRoomId, cinemaId, movieShowDateString)){
            return false;
        } else {
            return true;
        }
    }
    public Response updateData(HashMap<String, String> columnValueMap, String queryCondition, boolean isCommit) {
        return update(columnValueMap, queryCondition, getDefaultDatabaseTable(), isCommit);
    }
    public int countData(String queryCondition) {
        if (queryCondition.length() > 0) {
            queryCondition = queryCondition + " AND SCHEDULES.MOVIE_ID = MOVIES.ID AND SCHEDULES.SCREEN_ROOM_ID = SCREEN_ROOMS.ID AND CINEMAS.ID = SCREEN_ROOMS.CINEMA_ID AND SCHEDULES.SHOW_TIME_ID = SHOW_TIMES.ID";
        } else {
            queryCondition = "SCHEDULES.MOVIE_ID = MOVIES.ID AND SCHEDULES.SCREEN_ROOM_ID = SCREEN_ROOMS.ID AND CINEMAS.ID = SCREEN_ROOMS.CINEMA_ID AND SCHEDULES.SHOW_TIME_ID = SHOW_TIMES.ID";
        }
        return count(queryCondition, "SCHEDULES, MOVIES, SHOW_TIMES, SCREEN_ROOMS, CINEMAS");
    }
    @Override
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        if (queryCondition.length() > 0) {
            queryCondition = queryCondition + " AND SCHEDULES.MOVIE_ID = MOVIES.ID AND SCHEDULES.SCREEN_ROOM_ID = SCREEN_ROOMS.ID AND CINEMAS.ID = SCREEN_ROOMS.CINEMA_ID AND SCHEDULES.SHOW_TIME_ID = SHOW_TIMES.ID";
        } else {
            queryCondition = "SCHEDULES.MOVIE_ID = MOVIES.ID AND SCHEDULES.SCREEN_ROOM_ID = SCREEN_ROOMS.ID AND CINEMAS.ID = SCREEN_ROOMS.CINEMA_ID AND SCHEDULES.SHOW_TIME_ID = SHOW_TIMES.ID";
        }
            return select("SCHEDULES.ID AS 'SCHEDULES.ID', SCHEDULES.SHOW_DATE AS 'SCHEDULES.SHOW_DATE', SHOW_TIMES.START_TIME AS 'SHOW_TIMES.START_TIME', MOVIES.TITLE AS 'MOVIES.TITLE', SCREEN_ROOMS.NAME AS 'SCREEN_ROOMS.NAME', CINEMAS.NAME AS 'CINEMAS.NAME'", from, quantity, queryCondition, sortQuery, "SCHEDULES, MOVIES, SHOW_TIMES, SCREEN_ROOMS, CINEMAS");
    }
}
