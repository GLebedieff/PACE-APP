package com.example.paceapp.view;

import com.example.paceapp.controller.CrudController;
import com.example.paceapp.model.ArquivoProfessor;
import com.example.paceapp.model.Professor;
import com.example.paceapp.model.ValidacaoException;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ProfessorView extends BaseCrudView<Professor> {

    public ProfessorView(){   // Inicializando a tela genérica (parâmetros: título e controller cm a persistência)
        super("Gerenciamento de Professores", new CrudController<>(new ArquivoProfessor()));
    }

    @Override
    protected List<TableColumn<Professor, ?>> obterColunasDados(){ // ? aceita vários tipos, não só String
        List<TableColumn<Professor, ?>> colunas = new ArrayList<>();

        TableColumn<Professor, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome")); // Corrigido o getter de "nomw" para "nome"
        colNome.setPrefWidth(150);

        TableColumn<Professor, String> colCpf = new TableColumn<>("CPF");
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colCpf.setPrefWidth(120);

        TableColumn<Professor, String> colTelefone = new TableColumn<>("Telefone");
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colTelefone.setPrefWidth(110);

        TableColumn<Professor, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEmail.setPrefWidth(150);

        TableColumn<Professor, String> colDisciplina = new TableColumn<>("Disciplina");
        colDisciplina.setCellValueFactory(new PropertyValueFactory<>("disciplina"));
        colDisciplina.setPrefWidth(110);

        colunas.add(colNome);
        colunas.add(colCpf);
        colunas.add(colTelefone);
        colunas.add(colEmail);
        colunas.add(colDisciplina);
        return colunas;
    }

    @Override
    protected void abrirFormulario(Professor professorExistente){ // Corrigido typo de abrirFormulatio para abrirFormulario
        TextField txtNome = new TextField();
        TextField txtCpf = new TextField();
        TextField txtTelefone = new TextField();
        TextField txtEmail = new TextField();
        TextField txtDisciplina = new TextField();

        if(professorExistente != null){ // Se for edição, ou seja professorExistente n for null
            txtNome.setText(professorExistente.getNome()); // vai setar no nome o conteúdo do get do existente
            txtCpf.setText(professorExistente.getCpf());
            txtTelefone.setText(professorExistente.getTelefone());
            txtEmail.setText(professorExistente.getEmail());
            txtDisciplina.setText(professorExistente.getDisciplina());
            txtCpf.setDisable(true);
        }

        GridPane grid = new GridPane();
        grid.setHgap(10); // Espaçamento horizontal
        grid.setVgap(10); // Espaçamento vertical
        grid.add(new Label("Nome:"), 0, 0);
        grid.add(txtNome, 1, 0);
        grid.add(new Label("CPF (11 dígitos):"), 0, 1);
        grid.add(txtCpf, 1, 1);
        grid.add(new Label("Telefone:"), 0, 2);
        grid.add(txtTelefone, 1, 2);
        grid.add(new Label("Email:"), 0, 3);
        grid.add(txtEmail, 1, 3);
        grid.add(new Label("Disciplina:"), 0, 4);
        grid.add(txtDisciplina, 1, 4);

        Button btnSalvar = new Button("Confirmar");
        btnSalvar.setStyle("-fx-background-color: #2C4A7C; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");

        VBox layoutModal = new VBox(20, grid, btnSalvar);
        layoutModal.setPadding(new Insets(20));

        btnSalvar.setOnAction(e -> { // Quando a pessoa clicar no botão, executará esse código
            try { // Pegando as informações registradas nos campos do formulário
                String nome = txtNome.getText().trim();
                String cpf = txtCpf.getText().trim();
                String telefone = txtTelefone.getText().trim();
                String email = txtEmail.getText().trim();
                String disciplina = txtDisciplina.getText().trim();

                // Validação dos campos obrigatórios
                if (nome.isEmpty()) throw new ValidacaoException("O campo 'Nome' é obrigatório.");
                if (!cpf.matches("\\d{11}")) throw new ValidacaoException("O CPF deve conter exatamente 11 dígitos numéricos.");
                if (telefone.isEmpty()) throw new ValidacaoException("O telefone é obrigatório.");
                if (email.isEmpty() || !email.contains("@")) throw new ValidacaoException("E-mail inválido.");
                if (disciplina.isEmpty()) throw new ValidacaoException("O campo 'Disciplina' é obrigatório.");

                // Criando o objet do Model (regra de negócio) com o que foi preenchido no formulário
                Professor novo = new Professor(nome, cpf, telefone, email, disciplina);

                if(professorExistente == null){
                    controller.adicionar(novo);
                }else {
                    controller.atualizar(professorExistente.getCpf(), novo); // Caso exista cadastro, atualizá-lo
                }

                ((Stage) btnSalvar.getScene().getWindow()).close();
            }catch(ValidacaoException ex){
                mostrarAlerta(Alert.AlertType.ERROR, "Erro de validação", ex.getMessage());
            }
        });

        // Abre a janela de diálogo do formulário (necessário para a tela abrir)
        abrirModalFormulario(professorExistente == null ? "Cadastrar Professor" : "Editar Professor", layoutModal);
    } // Chave que fecha abrirFormulario de forma correta e isolada

    @Override
    protected void excluirItem(Professor professor) {
        controller.excluir(professor.getCpf()); // Solicita exclusão para o controller, passando o cpf
    }
}