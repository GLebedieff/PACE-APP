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

public class AlunoView extends BaseCrudView<Aluno> {

    public AlunoView (){
        super("Gerenciamento Alunos", new CrudController<>(new ArquivoAluno()));
    }

    // nome, serie, daNasc, matricula
    @Override

    //ara cada atributo, cria-se uma TableColumn
    protected List<TableColumn<Aluno, ?>> obterColunasDados(){
        List<TableColumn<Aluno, ?>> colunas = new ArrayList<>(); // declara a variavel colunas do tipo List<<>>
        /*TableCOlumn = classe q representa uma coluna que tem dois parametros, ALuno e ?. Pirmeiro é tipo e dps o valor
        * */

        TableColumn<Aluno, String> colNome = new TableColumn<>("Nome"); // coluna especifica; COnstructor new TableColumn recebe argumentos
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));// setCellValueFactory diz como a coluna vai descobrir o valor a mostrar em cada célula
        colNome.setPrefWidth(120);// style

        TableColumn<Aluno, String> colSerie = new TableColumn<>("Serie");
        colSerie.setCellValueFactory(new PropertyValueFactory<>("serie"));
        colSerie.setPrefWidth(120);

        TableColumn<Aluno, String> colDataNasc = new TableColumn<>("Data De Nascimento");
        colDataNasc.setCellValueFactory(new PropertyValueFactory<>("dataNasc"));
        colDataNasc.setPrefWidth(120);

        TableColumn<Aluno, String> colMatricula = new TableColumn<>("Matricula");
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colMatricula.setPrefWidth(120);


        colunas.add(colNome);
        colunas.add(colSerie);
        colunas.add(colDataNasc);
        colunas.add(colMatricula);

        return colunas;

    }

    @Override
    protected void abrirFormulario(Aluno alunoExistente){
        TextField txtNome = new TextField();
        TextField txtSerie = new TextField();
        TextField txtDataNasc = new TextField();
        TextField txtMatricula = new TextField(); // cria campo de texto vazios
        txtDataNasc.setPromptText("DD/MM/AAAA"); // campo q dica no campo

        if (alunoExistente !=null){
            txtNome.setText(alunoExistente.getNome());
            txtSerie.setText(alunoExistente.getSerie());
            txtDataNasc.setText(alunoExistente.getDataNasc());
            txtMatricula.setText(alunoExistente.getMatricula());
            txtMatricula.setDisable(true);
        }

        GridPane grid = new GridPane(); // Layout container
        grid.setHgap(10);
        grid.setVgap(10);

        //mais layout
        grid.add(new Label ("Nome"), 0, 0);
        grid.add(txtNome, 1, 0 );
        grid.add(new Label ("Série"), 0, 1);
        grid.add(txtSerie, 1, 1 );
        grid.add(new Label ("Data nascimento"), 0, 2);
        grid.add(txtDataNasc, 1, 2 );
        grid.add(new Label ("Matriucla"), 0, 3);
        grid.add(txtMatricula, 1, 3 );

        Button btnSalvar = new Button("Confirma");// cria botao
        btnSalvar.setStyle("-fx-background-color: #2C4A7C; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");// estiliza botao

        //meu deus, mays layout. n aguento mais
        VBox layoutModal = new VBox(20, grid, btnSalvar);
        layoutModal.setPadding(new Insets(20));


        btnSalvar.setOnAction(e -> {
            try{
                String nome = txtNome.getText().trim();
                String serie = txtSerie.getText().trim();
                String dataNasc = txtDataNasc.getText().trim();
                String matricula = txtMatricula.getText().trim();


                if (nome.isEmpty()) {
                    throw new ValidacaoException("O campo 'Nome' é obrigatório.");
                }

                if (serie.isEmpty()) {
                    throw new ValidacaoException("O campo 'Série' é obrigatório.");
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                try {
                    LocalDate.parse(dataNasc, formatter);
                } catch (DateTimeParseException ex) {
                    throw new ValidacaoException("A data deve estar no formato DD/MM/AAAA.");
                }

                if (matricula.isEmpty()) {
                    throw new ValidacaoException("O campo 'Matricula' é obrigatório");
                }

                Aluno novo = new Aluno(nome,serie, dataNasc, matricula);

                if (alunoExistente == null){
                    controller.adicionar(novo);
                }else {
                    controller.atualizar(alunoExistente.getMatricula(), novo);
                }

                ((Stage) btnSalvar.getScene().getWindow()).close();
            } catch (ValidacaoException ex){
                mostrarAlerta(Alert.AlertType.ERROR, "Erro de Validação", ex.getMessage());
            }
        });
        abrirModalFormulario(alunoExistente == null ? "Cadastrar aluno:" : "Editar aluno", layoutModal);
    }

    @Override
    protected void excluirItem(Aluno aluno) {
        controller.excluir(aluno.getMatricula());
    }




























}
