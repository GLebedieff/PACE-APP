package com.example.paceapp.view;

import com.example.paceapp.controller.CrudController;
import com.example.paceapp.model.ArquivoUsuario;
import com.example.paceapp.model.Usuario;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class UsuarioView extends BaseCrudView<Usuario> {
    public UsuarioView() {
        super("Gerenciamento de Usuários", new CrudController<>(new ArquivoUsuario()));
    }

    @Override
    protected List<TableColumn<Usuario, ?>> obterColunasDados() {
        List<TableColumn<Usuario, ?>> colunas = new ArrayList<>();

        TableColumn<Usuario, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colNome.setPrefWidth(120);

        TableColumn<Usuario, String> colEmail = new TableColumn<>("Email");
        colNome.setCellValueFactory(new PropertyValueFactory<>("email"));
        colNome.setPrefWidth(120);

        TableColumn<Usuario, String> colTelefone = new TableColumn<>("Telefone");
        colNome.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colNome.setPrefWidth(120);

        colunas.add(colNome);
        colunas.add(colEmail);
        colunas.add(colTelefone);

        return colunas;
    }

    @Override
    protected void abrirFormulario(Usuario usuarioExistente) {
        TextField txtNome = new TextField();
        TextField txtEmail = new TextField();
        PasswordField txtSenha = new PasswordField();
        TextField txtTelefone = new TextField();

        //pega os dados enquanto estiver editando e bloqueia o email (por ele ser o id)
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

        Button btnSalvar =  new Button("Confirmar");
        btnSalvar.setStyle("-fx-background-color: #2C4A7C; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");

        VBox layoutModal = new VBox(20, grid, btnSalvar);
        layoutModal.setPadding(new Insets(20));

        btnSalvar.setOnAction(e -> {
            try {
                String nome = txtNome.getText().trim();
                String email = txtEmail.getText().trim();
                String senha = txtSenha.getText();
                String telefone = txtTelefone.getText().trim();

                //validacao dos campos
                if (nome.isEmpty()) {
                    throw new ValidacaoException("O campo 'Nome' é obrigatório.");
                }
                if (email.isEmpty() || !email.contains("@")){
                    throw new ValidacaoException("Email inválido.");
                }
                if (senha.isEmpty()) {
                    throw new ValidacaoException("A senha é obrigatória.");
                }
                if (telefone.isEmpty() || !telefone.matches("\\d+")) {
                    throw new ValidacaoException("O campo 'Telefone' deve conter apenas números.");
                }

                Usuario novo = new Usuario(nome, email, senha, telefone);

                if (usuarioExistente == null) {
                    controller.adicionar(novo);
                } else {
                    controller.atualizar(usuarioExistente.getEmail(), novo); //passa o emai antigo como chave
                }

                //fechar o modal
                ((Stage) btnSalvar.getScene().getWindow()).close();
            } catch (ValidacaoException ex) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro de Validação: ", ex.getMessage());
            }
        });

        abrirModalFormulario(usuarioExistente == null ? "Cadastrar Usuário" : "Editar Usuário", layoutModal);
    }

    @Override
    protected void excluirItem(Usuario usuario) {
        controller.excluir(usuario.getEmail());
    }
}
