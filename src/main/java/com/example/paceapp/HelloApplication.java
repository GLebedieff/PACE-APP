package com.example.paceapp;

import com.example.paceapp.view.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        LoginView login = new LoginView();
        stage.setTitle("Login");
        stage.setScene(login.getCena());
        stage.show();
    }
    public static void main(String[] args){
        launch();
    }
}
