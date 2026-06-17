package com.example.paceapp;

import com.example.paceapp.view.MainView;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        MainView mainView = new MainView();
        stage.setTitle("PACE");
        stage.setScene(mainView.getCena());
        stage.show();
    }
    public static void main(String[] args){
        launch();
    }
}
