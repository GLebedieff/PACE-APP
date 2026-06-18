package com.example.paceapp.view;

import com.example.paceapp.controller.CrudController;
import com.example.paceapp.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;

public class TurmaView extends BaseCrudView<Turma> {

    public TurmaView() {
        super("Gerenciamento de Turmas", new CrudController<>(new ArquivoTurma()));
    }

    @Override
    protected List<TableColumn<Turma, ?>> obterColunasDados() {
        List<TableColumn<Turma, ?>> colunas = new ArrayList<>();

        TableColumn<Turma, String> colNome = new TableColumn<>("Nome da Turma");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nomeTurma"));
        colNome.setPrefWidth(120);

        TableColumn<Turma, String> colSerie = new TableColumn<>("Série");
        colSerie.setCellValueFactory(new PropertyValueFactory<>("serie"));
        colSerie.setPrefWidth(80);

        TableColumn<Turma, Integer> colQtd = new TableColumn<>("Qtdade. Alunos");
        colQtd.setCellValueFactory(new PropertyValueFactory<>("qtdAlunos"));
        colQtd.setPrefWidth(100);

        //Coluna para exibir o nome fantasia da instituição associada
        TableColumn<Turma, String> colInst = new TableColumn<>("Instituição");
        colInst.setCellValueFactory(cellData -> {
            Instituicao inst = cellData.getValue().getInstituicao();
            return new SimpleStringProperty(inst != null ? inst.getNomeFantasia() : "Sem Instituição");
        });
        colInst.setPrefWidth(150);


        colunas.add(colNome);
        colunas.add(colSerie);
        colunas.add(colQtd);
        colunas.add(colInst);

        return colunas;
    }

    @Override
    protected void abrirFormulario(Turma turmaExistente) {
        TextField txtNome = new TextField();
        TextField txtSerie = new TextField();
        TextField txtQtdAlunos = new TextField();

        // ---- PARA O CAMPO INSTITUIÇÃO ----
        ComboBox<Instituicao> cbInstituicao = new ComboBox<>();
        // Carrega as instituições do arquivo de dados no ComboBox
        ArrayList<Instituicao> listaInstituicoes = new ArquivoInstituicao().lerLista();
        cbInstituicao.setItems(FXCollections.observableArrayList(listaInstituicoes));

        // Exibir apenas o nome fantasia no ComboBox
        cbInstituicao.setConverter(new StringConverter<>() {
            @Override
            public String toString(Instituicao inst) {
                return inst != null ? inst.getNomeFantasia() : "";
            }
            @Override
            public Instituicao fromString(String string) {
                return null;
            }
        });

        // Para edição de turma
        if (turmaExistente != null) {
            txtNome.setText(turmaExistente.getNomeTurma());
            txtNome.setDisable(true); // Desabilita o nome da turma (identificador)
            txtSerie.setText(turmaExistente.getSerie());
            txtQtdAlunos.setText(String.valueOf(turmaExistente.getQtdAlunos()));
            cbInstituicao.setDisable(true); // Desabilita a seleção da instituição na alteração

            // Seleciona a instituição correspondente no ComboBox baseado no CNPJ
            if (turmaExistente.getInstituicao() != null) {
                for (Instituicao inst : cbInstituicao.getItems()) {
                    if (inst.getCnpj().equals(turmaExistente.getInstituicao().getCnpj())) {
                        cbInstituicao.setValue(inst);
                        break;
                    }
                }
            }
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Nome da Turma:"), 0, 0);
        grid.add(txtNome, 1, 0);
        grid.add(new Label("Série:"), 0, 1);
        grid.add(txtSerie, 1, 1);
        grid.add(new Label("Qtd. Alunos:"), 0, 2);
        grid.add(txtQtdAlunos, 1, 2);
        grid.add(new Label("Instituição:"), 0, 3);
        grid.add(cbInstituicao, 1, 3);


        Button btnSalvar = new Button("Confirmar");
        btnSalvar.setStyle("-fx-background-color: #2C4A7C; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");

        VBox layoutModal = new VBox(20, grid, btnSalvar);
        layoutModal.setPadding(new Insets(20));

        btnSalvar.setOnAction(e -> {
            try {
                String nome = txtNome.getText().trim();
                String serie = txtSerie.getText().trim();
                String qtdStr = txtQtdAlunos.getText().trim();
                Instituicao instSelecionada = cbInstituicao.getValue();

                // Validações nome e serie
                if (nome.isEmpty()) {
                    throw new ValidacaoException("O campo 'Nome da Turma' é obrigatório.");
                }
                if (serie.isEmpty()) {
                    throw new ValidacaoException("O campo 'Série' é obrigatório.");
                }

                // Validação qtdade de alunos
                int qtd;
                try {
                    qtd = Integer.parseInt(qtdStr);
                    if (qtd < 0) throw new NumberFormatException();
                } catch (NumberFormatException ex) {
                    throw new ValidacaoException("A quantidade de alunos deve ser um número inteiro válido e maior ou igual a zero.");
                }

                // Validação instituição
                if (instSelecionada == null) {
                    throw new ValidacaoException("É obrigatório selecionar uma Instituição.");
                }

                Turma nova = new Turma(nome, serie, qtd, instSelecionada);

                if (turmaExistente == null) {
                    controller.adicionar(nova);
                } else {
                    controller.atualizar(turmaExistente.getNomeTurma(), nova);
                }

                ((Stage) btnSalvar.getScene().getWindow()).close();
            } catch (ValidacaoException ex) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro de Validação", ex.getMessage());
            }
        });

        abrirModalFormulario(turmaExistente == null ? "Cadastrar Turma" : "Editar Turma", layoutModal);
    }

    @Override
    protected void excluirItem(Turma turma) {
        controller.excluir(turma.getNomeTurma());
    }
}