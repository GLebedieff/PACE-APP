package com.example.paceapp.view;

import com.example.paceapp.controller.CrudController;
import com.example.paceapp.model.ArquivoInstituicao;
import com.example.paceapp.model.Instituicao;
import com.example.paceapp.model.ValidacaoException;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class InstituicaoView extends BaseCrudView<Instituicao> {

    public InstituicaoView() {
        super("Gerenciamento de Instituições", new CrudController<>(new ArquivoInstituicao()));
    }

    @Override
    protected List<TableColumn<Instituicao, ?>> obterColunasDados() {
        List<TableColumn<Instituicao, ?>> colunas = new ArrayList<>();

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

        colunas.add(colRazaoSocial);
        colunas.add(colNomeFantasia);
        colunas.add(colCnpj);
        colunas.add(colData);
        colunas.add(colEndereco);

        return colunas;
    }

    @Override
    protected void abrirFormulario(Instituicao instituicaoExistente) {
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

                if (razaoSocial.isEmpty()) {
                    throw new ValidacaoException("O campo 'Razão Social' é obrigatório.");
                }

                if (nomeFantasia.isEmpty()) {
                    throw new ValidacaoException("O campo 'Nome Fantasia' é obrigatório.");
                }

                if (!cnpj.matches("\\d{14}")) {
                    throw new ValidacaoException("O CNPJ deve conter exatamente 14 dígitos numéricos.");
                }

                // validacao de data (Formato dd/MM/yyyy)
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                try {
                    LocalDate.parse(dataStr, formatter);
                } catch (DateTimeParseException ex) {
                    throw new ValidacaoException("A data deve estar no formato DD/MM/AAAA.");
                }

                if (endereco.isEmpty()) {
                    throw new ValidacaoException("O campo 'Endereço' é obrigatório.");
                }

                Instituicao nova = new Instituicao(razaoSocial, nomeFantasia, cnpj, dataStr, endereco);

                if (instituicaoExistente == null) {
                    controller.adicionar(nova);
                } else {
                    controller.atualizar(instituicaoExistente.getCnpj(), nova);
                }

                ((Stage) btnSalvar.getScene().getWindow()).close();
            } catch (ValidacaoException ex) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro de Validação", ex.getMessage());
            }
        });

        abrirModalFormulario(instituicaoExistente == null ? "Cadastrar Instituição" : "Editar Instituição", layoutModal);
    }

    @Override
    protected void excluirItem(Instituicao instituicao) {
        controller.excluir(instituicao.getCnpj());
    }
}