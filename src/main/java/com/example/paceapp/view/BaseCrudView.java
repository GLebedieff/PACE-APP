package com.example.paceapp.view;

import com.example.paceapp.controller.CrudController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public abstract class BaseCrudView<T> {
    protected final CrudController<T> controller;
    protected final String tituloTela;
    protected VBox rootPane;
    protected TableView<T> tabela;

    public BaseCrudView(String tituloTela, CrudController<T> controller){
        this.tituloTela = tituloTela;
        this.controller = controller;
        this.rootPane = new VBox(15);
        this.rootPane.setPadding(new Insets(20));
        this.rootPane.setStyle("-fx-background-color: white;");
        this.construirInterface();
    }

    public VBox getPane() {
        atualizarTabela();
        return this.rootPane;
    }

    private void construirInterface() {
        HBox cabecalho = new HBox();
        cabecalho.setAlignment(Pos.CENTER_LEFT);
        cabecalho.setSpacing(20);

        Label titulo = new Label(tituloTela);
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2C4A7C;");

        Button btnNovo = new Button("Novo Cadastro");
        btnNovo.setStyle("-fx-background-color: #2C4A7C; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        btnNovo.setOnAction(e -> abrirFormulario(null));

        cabecalho.getChildren().addAll(titulo, btnNovo);

        //config da tabela de visualizacao
        tabela = new TableView<>();
        tabela.setPrefHeight(400);

        //carrega coluna de dados fornecidas pelas subclasses
        List<TableColumn<T, ?>> colunasDados = obterColunasDados();
        tabela.getColumns().addAll(colunasDados);

        //coluna de acoes (alterar e excluir)
        TableColumn<T, Void> colAcoes = new TableColumn<>("Ações");
        colAcoes.setPrefWidth(200);
        colAcoes.setCellFactory(param -> new TableCell<T, Void>(){

        })
    }
}
