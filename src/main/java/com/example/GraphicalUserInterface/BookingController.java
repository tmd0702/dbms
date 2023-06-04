package com.example.GraphicalUserInterface;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.skin.ScrollBarSkin;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.text.Font;
import Database.BookingProcessor;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.animation.*;

import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.controlsfx.glyphfont.FontAwesome;
import UserManager.User;
import java.util.*;
import Utils.*;



public class BookingController {
    private Main main;
    //page1
    @FXML
    private  AnchorPane pane1;
    @FXML
    private ImageView image1;

    private ArrayList<Button> DateButton = new ArrayList<Button>();
    private ArrayList<Boolean> isDateActive = new ArrayList<Boolean>();
    private ArrayList<Button> CinemaButton = new ArrayList<Button>();
    private ArrayList<Boolean> isCinemaActive = new ArrayList<Boolean>();
    private ArrayList<Button> TimeButton = new ArrayList<Button>();
    private ArrayList<Boolean> isTimeActive = new ArrayList<Boolean>();
    @FXML
    private Button DateBtn1, DateBtn2, DateBtn3, DateBtn4, DateBtn5, DateBtn6, DateBtn7;
    @FXML
    private HBox cinemaSection;
    @FXML
    private ScrollPane cinemaSectionScrollPane = new ScrollPane();
    @FXML
    private HBox timeSection;
    @FXML
    private ScrollPane timeSlotSecionScrollPane = new ScrollPane();
    @FXML
    private FontAwesomeIconView cineScrollLeftBtn, cineScrollRightBtn, timeSlotScrollLeftBtn, timeSlotScrollRightBtn;
    @FXML
    private Label announceTime, announceCinema;
    private ArrayList<ArrayList<String>> cinemaInfor = new ArrayList<ArrayList<String>>();
    private ArrayList<String> cinemaName;
    private ArrayList<ArrayList<String>> timeInfor = new ArrayList<ArrayList<String>>();
    private ArrayList<String> timeSlotList;
    private LocalDateTime now = LocalDateTime.now();
    // page2
    @FXML
    private StackPane stackpane;
    @FXML
    private  AnchorPane pane2;
    @FXML
    private ImageView image2;
    @FXML
    private GridPane SeatGrid;
    private ArrayList<String> SeatId = new ArrayList<>();
    private ArrayList<String> SeatName = new ArrayList<String>();
    @FXML
    private Label nameMovieBooking, nameCinema, showTime, showDate, seatID, screenName, price, combo, total;
    @FXML
    private AnchorPane ticketInfor;
    // page3
    @FXML
    private AnchorPane pane3;
    @FXML
    private ImageView image3;
    @FXML
    private Label countdownLabel;
    private Timeline timeline = new Timeline();
    private AnchorPane changeNumberItemAnchorPane = new AnchorPane();
    @FXML
    private VBox itemControllerContainer;
    private ArrayList<AnchorPane> listPane = new ArrayList<AnchorPane>();

    private ArrayList<Integer> numberOfitems= new ArrayList<Integer>();
    //page4
    @FXML
    private AnchorPane pane4;
    @FXML
    private ImageView image4;
    @FXML
    private Label nameMovieBookingFinal, nameCinemaFinal, showTimeFinal, seatIDFinal, screenNameFinal, priceFinal, comboFinal, totalFinal, discount;
    @FXML
    private TextField promotionField;
    @FXML
    private Label countdownLabel1;
    @FXML
    private VBox paymentMethodVbox;
    private ArrayList<RadioButton> radioButtonList = new ArrayList<RadioButton>();
    private BookingProcessor bookingProcessor;

    @FXML
     public void initialize() throws SQLException {
//        bookingProcessor.getConnector().setAutoCommit(false);
        ConstructPane();
        //page1
        ConstructDateButton();
        FormartDateButton();
        scrollBtnInit();
        //page3
        //page4

    }
    public void itemsControllerInit(ArrayList<ArrayList<String>> itemFetcher) {
//        ArrayList<ArrayList<String>> itemFetcher = bookingProcessor.getItems();
        ArrayList<String> itemNames = Utils.getDataValuesByColumnName(itemFetcher, "ITEMS.NAME");
        ArrayList<String> priceItems = Utils.getDataValuesByColumnName(itemFetcher, "PRICES.PRICE");
        for (String itemName : itemNames) {
            Label itemLabel = new Label(itemName);
            itemLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px");
            itemLabel.setPrefSize(120, Region.USE_COMPUTED_SIZE);
            itemLabel.setTextAlignment(TextAlignment.RIGHT);
            itemLabel.setWrapText(true);
            HBox itemLabelContainer = new HBox(itemLabel);
            itemLabelContainer.setAlignment(Pos.CENTER);
            itemLabelContainer.setStyle("-fx-background-color: #2B2B2B; -fx-background-radius: 20px");
            itemLabelContainer.setPadding(new Insets(5, 5, 5, 5));
            itemLabelContainer.setPrefWidth(150);
            Label quantityLabel = new Label("0");
            quantityLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
            Button subtractQuantityBtn = new Button("-"), addQuantityBtn = new Button("+");
            subtractQuantityBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    changeNumberOfItems(quantityLabel, subtractQuantityBtn, itemNames.indexOf(itemName), priceItems);
                    calculateTotal();
                }
            });
            addQuantityBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    changeNumberOfItems(quantityLabel, addQuantityBtn, itemNames.indexOf(itemName), priceItems);
                    calculateTotal();
                }
            });
            HBox itemController = new HBox(subtractQuantityBtn, quantityLabel, addQuantityBtn);
            itemController.setPadding(new Insets(10, 10, 10, 10));
            subtractQuantityBtn.setStyle("-fx-background-color: #7C7C7C; -fx-text-fill: white; -fx-font-weight: bold;-fx-background-radius: 10px;");
            addQuantityBtn.setStyle("-fx-background-color: #7C7C7C; -fx-text-fill: white; -fx-font-weight: bold;-fx-background-radius: 10px;");
            subtractQuantityBtn.setPrefWidth(30);
            addQuantityBtn.setPrefWidth(30);
            itemController.setStyle("-fx-background-color: #2B2B2B; -fx-background-radius: 20px;");
            itemController.setPrefWidth(150);
            itemController.setSpacing(20);
            itemController.setAlignment(Pos.CENTER);
            HBox itemControllerInstance = new HBox(itemLabelContainer, itemController);
            itemControllerInstance.setAlignment(Pos.TOP_LEFT);
            itemControllerInstance.setSpacing(100);
            itemControllerContainer.setSpacing(20);
            itemControllerContainer.setAlignment(Pos.CENTER_LEFT);
            itemControllerContainer.getChildren().add(itemControllerInstance);
        }
    }
    public void getIdItems(ArrayList<ArrayList<String>> itemFetcher, int index){
        ArrayList<String> itemPicked = new ArrayList<>();
        itemPicked.add(getIdFromIndex(itemFetcher, index));
        itemPicked.add(Integer.toString(numberOfitems.get(index)));
        bookingProcessor.getBookingInfor().addItems(itemPicked);
    }
    public BookingController(){
        this.main = Main.getInstance();
        this.bookingProcessor = main.getProcessorManager().getBookingProcessor();
    }

    public void ConstructItem(){
        ArrayList<ArrayList<String>> itemFetcher = bookingProcessor.getItems();
        ArrayList<String> itemNames = Utils.getDataValuesByColumnName(itemFetcher, "ITEMS.NAME");
        for(int i = 0; i < itemNames.size(); i++)
        numberOfitems.add(0);
        itemsControllerInit(itemFetcher);
    }
    public void ConstructPaymentMethodButton(){
        ArrayList<ArrayList<String>> paymentMethodData = bookingProcessor.getPaymentMethod();
        ArrayList<String> namePaymentMethod = Utils.getDataValuesByColumnName(paymentMethodData, "NAME");
        System.out.println(namePaymentMethod);
        ToggleGroup toggleGroup = new ToggleGroup();
        paymentMethodVbox.setSpacing(20);
        HBox hbox = new HBox();
        for(int i = 0; i < namePaymentMethod.size(); i++){
            if(i%2==0){
                hbox = new HBox();
            }
            hbox.setSpacing(100);
            RadioButton radioButton = new RadioButton(namePaymentMethod.get(i));
            radioButton.setToggleGroup(toggleGroup);
            radioButton.setTextFill(Color.WHITE);
            radioButton.setOnAction(event ->{
                bookingProcessor.getBookingInfor().setPaymentMethodId(getIdFromIndex(paymentMethodData, radioButtonList.indexOf(radioButton)));
                System.out.println(bookingProcessor.getBookingInfor().getPaymentMethodId());
            });
            hbox.getChildren().addAll(radioButton);
            radioButtonList.add(radioButton);
            if(i%2 != 0 || i == namePaymentMethod.size()-1){
                paymentMethodVbox.getChildren().add(hbox);
            }
        }
    }
    public void ConstructPane(){
        bookingProcessor.getBookingInfor().setIdMovie(main.getMovieOnBooking().getId());
        bookingProcessor.getBookingInfor().setNameMovie(main.getMovieOnBooking().getTitle());
//        changeNumberItemAnchorPane.getChildren().add(new Button());
        changeNumberItemAnchorPane.setStyle("-fx-background: #FFFFFF; -fx-background-radius: 19");
        changeNumberItemAnchorPane.setPrefSize(191, 46);
        pane3.getChildren().add(changeNumberItemAnchorPane);
        listPane.add(pane1);
        listPane.add(pane2);
        listPane.add(pane3);
        listPane.add(pane4);
        image1.setImage(main.getMovieOnBooking().getPosterImage());
        image2.setImage(main.getMovieOnBooking().getPosterImage());
        image3.setImage(main.getMovieOnBooking().getPosterImage());
        image4.setImage(main.getMovieOnBooking().getPosterImage());
    }

     public void ConstructTicketInfor() {
         nameMovieBooking.setText(main.getMovieOnBooking().getTitle());
         setScreenTicketInfor();
         price.setText("0");
         combo.setText("0");
         total.setText("0");
         seatID.setText("");
//         countdownLabel.setText("15:00");
//         setCountDown(countdownLabel);

     }
     public void ConstructTicketInforFinal(){
        nameMovieBookingFinal.setText(nameMovieBooking.getText());
        nameCinemaFinal.setText(nameCinema.getText());
        screenNameFinal.setText(screenName.getText());
        showTimeFinal.setText(showTime.getText() + ", " + showDate.getText());
        seatIDFinal.setText(seatID.getText());
        priceFinal.setText(price.getText());
        comboFinal.setText(combo.getText());
        totalFinal.setText(total.getText());
        discount.setText("0");
         ConstructPaymentMethodButton();

//        countdownLabel1.setText(countdownLabel.getText());
//        setCountDown(countdownLabel1);

     }
     @FXML
     public void applyPromotionOnClick() throws IOException{
        System.out.println("apply");
        bookingProcessor.getBookingInfor().setPromotionCode(promotionField.getText());
        handleApplyPromotionRequest();
     }
     public void handleApplyPromotionRequest(){
        Response response = main.getProcessorManager().getPromotionManagementProcessor().getData(0,-1, String.format("PROMOTIONS.ID = '%s'", bookingProcessor.getBookingInfor().getPromotionCode()),"");
        System.out.println(response.getData());

        if(Utils.getDataValuesByColumnName(response.getData(), "PROMOTIONS.ID").size() > 0){
            Float discountNumber = Float.parseFloat(Utils.getDataValuesByColumnName(response.getData(), "PROMOTIONS.DISCOUNT").get(0));
            calculateDiscount(discountNumber);
        }else{
            Dialog<String> dialog = new Dialog<String>();
            //Setting the title
            dialog.setTitle("Failed");
            ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            dialog.setContentText("wrong promotion code");
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.showAndWait();
        }
     }
     public void ConstructDateButton(){
         DateButton.add(DateBtn1);
         DateButton.add(DateBtn2);
         DateButton.add(DateBtn3);
         DateButton.add(DateBtn4);
         DateButton.add(DateBtn5);
         DateButton.add(DateBtn6);
         DateButton.add(DateBtn7);
         ConstructActiveList(DateButton, isDateActive);
     }
     public void ConstructCinemaButton(){
        CinemaButton = new ArrayList<Button>();
        cinemaInfor = new ArrayList<ArrayList<String>>();
        cinemaSection = new HBox();
        cinemaSection.setSpacing(5);
        cinemaSectionScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        cinemaInfor = bookingProcessor.getTheaterList();
        System.out.println(cinemaInfor);
        cinemaName = Utils.getDataValuesByColumnName(cinemaInfor, "NAME");
        for(String cinema : cinemaName){
            Button button = new Button();
            button.setWrapText(true);
            button.setTextAlignment(TextAlignment.CENTER);
            //button.setPrefSize(134,48);
            button.setMinSize(134, 48);
            button.setStyle("-fx-background-color: #2B2B2B");
            button.setTextFill(Color.WHITE);
            button.setText(cinema);
            button.setOnAction(e->handleDateButtonAction(e));
            CinemaButton.add(button);
            cinemaSection.getChildren().add(button);
        }
        cinemaSectionScrollPane.setContent(cinemaSection);
        ConstructActiveList(CinemaButton, isCinemaActive);
     }
    public String getIdFromIndex(ArrayList<ArrayList<String>> infor, int index ) {
        String id = infor.get(2 + index).get(0);
        return id;
    }
     public void ConstructTimeButton(){
        TimeButton = new ArrayList<Button>();
        timeInfor = new ArrayList<ArrayList<String>>();
        timeSection = new HBox();
        timeSection.setSpacing(5);
        timeSlotSecionScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        timeInfor = bookingProcessor.getTimeSlotList();
         System.out.println(timeInfor);
         timeSlotList = Utils.getDataValuesByColumnName(timeInfor, "TIME_SLOT");
         LocalDateTime now = LocalDateTime.now();
         DateTimeFormatter todayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
         String today = now.format(todayFormatter);
         DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
         String nowTime = now.format(timeFormatter);
        for(String timeSlot : timeSlotList){
            Button button = new Button();
            button.setWrapText(true);
            button.setTextAlignment(TextAlignment.CENTER);
            //button.setPrefSize(134,48);
            button.setMinSize(75, 48);
            button.setStyle("-fx-background-color: #2B2B2B");
            button.setTextFill(Color.WHITE);
            button.setText(timeSlot);
            button.setOnAction(e->handleDateButtonAction(e));
            TimeButton.add(button);
            if(bookingProcessor.getBookingInfor().getDate().equals(today) && timeSlot.compareTo(nowTime) <= 0)
                continue;
            timeSection.getChildren().add(button);
        }
        timeSlotSecionScrollPane.setContent(timeSection);
        ConstructActiveList(TimeButton, isTimeActive);
     }
     public void ConstructSeatGrid(){
        SeatGrid.setPrefSize(507, 298);
        SeatGrid.setLayoutX(385);
        SeatGrid.setLayoutY(234);
        SeatGrid.setHgap(10);
        SeatGrid.setVgap(10);
        if(!pane2.getChildren().contains(SeatGrid))
            pane2.getChildren().add(SeatGrid);
        ArrayList<ArrayList<String>> SeatInfor = bookingProcessor.getSeat();
        ArrayList<String> SeatList = Utils.getDataValuesByColumnName(SeatInfor, "NAME");
        System.out.println(SeatList);
        System.out.println(SeatInfor.size());
        for(int i = 0; i < SeatList.size(); i++){
            String s = SeatList.get(i);
            int rowIndex = s.charAt(0) - 'A';
            int columnIndex = Integer.parseInt(s.substring(1));
            Button button = new Button(s);
            button.setTextFill(Color.BLACK);
            button.setPrefSize(25,34);
            button.setFont(Font.font(7.5));
            button.setWrapText(true);
            boolean occupied = bookingProcessor.checkStatusSeat(getIdFromIndex(bookingProcessor.getSeat(), i), bookingProcessor.getBookingInfor().getScheduleId());
            System.out.println(occupied);
            if(!occupied){
                button.setStyle("-fx-background-color: #393939;");
//                button.setDisable(true);
                SeatGrid.add(button, columnIndex, rowIndex);
                continue;
            }else{
                button.setStyle("-fx-background-color: #A4A4A4");
            }
            SeatGrid.add(button, columnIndex, rowIndex);
            button.setOnAction(event->{
                if(SeatName.size() < 12) {
                    if (button.getStyle() == "-fx-background-color: #A4A4A4") {
                        button.setStyle("-fx-background-color: #8D090D");
                        SeatName.add(button.getText());
                        SeatId.add(getIdFromIndex(SeatInfor, SeatList.indexOf(s)));
                        displaySeatInfor();
                    } else {
                        button.setStyle("-fx-background-color: #A4A4A4");
                        SeatName.remove(button.getText());
                        SeatId.remove(getIdFromIndex(SeatInfor, SeatList.indexOf(s)));
                        displaySeatInfor();
                    }
                    SeatGrid.requestFocus();
                }
                else if(button.getStyle() == "-fx-background-color: #8D090D"){
                    button.setStyle("-fx-background-color: #A4A4A4");
                    SeatName.remove(button.getText());
                    SeatId.remove(getIdFromIndex(SeatInfor, SeatList.indexOf(s)));
                    displaySeatInfor();
                } else{
                    Dialog<String> dialog = new Dialog<String>();
                    //Setting the title
                    dialog.setTitle("Failed");
                    ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                    dialog.setContentText("wrong promotion code");
                    dialog.getDialogPane().getButtonTypes().add(type);
                    dialog.showAndWait();
                }
            });
        }

     }
     public void displayAnnounce(){
        if(showDate.getText() == ""){
            InvisibleScrollpane(cinemaSection, cineScrollLeftBtn, cineScrollRightBtn);
            InvisibleScrollpane(timeSection, timeSlotScrollLeftBtn, timeSlotScrollRightBtn);
            announceCinema.setVisible(true);
            announceTime.setVisible(true);
        }else {
            VisibleScrollpane(cinemaSection, cineScrollLeftBtn, cineScrollRightBtn);
            if (nameCinema.getText() == "") {
                InvisibleScrollpane(timeSection, timeSlotScrollLeftBtn, timeSlotScrollRightBtn);
                announceCinema.setVisible(false);
                announceTime.setVisible(true);
            } else {
                VisibleScrollpane(timeSection, timeSlotScrollLeftBtn, timeSlotScrollRightBtn);
                announceCinema.setVisible(false);
                announceTime.setVisible(false);

            }
        }
     }
     public void InvisibleScrollpane(HBox hbox, FontAwesomeIconView left, FontAwesomeIconView right){
        hbox.setVisible(false);
        left.setVisible(false);
        right.setVisible(false);
     }
    public void VisibleScrollpane(HBox hbox, FontAwesomeIconView left, FontAwesomeIconView right){
        hbox.setVisible(true);
        left.setVisible(true);
        right.setVisible(true);
    }
     public void getColumnRowOfRoom(ArrayList<String> list, int r, int c){
         char maxChar = 'A';
         int maxNum = 1;

         for (String s : list) {
             char t = s.charAt(0);
             int num = Integer.parseInt(s.substring(1));

             if (t >= maxChar && num > maxNum) {
                 maxChar = t;
                 maxNum = num;
             }
         }
         r = maxChar;
         c = maxNum;

     }
//     public void handleComboButton(ActionEvent event){
//            Button b = (Button) event.getSource();
//            if(CaraButton.contains(b))
//                changeNumberOfItems(numberOfCara, b, CaraButton);
//            else if(CheeseButton.contains(b))
//                changeNumberOfItems(numberOfCheese, b, CheeseButton);
//            else if(CokeButton.contains(b))
//                changeNumberOfItems(numberOfCoke, b, CokeButton);
//            else if (ComboButton.contains(b))
//                changeNumberOfItems(numberOfCombo, b, ComboButton);
//            calculateTotal();
//
//     }
     public void changeNumberOfItems(Label label, Button b, int index, ArrayList<String> priceList){
        int i = Integer.parseInt(label.getText());
            if(b.getText().equals("-")){
                if(i > 0)
                    i--;
            }
            else i++;
        label.setText(Integer.toString(i));
        getPriceCombo(index, i);
        calculateCombo(priceList);
     }
     public void calculateCombo(ArrayList<String> priceList){
         int p = 0;
         for(int j = 0; j < priceList.size();j++){
             p += Integer.parseInt(priceList.get(j)) * numberOfitems.get(j);
         }
         combo.setText(Integer.toString(p));
         comboFinal.setText(combo.getText());
     }
     public void getPriceCombo(int index, int i){
        numberOfitems.set(index, i);
     }

     public void displaySeatInfor(){
         Collections.sort(SeatName);
             String listseat = "";
             if(SeatName.size() == 0)
                 listseat = "";
             else {
                 for(int i = 0; i < SeatName.size(); i++){
                     if(i > 0) {
                         listseat += ", ";
                     }
                     listseat += SeatName.get(i);
                 }
             }
             seatID.setText(listseat);

             calculatePrice();
             calculateTotal();

     }
     public void calculatePrice(){
         int Price = SeatName.size() * 70000;
         price.setText(Integer.toString(Price));
         //priceFinal.setText(price.getText());
     }
     public void calculateTotal(){
         total.setText(Integer.toString(Integer.parseInt(price.getText()) + Integer.parseInt(combo.getText())));
         totalFinal.setText(total.getText());
     }
    public void calculateDiscount(Float discountNumber){
        discount.setText("-" + (int)(Float.parseFloat(total.getText()) * discountNumber));
        bookingProcessor.getBookingInfor().setDiscount(Integer.parseInt(discount.getText()));
        totalFinal.setText(Integer.toString(Integer.parseInt(total.getText()) + bookingProcessor.getBookingInfor().getDiscount()));
    }
     public void ConstructActiveList(ArrayList<Button> ListButton, ArrayList<Boolean> ListActive){
         for (int i = 0; i < ListButton.size(); i++){
             ListActive.add(false);
         }
     }

    public void handleDateButtonAction(ActionEvent event) {
        Button button = (Button) event.getSource();
        if(DateButton.contains(button)) {
            setClick(DateButton, button, isDateActive, showDate);
        }
        else if (CinemaButton.contains(button)){
            setClick(CinemaButton, button, isCinemaActive, nameCinema);
        }
        else
            setClick(TimeButton, button, isTimeActive, showTime);
    }

    public void setClick(ArrayList<Button> ListButton, Button button, ArrayList<Boolean> ListActive, Label label){
        int i;
        for(i = 0; i < ListButton.size(); i++){
            if(ListButton.get(i) == button)
                break;
        }
        if(!ListActive.get(i)){
            ListButton.get(i).setStyle("-fx-background-color: #760404");
            setDefautlColor(i, ListButton);
            ListActive.set(i, true);
            setActiveButton(i, ListActive);
            if(checkDate(button)) {
                int j;
                for(j = 0; j < DateButton.size(); j++)
                    if(button == DateButton.get(j))
                        break;
                LocalDateTime now = LocalDateTime.now().plusDays(i);
                DateTimeFormatter ShowDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                label.setText(now.format(ShowDateFormat));
                DateTimeFormatter showDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                bookingProcessor.getBookingInfor().setDate(now.format(showDateFormat));
                System.out.println(bookingProcessor.getBookingInfor().getDate());
                ConstructCinemaButton();
                nameCinema.setText("");
                showTime.setText("");
            }
            else
                label.setText(button.getText());
        }else{
            ListActive.set(i, false);
            ListButton.get(i).setStyle("-fx-background-color: #2B2B2B");
            label.setText("");
        }
        if(CinemaButton.contains(button)){
            System.out.println(getIdFromIndex(cinemaInfor, CinemaButton.indexOf(button)));
            bookingProcessor.getBookingInfor().setNameCinema(getIdFromIndex(cinemaInfor, CinemaButton.indexOf(button)));
            ConstructTimeButton();
        }
        if(TimeButton.contains(button)){

            System.out.println(getIdFromIndex(timeInfor, TimeButton.indexOf(button)));
            bookingProcessor.getBookingInfor().setScheduleId(Utils.getDataValuesByColumnName(timeInfor, "SCHEDULE_ID").get(TimeButton.indexOf(button)));
            System.out.println("Schedule ID: " + bookingProcessor.getBookingInfor().getScheduleId());
            bookingProcessor.getBookingInfor().setTime(getIdFromIndex(timeInfor, TimeButton.indexOf(button)));
            ConstructTicketInfor();
            if(!(button.getStyle() == "-fx-background-color: #8D090D")) {
                pane2.getChildren().remove(SeatGrid);
                SeatGrid = new GridPane();
                bookingProcessor.getBookingInfor().setSeats(new ArrayList<String>());
                SeatName = new ArrayList<String>();
            }
            ConstructSeatGrid();

        }
        displayAnnounce();
    }

    public boolean checkDate(Button b){
        if(!DateButton.contains(b))
            return false;
        return true;
    }

    public void setActiveButton(int i, ArrayList<Boolean> ListActive){
        for(int j = 0; j < ListActive.size(); j++){
            if(i == j)
                continue;
            ListActive.set(j, false);
        }
    }

    public void setDefautlColor(int i, ArrayList<Button> ListButton){
        for(int j = 0; j < ListButton.size(); j++){
            if(j == i)
                continue;
            ListButton.get(j).setStyle("-fx-background-color: #2B2B2B");
        }
    }

    public void setTime(LocalDateTime now, Button b){
        DateTimeFormatter dateFormater = DateTimeFormatter.ofPattern("dd/MM");
        DateTimeFormatter dayOfWeekformater = DateTimeFormatter.ofPattern("EEEE", Locale.US);
        String Formateddate = now.format(dateFormater);
        String Formateddyaofweek  =now.format(dayOfWeekformater);
        b.setText(Formateddyaofweek + " " + Formateddate);
    }

    public void FormartDateButton(){
            LocalDateTime subnow;
            for (int i = 0; i < DateButton.size(); i++) {
                subnow = now.plusDays(i);
                setTime(subnow, DateButton.get(i));
            }
    }

    public void handleSwitchToPageBefore(ActionEvent event){

        Button b = (Button) event.getSource();
        int i;
        for( i = 0; i < listPane.size(); i++) {
            if (listPane.get(i).getChildren().contains(b))
            {
                stackpane.getChildren().remove(listPane.get(i));
                if(i == 2){
                    pane2.getChildren().addAll(ticketInfor);
                    //setScreenTicketInfor();
                }
                break;
            }

        }
        if(!stackpane.getChildren().contains(listPane.get(i-1))) {
            stackpane.getChildren().addAll(listPane.get(i-1));

        }

    }
    public void setScreenTicketInfor(){
        ticketInfor.setStyle("-fx-background-color: #000000cd");
        this.screenName.setText(bookingProcessor.getScreen());
    }
    public  void handleSwitchPageAfter(ActionEvent event){
        Button b = (Button) event.getSource();
        int i;
        for( i = 0; i < listPane.size(); i++) {
            if(listPane.get(i).getChildren().contains(b)){
                if(i == 0 && (!cinemaSection.isVisible() || (nameCinema.getText() == "" || showTime.getText() == "" || showDate.getText() == ""))){
                    Dialog<String> dialog = new Dialog<String>();
                    //Setting the title
                    dialog.setTitle("Failed");
                    ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                    dialog.setContentText("Please choose one date to show");
                    dialog.getDialogPane().getButtonTypes().add(type);
                    dialog.showAndWait();
                    return;
                }
                if(i == 0){
                    countdownLabel.setText("15:00");
                    setCountDown(countdownLabel);
                }
                if(i == 1){
                    ConstructItem();
                }
                if(i == 1 && seatID.getText() == ""){
                    Dialog<String> dialog = new Dialog<String>();
                    //Setting the title
                    dialog.setTitle("Failed");
                    ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                    dialog.setContentText("wrong promotion code");
                    dialog.getDialogPane().getButtonTypes().add(type);
                    dialog.showAndWait();
                    return;
                }
                if(i == 2){
                    ArrayList<ArrayList<String>> itemFetcher = bookingProcessor.getItems();
                    for(int j = 0; j <  numberOfitems.size(); j++ ){
                        if(numberOfitems.get(j) != 0) {
                            getIdItems(itemFetcher, j);
                        }
                    }
                    System.out.println(bookingProcessor.getBookingInfor().getItems());
                    ConstructTicketInforFinal();
                }
                    stackpane.getChildren().remove(listPane.get(i));
                    if (i == 1) {
                        pane3.getChildren().addAll(ticketInfor);
                        //setScreenTicketInfor();
                    }
                    break;

            }

        }

        if(!stackpane.getChildren().contains(listPane.get(i+1))) {
            stackpane.getChildren().addAll(listPane.get(i + 1));
        }

    }
    public void setCountDown(Label countDown){
        if(timeline.getStatus() == Animation.Status.RUNNING)
            timeline.stop();
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE); // Chạy vô hạn
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1), event -> {
                    // Giảm giây đi 1 giây
                    int secondsLeft = Integer.parseInt(countDown.getText().substring(3));
                    secondsLeft--;
                    if (secondsLeft < 0) {
                        // Nếu hết giờ, reset về 59 giây
                        secondsLeft = 59;
                        int minutesLeft = Integer.parseInt(countDown.getText().substring(0, 2));
                        minutesLeft--;
                        if (minutesLeft < 0) {
                            minutesLeft = 0;
                            secondsLeft = 0;
                        }
                        countDown.setText(String.format("%02d:%02d", minutesLeft, secondsLeft));
                    } else {
                        // Cập nhật đồng hồ đếm ngược
                        int minutesLeft = Integer.parseInt(countDown.getText().substring(0, 2));
                        countDown.setText(String.format("%02d:%02d", minutesLeft, secondsLeft));
                    }
                    if(Integer.parseInt(countDown.getText().substring(0, 2)) == 0 && Integer.parseInt(countDown.getText().substring(3)) == 0){
                        System. out.println("Het gio");
                        timeline.stop();
                        Dialog<String> dialog = new Dialog<String>();
                        //Setting the title
                        dialog.setTitle("Failed");
                        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                        dialog.setContentText("wrong promotion code");
                        dialog.getDialogPane().getButtonTypes().add(type);
                        dialog.show();
                        try {
                            main.changeScene("booking-form.fxml");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    countdownLabel1.setText(countdownLabel.getText());
                })
        );
        timeline.play();
    }
    public void scrollBtnInit() {
        scrollBtnChangeStyleOnHover(cineScrollLeftBtn);
        scrollBtnChangeStyleOnHover(cineScrollRightBtn);
        scrollBtnChangeStyleOnHover(timeSlotScrollLeftBtn);
        scrollBtnChangeStyleOnHover(timeSlotScrollRightBtn);
    }
    public void scrollAnimation(DoubleProperty property, double seconds, double targetHvalue) {
        Animation animation = new Timeline(
                new KeyFrame(Duration.seconds(seconds),
                        new KeyValue(property, targetHvalue)));
        animation.play();
    }
    public ScrollPane getScrollPane(FontAwesomeIconView button){
        if(button.getId().contains("cine")){
            return cinemaSectionScrollPane;
        }
        if(button.getId().contains("time")){
            return timeSlotSecionScrollPane;
        }
        return null;
    }
    @FXML
    public void scrollLeftBtnOnClick(MouseEvent event) {
        Button button = (Button)(cinemaSection.getChildren().get(0));
        ScrollPane scrollPane = getScrollPane((FontAwesomeIconView) event.getSource());
        int n = 0;
        if(scrollPane == cinemaSectionScrollPane){
            n = 4;
        }else{
            n = 7;
        }
        HBox hbox = (HBox)scrollPane.getContent();
        scrollAnimation(scrollPane.hvalueProperty(), 0.5, scrollPane.getHvalue() - (((button.getWidth() + hbox.getSpacing()) * n )/ hbox.getWidth() + 0.007)) ;
    }
    @FXML
    public void scrollRightBtnOnClick(MouseEvent event) {
        Button button = (Button)(cinemaSection.getChildren().get(0));
        ScrollPane scrollPane = getScrollPane((FontAwesomeIconView) event.getSource());
        int n = 0;
        if(scrollPane == cinemaSectionScrollPane){
            n = 4;
        }else{
            n = 7;
        }
        HBox hbox = (HBox)scrollPane.getContent();
        scrollAnimation(scrollPane.hvalueProperty(), 0.5, scrollPane.getHvalue() + (((button.getWidth() + hbox.getSpacing()) * n )/ hbox.getWidth() + 0.007));
    }
    public void scrollBtnChangeStyleOnHover(FontAwesomeIconView btn) {
        Animation fadeInAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0.3),
                        new KeyValue(btn.opacityProperty(), 0.8)));
        Animation fadeOutAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0.3),
                        new KeyValue(btn.opacityProperty(), 0.3)));
        btn.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                fadeInAnimation.play();
                btn.setOpacity(0.5);
            }
        });
        btn.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                fadeOutAnimation.play();
                btn.setOpacity(0.2);
            }
        });
    }
    public void onButtonClicked(){

    }
    @FXML
    public void onPurchaseButtonClicked() throws SQLException {
        if(bookingProcessor.getBookingInfor().getPaymentMethodId() == ""){
            Dialog<String> dialog = new Dialog<String>();
            //Setting the title
            dialog.setTitle("Failed");
            ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            dialog.setContentText("Haven't chosen payment method yet");
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.showAndWait();
        }
        else {
            bookingProcessor.getBookingInfor().setTotal(Integer.parseInt(totalFinal.getText()));
            bookingProcessor.getBookingInfor().setTicketPrice(Integer.parseInt(priceFinal.getText()));
            bookingProcessor.getBookingInfor().setComboPrice(Integer.parseInt(comboFinal.getText()));
            bookingProcessor.getBookingInfor().setSeats(SeatId);
            if(bookingProcessor.storePaymentInfor()){
                Dialog<String> dialog = new Dialog<String>();
                //Setting the title
                dialog.setTitle("Successful");
                ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                dialog.setContentText("Payment successful");
                dialog.getDialogPane().getButtonTypes().add(type);
                dialog.show();
                try {
                    main.changeScene("main-outline.fxml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else{
                Dialog<String> dialog = new Dialog<String>();
                //Setting the title
                dialog.setTitle("Failed");
                ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                dialog.setContentText("Payment failed");
                dialog.getDialogPane().getButtonTypes().add(type);
                dialog.show();
                try {
                    main.changeScene("booking-form.fxml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
//                bookingProcessor.getConnector().setAutoCommit(true);
            }
        }
    }
}
