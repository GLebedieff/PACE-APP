package com.example.paceapp.view;

import com.example.paceapp.controller.CrudController;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
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
            private final Button btnEditar = new Button("Editar");
            private final Button btnExcluir = new Button("Excluir");
            private final HBox container = new HBox(8, btnEditar, btnExcluir);

            {
                btnEditar.setStyle("-fx-background-color: #F5C842; -fx-text-fill: #2C4A7C; -fx-font-weight: bold; -fx-cursor: hand;");
                btnExcluir.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
                container.setAlignment(Pos.CENTER);

                //funcao editar para abrir formulario
                btnEditar.setOnAction(e -> {
                   T item = getTableView().getItems().get(getIndex());
                   abrirFormulario(item);
                });

                //funcao excluir para pedir confirmacao e remover
                btnExcluir.setOnAction(e -> {
                    T item = getTableView().getItems().get(getIndex());
                    Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION, "Confirmar exclusão?", ButtonType.YES, ButtonType.NO);
                    confirmacao.showAndWait().ifPresent(resposta -> {
                        if (resposta == ButtonType.YES) {
                            excluirItem(item);
                            atualizarTabela();
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(container);
                }
            }
        });

        tabela.getColumns().add(colAcoes);
        rootPane.getChildren().addAll(cabecalho, tabela);
    }

    public void atualizarTabela() {
        ArrayList<T> lista = controller.listarTodos();
        tabela.setItems(FXCollections.observableArrayList(lista));
    }

    //janela modal para editar dados
    protected void abrirModalFormulario(String titulo, VBox layoutFormulario) {
        Stage stageModal = new Stage();
        stageModal.setTitle(titulo);
        stageModal.initModality(Modality.APPLICATION_MODAL);

        Scene cena = new Scene(layoutFormulario, 400, 320);
        stageModal.setScene(cena);
        stageModal.showAndWait();
        atualizarTabela();
    }

    protected void mostrarAlerta(Alert.AlertType tipo, String titulo, String conteudo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(conteudo);
        alerta.showAndWait();
    }

    //ganchos para as views filhas
    protected abstract List<TableColumn<T, ?>> obterColunasDados();
    protected abstract void abrirFormulario(T itemExistente);
    protected abstract void excluirItem(T item);
}
