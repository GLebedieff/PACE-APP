package com.example.paceapp.view;

import com.example.paceapp.model.ArquivoUsuario;
import com.example.paceapp.model.Usuario;
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

import java.util.ArrayList;

public class UsuarioView {

    private final ArquivoUsuario arquivo = new ArquivoUsuario();
    private VBox rootPane;
    private TableView<Usuario> tabela;

    public UsuarioView() {
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

        Label titulo = new Label("Gerenciamento de Usuários");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2C4A7C;");

        Button btnNovo = new Button("Novo Cadastro");
        btnNovo.setStyle("-fx-background-color: #2C4A7C; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        btnNovo.setOnAction(e -> abrirFormulario(null));
        cabecalho.getChildren().addAll(titulo, btnNovo);

        tabela = new TableView<>();
        tabela.setPrefHeight(400);

        TableColumn<Usuario, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colNome.setPrefWidth(120);

        TableColumn<Usuario, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEmail.setPrefWidth(120);

        TableColumn<Usuario, String> colTelefone = new TableColumn<>("Telefone");
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colTelefone.setPrefWidth(120);

        TableColumn<Usuario, Void> colAcoes = new TableColumn<>("Ações");
        colAcoes.setPrefWidth(200);
        colAcoes.setCellFactory(param -> new TableCell<Usuario, Void>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnExcluir = new Button("Excluir");
            private final HBox container = new HBox(8, btnEditar, btnExcluir);

            {
                btnEditar.setStyle("-fx-background-color: #F5C842; -fx-text-fill: #2C4A7C; -fx-font-weight: bold; -fx-cursor: hand;");
                btnExcluir.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
                container.setAlignment(Pos.CENTER);

                btnEditar.setOnAction(e -> {
                    Usuario item = getTableView().getItems().get(getIndex());
                    abrirFormulario(item);
                });

                btnExcluir.setOnAction(e -> {
                    Usuario item = getTableView().getItems().get(getIndex());
                    Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION, "Confirmar exclusão?", ButtonType.YES, ButtonType.NO);
                    confirmacao.showAndWait().ifPresent(resposta -> {
                        if (resposta == ButtonType.YES) {
                            arquivo.excluir(item.getEmail());
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

        tabela.getColumns().addAll(colNome, colEmail, colTelefone, colAcoes);
        rootPane.getChildren().addAll(cabecalho, tabela);
    }

    private void atualizarTabela() {
        ArrayList<Usuario> lista = arquivo.lerLista();
        tabela.setItems(FXCollections.observableArrayList(lista));
    }

    private void abrirFormulario(Usuario usuarioExistente) {
        TextField txtNome = new TextField();
        TextField txtEmail = new TextField();
        PasswordField txtSenha = new PasswordField();
        TextField txtTelefone = new TextField();

        if (usuarioExistente != null) {
            txtNome.setText(usuarioExistente.getNome());
            txtEmail.setText(usuarioExistente.getEmail());
            txtTelefone.setText(usuarioExistente.getTelefone());
            txtEmail.setDisable(true);
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Nome:"), 0, 0);
        grid.add(txtNome, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(txtEmail, 1, 1);
        grid.add(new Label("Senha:"), 0, 2);
        grid.add(txtSenha, 1, 2);
        grid.add(new Label("Telefone:"), 0, 3);
        grid.add(txtTelefone, 1, 3);

        Button btnSalvar = new Button("Confirmar");
        btnSalvar.setStyle("-fx-background-color: #2C4A7C; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");

        VBox layoutModal = new VBox(20, grid, btnSalvar);
        layoutModal.setPadding(new Insets(20));

        btnSalvar.setOnAction(e -> {
            try {
                String nome = txtNome.getText().trim();
                String email = txtEmail.getText().trim();
                String senha = txtSenha.getText();
                String telefone = txtTelefone.getText().trim();

                if (nome.isEmpty()) throw new ValidacaoException("O campo 'Nome' é obrigatório.");
                if (email.isEmpty() || !email.contains("@")) throw new ValidacaoException("Email inválido.");
                if (senha.isEmpty()) throw new ValidacaoException("A senha é obrigatória.");
                if (telefone.isEmpty() || !telefone.matches("\\d+")) throw new ValidacaoException("O campo 'Telefone' deve conter apenas números.");

                Usuario novo = new Usuario(nome, email, senha, telefone);

                if (usuarioExistente == null) {
                    arquivo.adicionar(novo);
                } else {
                    arquivo.atualizar(usuarioExistente.getEmail(), novo);
                }

                ((Stage) btnSalvar.getScene().getWindow()).close();
            } catch (ValidacaoException ex) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro de Validação: ", ex.getMessage());
            }
        });

        Stage stageModal = new Stage();
        stageModal.setTitle(usuarioExistente == null ? "Cadastrar Usuário" : "Editar Usuário");
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
