package BookingManager;

import com.example.GraphicalUserInterface.BookingController;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
public class BookingInfor {
    private String idMovie, nameMovie, nameCinema, date, time, screen, promotionCode, paymentMethodId, scheduleId;
    private ArrayList<String> seats;
    private ArrayList<ArrayList<String>> items;
    private int ticketPrice, comboPrice, total, discount;


    public BookingInfor(){
        this.idMovie = "";
        this.nameMovie = "";
        this.nameCinema = "";
        this.date = "";
        this.time = "";
        this.screen = "";
        this.discount = 0;
        this.promotionCode = "";
        this.paymentMethodId = "";
        this.seats = new ArrayList<>();
        this.items = new ArrayList<>();
        this.ticketPrice = 0;
        this.comboPrice = 0;
        this.total = 0;
    }
    public BookingInfor(String id, String movie, String cinema, String date, String time, String screen, String promotionCode, String scheduleId, String method, int discount, ArrayList<String> seats, ArrayList<ArrayList<String>> items, int ticketp, int combop, int totalp){
        this.idMovie = id;
        this.nameMovie = movie;
        this.nameCinema = cinema;
        this.date = date;
        this.time = time;
        this.screen = screen;
        this.promotionCode = promotionCode;
        this.paymentMethodId = method;
        this.scheduleId = scheduleId;
        this.discount = discount;
        this.seats = seats;
        this.items = items;
        this.ticketPrice = ticketp;
        this.comboPrice = combop;
        this.total = totalp;
    }
    public void setIdMovie(String id){this.idMovie = id;}
    public void addItems(ArrayList<String> item){
        this.items.add(item);
    }
    public void addSeats(String seat){
        this.seats.add(seat);
    }
    public void setNameMovie(String movie){this.nameMovie = movie;}
    public void setNameCinema(String cinema){this.nameCinema = cinema;}
    public void setDate(String date){this.date = date;}
    public void setTime(String time){this.time = time;}
    public void setScreen(String screen){this.screen = screen;}
    public void setScheduleId(String scheduleId) {this.scheduleId = scheduleId;}
    public void setDiscount(int discount){this.discount = discount;}
    public void setPromotionCode(String promotionCode){this.promotionCode = promotionCode;}
    public void setPaymentMethodId(String paymentMethodId){this.paymentMethodId = paymentMethodId;}
    public void setSeats(ArrayList<String> seats){this.seats = seats;}
    public void setItems(ArrayList<ArrayList<String>> items){this.items = items;}
    public void setTicketPrice(int price){this.ticketPrice = price;}
    public void setComboPrice(int price){this.comboPrice = price;}
    public void setTotal(int price){this.total = price;}
    public String getNameMovie(){ return this.nameMovie;}
    public String getNameCinema(){return this.nameCinema;}
    public String getDate(){return this.date;}
    public String getTime(){return this.time;}
    public String getScreen(){return this.screen;}
    public String getScheduleId(){return this.scheduleId;}
    public int getDiscount(){return this.discount;}
    public String getPromotionCode(){return this.promotionCode;}
    public String getPaymentMethodId(){return this.paymentMethodId;}
    public ArrayList<String> getSeats(){return this.seats;}
    public ArrayList<ArrayList<String>> getItems(){return this.items;}
    public int getTicketPrice(){return this.ticketPrice;}
    public int getComboPrice(){return this.comboPrice;}
    public int getTotal(){return this.total;}
    public String getIdMovie(){return this.idMovie;}


}
