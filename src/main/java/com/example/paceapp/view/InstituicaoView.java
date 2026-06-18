package com.example.paceapp.view;

import com.example.paceapp.model.ArquivoInstituicao;
import com.example.paceapp.model.Instituicao;
import com.example.paceapp.model.ValidacaoException;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class InstituicaoView {

    private final ArquivoInstituicao arquivo = new ArquivoInstituicao();
    private VBox rootPane;
    private TableView<Instituicao> tabela;

    public InstituicaoView() {
        this.rootPane = new VBox(15);
        this.rootPane.setPadding(new Insets(20));
        this.rootPane.setStyle("-fx-background-color: white;");
        construirInterface();
    }

    public VBox getPane() {
        atualizarTabela();
        return rootPane;
    }

    private void construirInterface() {
        HBox cabecalho = new HBox();
        cabecalho.setAlignment(Pos.CENTER_LEFT);
        cabecalho.setSpacing(20);

        Label titulo = new Label("Gerenciamento de Instituições");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2C4A7C;");

        Button btnNovo = new Button("Novo Cadastro");
        btnNovo.setStyle("-fx-background-color: #2C4A7C; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        btnNovo.setOnAction(e -> abrirFormulario(null));
        cabecalho.getChildren().addAll(titulo, btnNovo);

        tabela = new TableView<>();
        tabela.setPrefHeight(400);

        TableColumn<Instituicao, String> colRazaoSocial = new TableColumn<>("Razão Social");
        colRazaoSocial.setCellValueFactory(new PropertyValueFactory<>("razaoSocial"));
        colRazaoSocial.setPrefWidth(120);

        TableColumn<Instituicao, String> colNomeFantasia = new TableColumn<>("Nome Fantasia");
        colNomeFantasia.setCellValueFactory(new PropertyValueFactory<>("nomeFantasia"));
        colNomeFantasia.setPrefWidth(120);

        TableColumn<Instituicao, String> colCnpj = new TableColumn<>("CNPJ");
        colCnpj.setCellValueFactory(new PropertyValueFactory<>("cnpj"));
        colCnpj.setPrefWidth(120);

        TableColumn<Instituicao, String> colData = new TableColumn<>("Fundação");
        colData.setCellValueFactory(new PropertyValueFactory<>("dataFundacao"));
        colData.setPrefWidth(120);

        TableColumn<Instituicao, String> colEndereco = new TableColumn<>("Endereço");
        colEndereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        colEndereco.setPrefWidth(120);

        TableColumn<Instituicao, Void> colAcoes = new TableColumn<>("Ações");
        colAcoes.setPrefWidth(200);
        colAcoes.setCellFactory(param -> new TableCell<Instituicao, Void>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnExcluir = new Button("Excluir");
            private final HBox container = new HBox(8, btnEditar, btnExcluir);

            {
                btnEditar.setStyle("-fx-background-color: #F5C842; -fx-text-fill: #2C4A7C; -fx-font-weight: bold; -fx-cursor: hand;");
                btnExcluir.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
                container.setAlignment(Pos.CENTER);

                btnEditar.setOnAction(e -> {
                    Instituicao item = getTableView().getItems().get(getIndex());
                    abrirFormulario(item);
                });

                btnExcluir.setOnAction(e -> {
                    Instituicao item = getTableView().getItems().get(getIndex());
                    Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION, "Confirmar exclusão?", ButtonType.YES, ButtonType.NO);
                    confirmacao.showAndWait().ifPresent(resposta -> {
                        if (resposta == ButtonType.YES) {
                            arquivo.excluir(item.getCnpj());
                            atualizarTabela();
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : container);
            }
        });

        tabela.getColumns().addAll(colRazaoSocial, colNomeFantasia, colCnpj, colData, colEndereco, colAcoes);
        rootPane.getChildren().addAll(cabecalho, tabela);
    }

    private void atualizarTabela() {
        ArrayList<Instituicao> lista = arquivo.lerLista();
        tabela.setItems(FXCollections.observableArrayList(lista));
    }

    private void abrirFormulario(Instituicao instituicaoExistente) {
        TextField txtRazaoSocial = new TextField();
        TextField txtNomeFantasia = new TextField();
        TextField txtCnpj = new TextField();
        TextField txtDataFundacao = new TextField();
        TextField txtEndereco = new TextField();
        txtDataFundacao.setPromptText("DD/MM/AAAA");

        if (instituicaoExistente != null) {
            txtRazaoSocial.setText(instituicaoExistente.getRazaoSocial());
            txtNomeFantasia.setText(instituicaoExistente.getNomeFantasia());
            txtCnpj.setText(instituicaoExistente.getCnpj());
            txtDataFundacao.setText(instituicaoExistente.getDataFundacao());
            txtEndereco.setText(instituicaoExistente.getEndereco());
            txtCnpj.setDisable(true);
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Razão Social:"), 0, 0);
        grid.add(txtRazaoSocial, 1, 0);
        grid.add(new Label("Nome Fantasia:"), 0, 1);
        grid.add(txtNomeFantasia, 1, 1);
        grid.add(new Label("CNPJ (14 dígitos):"), 0, 2);
        grid.add(txtCnpj, 1, 2);
        grid.add(new Label("Data Fundação:"), 0, 3);
        grid.add(txtDataFundacao, 1, 3);
        grid.add(new Label("Endereço:"), 0, 4);
        grid.add(txtEndereco, 1, 4);

        Button btnSalvar = new Button("Confirmar");
        btnSalvar.setStyle("-fx-background-color: #2C4A7C; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");

        VBox layoutModal = new VBox(20, grid, btnSalvar);
        layoutModal.setPadding(new Insets(20));

        btnSalvar.setOnAction(e -> {
            try {
                String razaoSocial = txtRazaoSocial.getText().trim();
                String nomeFantasia = txtNomeFantasia.getText().trim();
                String cnpj = txtCnpj.getText().trim();
                String dataStr = txtDataFundacao.getText().trim();
                String endereco = txtEndereco.getText().trim();

                if (razaoSocial.isEmpty()) throw new ValidacaoException("O campo 'Razão Social' é obrigatório.");
                if (nomeFantasia.isEmpty()) throw new ValidacaoException("O campo 'Nome Fantasia' é obrigatório.");
                if (!cnpj.matches("\\d{14}")) throw new ValidacaoException("O CNPJ deve conter exatamente 14 dígitos numéricos.");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                try {
                    LocalDate.parse(dataStr, formatter);
                } catch (DateTimeParseException ex) {
                    throw new ValidacaoException("A data deve estar no formato DD/MM/AAAA.");
                }

                if (endereco.isEmpty()) throw new ValidacaoException("O campo 'Endereço' é obrigatório.");

                Instituicao nova = new Instituicao(razaoSocial, nomeFantasia, cnpj, dataStr, endereco);

                if (instituicaoExistente == null) {
                    arquivo.adicionar(nova);
                } else {
                    arquivo.atualizar(instituicaoExistente.getCnpj(), nova);
                }

                ((Stage) btnSalvar.getScene().getWindow()).close();
            } catch (ValidacaoException ex) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro de Validação", ex.getMessage());
            }
        });

        Stage stageModal = new Stage();
        stageModal.setTitle(instituicaoExistente == null ? "Cadastrar Instituição" : "Editar Instituição");
        stageModal.initModality(Modality.APPLICATION_MODAL);
        stageModal.setScene(new Scene(layoutModal, 400, 320));
        stageModal.showAndWait();
        atualizarTabela();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String conteudo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(conteudo);
        alerta.showAndWait();
    }
}