package com.example.paceapp.view;

import com.example.paceapp.controller.CrudController;
import com.example.paceapp.model.ArquivoResponsavelLegal;
import com.example.paceapp.model.ResponsavelLegal;
import com.example.paceapp.model.ValidacaoException;
import javafx.geometry.Insets; // Importação correta do Insets do JavaFX
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ResponsavelLegalView extends BaseCrudView<ResponsavelLegal> {

    public ResponsavelLegalView() {
        super("Gerenciamento de Responsáveis Legais", new CrudController<>(new ArquivoResponsavelLegal()));
    }

    @Override
    protected List<TableColumn<ResponsavelLegal, ?>> obterColunasDados() {
        List<TableColumn<ResponsavelLegal, ?>> colunas = new ArrayList<>();

        TableColumn<ResponsavelLegal, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colNome.setPrefWidth(150);

        TableColumn<ResponsavelLegal, String> colCpf = new TableColumn<>("CPF");
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colCpf.setPrefWidth(120);

        TableColumn<ResponsavelLegal, String> colTelefone = new TableColumn<>("Telefone");
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colTelefone.setPrefWidth(110);

        TableColumn<ResponsavelLegal, String> colParentesco = new TableColumn<>("Parentesco");
        colParentesco.setCellValueFactory(new PropertyValueFactory<>("parentesco"));
        colParentesco.setPrefWidth(110);

        TableColumn<ResponsavelLegal, String> colNascimento = new TableColumn<>("Nascimento");
        colNascimento.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));
        colNascimento.setPrefWidth(100);

        colunas.add(colNome);
        colunas.add(colCpf);
        colunas.add(colTelefone);
        colunas.add(colNascimento);
        colunas.add(colParentesco);

        return colunas;
    }

    @Override
    protected void abrirFormulario(ResponsavelLegal responsavelExistente){
        TextField txtNome = new TextField(); // Criando os campos (TextField - caixa de entrada de texto)
        TextField txtcpf = new TextField();
        TextField txtTelefone = new TextField();
        TextField txtParentesco = new TextField();
        TextField txtDataNascimento = new TextField();
        txtDataNascimento.setPromptText("DD/MM/AAAA"); //Como se fosse o placeholder

        if(responsavelExistente != null){
            txtNome.setText(responsavelExistente.getNome());
            txtcpf.setText(responsavelExistente.getCpf()); // Preenchendo com o valor pego no get do objeto
            txtTelefone.setText(responsavelExistente.getTelefone());
            txtParentesco.setText(responsavelExistente.getParentesco());
            txtDataNascimento.setText(responsavelExistente.getDataNascimento());
            txtcpf.setDisable(true); // CPF desabilitado na edição
        }

        GridPane grid = new GridPane(); // GridPane - organiza os elementos em linhas e colunas
        grid.setHgap(10); // Espaçamento horizontal
        grid.setVgap(10); // Espaçamento vertical
        grid.add(new Label ("Nome:"), 0, 0); // colocando o label (linha 0 coluna 0)
        grid.add(txtNome, 1,0); // Vai ir o valor do nome lá de cima abaixo do label (dentro do TextFild);
        grid.add(new Label("CPF (11 dígitos):"), 0,1);
        grid.add(txtcpf, 1, 1);
        grid.add(new Label("Telefone:"),0, 2);
        grid.add(txtTelefone, 1, 2);
        grid.add(new Label("Parentesco:"), 0, 3);
        grid.add(txtParentesco, 1, 3);
        grid.add(new Label("Data Nascimento:"), 0, 4);
        grid.add(txtDataNascimento, 1, 4);

        Button btnSalvar = new Button("Confirmar");
        btnSalvar.setStyle("-fx-background-color: #2C4A7C; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");

        VBox layoutModal = new VBox(20, grid, btnSalvar);
        layoutModal.setPadding(new Insets(20));

        btnSalvar.setOnAction(e -> {
            try{
                String nome = txtNome.getText().trim();
                String cpf = txtcpf.getText().trim(); // Pegar o dado inserido e armazenar na String cpf
                String telefone = txtTelefone.getText().trim();
                String parentesco = txtParentesco.getText().trim();
                String dataNascimento = txtDataNascimento.getText().trim();

                // Validações para campos obrigatórios
                if (nome.isEmpty()) throw new ValidacaoException("O campo 'Nome' é obrigatório.");
                if (!cpf.matches("\\d{11}")) throw new ValidacaoException("O CPF deve conter exatamente 11 dígitos numéricos.");
                if (telefone.isEmpty()) throw new ValidacaoException("O telefone é obrigatório.");
                if (parentesco.isEmpty()) throw new ValidacaoException("O parentesco é obrigatório.");

                // Validação da Data (Formato DD/MM/AAAA)
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                try {
                    LocalDate.parse(dataNascimento, formatter);
                } catch (DateTimeParseException ex) {
                    throw new ValidacaoException("A data de nascimento deve estar no formato DD/MM/AAAA.");
                }
                ResponsavelLegal novo = new ResponsavelLegal(nome, cpf, telefone, parentesco, dataNascimento);
                if(responsavelExistente == null){
                    controller.adicionar(novo);
                }else{
                    controller.atualizar(responsavelExistente.getCpf(), novo);
                }
                ((Stage) btnSalvar.getScene().getWindow()).close();
            }catch(ValidacaoException ex){
                mostrarAlerta(Alert.AlertType.ERROR, "Erro de validação", ex.getMessage());
            }
        });
        abrirModalFormulario(responsavelExistente == null ? "Cadastrar Responsável":
                "Editar responsável", layoutModal);
    }
    @Override
    protected void excluirItem(ResponsavelLegal responsavel) {
        controller.excluir(responsavel.getCpf());
    }
}