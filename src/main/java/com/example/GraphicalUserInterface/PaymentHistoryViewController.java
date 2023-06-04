package com.example.GraphicalUserInterface;

import Database.PaymentManagementProcessor;
import Utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PaymentHistoryViewController implements Initializable {
    private Main main;
    @FXML
    private GridPane paymentHistoryViewGrid;
    private ArrayList<ArrayList<String>> paymentHistoryFetcher;
    public PaymentHistoryViewController() {
        main = Main.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paymentHistoryViewGridInit();
    }
    public void paymentHistoryViewGridInit() {
        paymentHistoryFetcher = main.getProcessorManager().getPaymentManagementProcessor().getData(0, -1, String.format("USERS.ID = '%s'", main.getSignedInUser().getId()), "PAYMENTS.PAYMENT_DATE DESC").getData();

        System.out.println(paymentHistoryFetcher);
        for (int i=2; i<paymentHistoryFetcher.size();++i) {
            ArrayList<String> paymentHistoryInfo = paymentHistoryFetcher.get(i);
            RowConstraints rowConstraints = new RowConstraints();
            Label paymentDateLabel = new Label(Utils.getRowValueByColumnName(i, "PAYMENTS.PAYMENT_DATE", paymentHistoryFetcher));
            Label totalAmountLabel = new Label(Utils.getRowValueByColumnName(i, "PAYMENTS.TOTAL_AMOUNT", paymentHistoryFetcher));
            Label movieTitleLabel = new Label("test1");
            Label cinemaNameLabel = new Label("test2");
            setStyleForGridViewLabel(paymentDateLabel);
            setStyleForGridViewLabel(totalAmountLabel);
            setStyleForGridViewLabel(movieTitleLabel);
            setStyleForGridViewLabel(cinemaNameLabel);
            paymentHistoryViewGrid.addRow(i - 1, movieTitleLabel, cinemaNameLabel, paymentDateLabel, totalAmountLabel);
        }
    }
    public void setStyleForGridViewLabel(Label label) {
        label.setStyle("");
    }
}
