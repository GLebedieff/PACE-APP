package com.example.paceapp.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LoginView {
    private VBox main;
    private final double largura = 500;
    private final double altura = 300;
    private Scene login;

    public LoginView(){
        this.main = new VBox();
        this.login = new Scene(main, largura, altura);
    }

    public Scene getCena(){
        this.construirCena();
        return login;
    }

    public void construirCena(){
        HBox cabecalho = new HBox();
        HBox principal = new HBox();
        main.getChildren().addAll(cabecalho, principal);

        Text cabecalhoTitulo = new Text("PACE");

        HBox redirect = new HBox();
        Button redirectLogin = new Button();
        Button redirectCadastro = new Button();
        redirect.getChildren().addAll(redirectLogin, redirectCadastro);

        cabecalho.getChildren().addAll(cabecalhoTitulo, redirect);
        cabecalho.setStyle("" +
                "-fx-background-color: blue;" +
                "");

        VBox loginBox = new VBox();
        Text loginTitulo = new Text("Login");

        VBox email = new VBox();
        Label emailLabel = new Label("Email");
        TextArea emailInput = new TextArea();
        email.getChildren().addAll(emailLabel, emailInput);

        VBox senha = new VBox();
        Label senhaLabel = new Label("Senha");
        TextArea senhaInput = new TextArea();
        senha.getChildren().addAll(senhaLabel, senhaInput);

        Button btnLogin = new Button();

        loginBox.getChildren().addAll(loginTitulo, email, senha, btnLogin);
        loginBox.setStyle("-fx-background-color: brown;");
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(15));

        principal.getChildren().add(loginBox);
        principal.setAlignment(Pos.CENTER);
        principal.setPadding(new Insets(30));

    }
}
