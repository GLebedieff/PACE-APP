package com.example.paceapp.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class MainView {

    private BorderPane root;
    private Scene scene;
    private StackPane areaConteudo;

    public MainView() {
        this.root = new BorderPane();
        this.scene = new Scene(root, 900, 600);
    }

    public Scene getCena() {
        construirCena();
        return scene;
    }

    private void construirCena() {
        // TOPO - barra azul escuro
        HBox topo = new HBox();
        topo.setStyle("-fx-background-color: #2C4A7C; -fx-padding: 10;");
        Text titulo = new Text("PACE");
        titulo.setStyle("-fx-fill: white; -fx-font-size: 18; -fx-font-weight: bold;");
        topo.getChildren().add(titulo);

        // ÁREA DE CONTEÚDO (centro)
        areaConteudo = new StackPane();
        areaConteudo.setStyle("-fx-background-color: white;");
        Label placeholder = new Label("Selecione um item no menu");
        placeholder.setStyle("-fx-text-fill: #2C4A7C;");
        areaConteudo.getChildren().add(placeholder);

        // MENU LATERAL
        VBox menuLateral = new VBox();
        menuLateral.setStyle("-fx-background-color: #F5C842; -fx-padding: 10;");
        menuLateral.setPrefWidth(200);

        Label menuTitulo = new Label("MENU PRINCIPAL");
        menuTitulo.setStyle("-fx-text-fill: #2C4A7C; -fx-font-size: 11; -fx-padding: 0 0 10 5;");

        Button btnResponsavelLegal = criarBotaoMenu("Responsável Legal");
        Button btnProfessor = criarBotaoMenu("Professor");
        Button btnAluno = criarBotaoMenu("Aluno");
        Button btnProfissionalResponsavel = criarBotaoMenu("Profissional Responsável");
        Button btnInstituicao = criarBotaoMenu("Instituição");
        Button btnUsuario = criarBotaoMenu("Usuário");
        Button btnEmilly1 = criarBotaoMenu("Emilly 1");
        Button btnEmilly2 = criarBotaoMenu("Emilly 2");

        menuLateral.getChildren().addAll(
                menuTitulo,
                btnResponsavelLegal,
                btnProfessor,
                btnAluno,
                btnProfissionalResponsavel,
                btnInstituicao,
                btnUsuario,
                btnEmilly1,
                btnEmilly2
        );

        // AÇÕES DOS BOTÕES
        btnResponsavelLegal.setOnAction(e -> areaConteudo.getChildren().setAll(new Label("CRUD Responsável Legal")));
        btnProfessor.setOnAction(e -> areaConteudo.getChildren().setAll(new Label("CRUD Professor")));
        btnAluno.setOnAction(e -> areaConteudo.getChildren().setAll(new Label("CRUD Aluno")));
        btnProfissionalResponsavel.setOnAction(e -> areaConteudo.getChildren().setAll(new Label("CRUD Profissional Responsável")));
        btnInstituicao.setOnAction(e -> areaConteudo.getChildren().setAll(new Label("CRUD Instituição")));
        btnUsuario.setOnAction(e -> areaConteudo.getChildren().setAll(new Label("CRUD Usuário")));
        btnEmilly1.setOnAction(e -> areaConteudo.getChildren().setAll(new Label("CRUD Emilly 1")));
        btnEmilly2.setOnAction(e -> areaConteudo.getChildren().setAll(new Label("CRUD Emilly 2")));

        // MONTAGEM FINAL
        root.setTop(topo);
        root.setLeft(menuLateral);
        root.setCenter(areaConteudo);
    }

    private Button criarBotaoMenu(String texto) {
        Button btn = new Button(texto);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #2C4A7C; -fx-font-weight: bold; -fx-padding: 10 5; -fx-cursor: hand;");
        return btn;
    }
}