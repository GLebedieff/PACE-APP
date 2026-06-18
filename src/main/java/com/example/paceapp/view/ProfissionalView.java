package com.example.paceapp.view;

import com.example.paceapp.controller.CrudController;
import com.example.paceapp.model.*;
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

public class ProfissionalView extends BaseCrudView<Profissional> {

    public ProfissionalView (){
        super("Gerenciamento Profissional", new CrudController<>(new ArquivoProfissional()));
    }

    // nome, cpf, email, crm
    @Override

    //ara cada atributo, cria-se uma TableColumn
    protected List<TableColumn<Profissional, ?>> obterColunasDados(){
        List<TableColumn<Profissional, ?>> colunas = new ArrayList<>(); // declara a variavel colunas do tipo List<<>>
        /*TableCOlumn = classe q representa uma coluna que tem dois parametros, ALuno e ?. Pirmeiro é tipo e dps o valor
         * */

        TableColumn<Profissional, String> colNome = new TableColumn<>("Nome"); // coluna especifica; COnstructor new TableColumn recebe argumentos
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));// setCellValueFactory diz como a coluna vai descobrir o valor a mostrar em cada célula
        colNome.setPrefWidth(120);// style

        TableColumn<Profissional, String> colCpf = new TableColumn<>("Cpf");
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colCpf.setPrefWidth(120);

        TableColumn<Profissional, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEmail.setPrefWidth(120);

        TableColumn<Profissional, String> colCrm = new TableColumn<>("Crm");
        colCrm.setCellValueFactory(new PropertyValueFactory<>("crm"));
        colCrm.setPrefWidth(120);


        colunas.add(colNome);
        colunas.add(colCpf);
        colunas.add(colEmail);
        colunas.add(colCrm);

        return colunas;

    }

    @Override
    protected void abrirFormulario(Profissional profissionalExistente){
        TextField txtNome = new TextField();
        TextField txtCpf = new TextField();
        TextField txtEmail = new TextField();
        TextField txtCrm = new TextField(); // cria campo de texto vazios

        if (profissionalExistente !=null){
            txtNome.setText(profissionalExistente.getNome());
            txtCpf.setText(profissionalExistente.getCpf());
            txtEmail.setText(profissionalExistente.getEmail());
            txtCrm.setText(profissionalExistente.getCrm());
            txtCrm.setDisable(true);
        }

        GridPane grid = new GridPane(); // Layout container
        grid.setHgap(10);
        grid.setVgap(10);

        //mais layout
        grid.add(new Label ("Nome"), 0, 0);
        grid.add(txtNome, 1, 0 );
        grid.add(new Label ("CPF"), 0, 1);
        grid.add(txtCpf, 1, 1 );
        grid.add(new Label ("Email"), 0, 2);
        grid.add(txtEmail, 1, 2 );
        grid.add(new Label ("CRM"), 0, 3);
        grid.add(txtCrm, 1, 3 );

        Button btnSalvar = new Button("Confirma");// cria botao
        btnSalvar.setStyle("-fx-background-color: #2C4A7C; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");// estiliza botao

        //meu deus, mays layout. n aguento mais
        VBox layoutModal = new VBox(20, grid, btnSalvar);
        layoutModal.setPadding(new Insets(20));


        btnSalvar.setOnAction(e -> {
            try{
                String nome = txtNome.getText().trim();
                String cpf = txtCpf.getText().trim();
                String email = txtEmail.getText().trim();
                String crm = txtCrm.getText().trim();


                if (nome.isEmpty()) {
                    throw new ValidacaoException("O campo 'Nome' é obrigatório.");
                }

                if (cpf.isEmpty()) {
                    throw new ValidacaoException("O campo 'Série' é obrigatório.");
                }

                if (!cpf.matches("\\d{11}")) {
                    throw new ValidacaoException("O CPF deve conter exatamente 11 dígitos numéricos.");
                }

                if (crm.isEmpty()) {
                    throw new ValidacaoException("O campo 'Crm' é obrigatório");
                }

                Profissional novo = new Profissional(nome,cpf, email, crm);

                if (profissionalExistente == null){
                    controller.adicionar(novo);
                }else {
                    controller.atualizar(profissionalExistente.getCrm(), novo);
                }

                ((Stage) btnSalvar.getScene().getWindow()).close();
            } catch (ValidacaoException ex){
                mostrarAlerta(Alert.AlertType.ERROR, "Erro de Validação", ex.getMessage());
            }
        });
        abrirModalFormulario(profissionalExistente == null ? "Cadastrar profissional:" : "Editar profissional", layoutModal);
    }

    @Override
    protected void excluirItem(Profissional profissional) {
        controller.excluir(profissional.getCrm());
    }




























}
