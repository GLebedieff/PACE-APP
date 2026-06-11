package com.example.paceapp.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class CadastroUsuarioView {
    //atributos gerais da classe
    private VBox main;
    private final double largura = 500;
    private final double altura = 300;
    private Scene login;

    //componentes da cena
    private TextField nomeInput;
    private TextField emailInput;
    private PasswordField senhaInput;
    private TextField telefoneInput;

    public CadastroUsuarioView(){
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

        VBox cadastroBox = new VBox();
        Text cadastroTitulo = new Text("Cadastro");

        VBox nome = new VBox();
        Label nomeLabel = new Label("Nome");
        this.nomeInput = new TextField();
        nome.getChildren().addAll(nomeLabel, nomeInput);

        VBox email = new VBox();
        Label emailLabel = new Label("Email");
        this.emailInput = new TextField();
        email.getChildren().addAll(emailLabel, emailInput);

        VBox senha = new VBox();
        Label senhaLabel = new Label("Senha");
        this.senhaInput = new PasswordField();
        senha.getChildren().addAll(senhaLabel, senhaInput);

        VBox telefone = new VBox();
        Label telefoneLabel = new Label("Telefone");
        this.telefoneInput = new TextField();
        telefone.getChildren().addAll(telefoneLabel, telefoneInput);

        Button btnCadastro = new Button();

        cadastroBox.getChildren().addAll(cadastroTitulo, nome, email, senha, telefone, btnCadastro);
        cadastroBox.setStyle("-fx-background-color: brown;");
        cadastroBox.setAlignment(Pos.CENTER);
        cadastroBox.setPadding(new Insets(15));

        principal.getChildren().add(cadastroBox);
        principal.setAlignment(Pos.CENTER);
        principal.setPadding(new Insets(30));
    }

    public String getNome(){
        return nomeInput.getText();
    }

    public String getEmail(){
        return emailInput.getText();
    }

    public String getSenha(){
        return senhaInput.getText();
    }

    public String getTelefone(){
        return telefoneInput.getText();
    }
}
