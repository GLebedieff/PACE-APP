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

public class PedagogoView extends BaseCrudView<Pedagogo> {

    public PedagogoView() {
        super("Gerenciamento de Pedagogos", new CrudController<>(new ArquivoPedagogo()));
    }

    @Override
    protected List<TableColumn<Pedagogo, ?>> obterColunasDados() {
        List<TableColumn<Pedagogo, ?>> colunas = new ArrayList<>();

        TableColumn<Pedagogo, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colNome.setPrefWidth(120);

        TableColumn<Pedagogo, String> colCpf = new TableColumn<>("CPF");
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colCpf.setPrefWidth(100);

        TableColumn<Pedagogo, String> colEsp = new TableColumn<>("Especialização");
        colEsp.setCellValueFactory(new PropertyValueFactory<>("especializacao"));
        colEsp.setPrefWidth(120);

        //Coluna para exibir o nome fantasia da instituição associada
        TableColumn<Pedagogo, String> colInst = new TableColumn<>("Instituição");
        colInst.setCellValueFactory(cellData -> {
            Instituicao inst = cellData.getValue().getInstituicao();
            return new SimpleStringProperty(inst != null ? inst.getNomeFantasia() : "Sem Instituição");
        });
        colInst.setPrefWidth(150);


        colunas.add(colNome);
        colunas.add(colCpf);
        colunas.add(colEsp);
        colunas.add(colInst);

        return colunas;
    }

    @Override
    protected void abrirFormulario(Pedagogo pedagogoExistente) {
        TextField txtNome = new TextField();
        TextField txtCpf = new TextField();
        TextField txtEspecializacao = new TextField();

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

        // Para edição de pedagogo
        if (pedagogoExistente != null) {
            txtNome.setText(pedagogoExistente.getNome());
            txtCpf.setText(pedagogoExistente.getCpf());
            txtEspecializacao.setText(pedagogoExistente.getEspecializacao());

            txtCpf.setDisable(true);          // CPF desabilitado na edição (identificador)
            cbInstituicao.setDisable(true);   // Instituição desabilitada na edição
            // Seleciona a instituição existente no ComboBox
            if (pedagogoExistente.getInstituicao() != null) {
                for (Instituicao inst : cbInstituicao.getItems()) {
                    if (inst.getCnpj().equals(pedagogoExistente.getInstituicao().getCnpj())) {
                        cbInstituicao.setValue(inst);
                        break;
                    }
                }
            }
        }

        // Layout do formulário
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Nome:"), 0, 0);
        grid.add(txtNome, 1, 0);
        grid.add(new Label("CPF (11 dígitos):"), 0, 1);
        grid.add(txtCpf, 1, 1);
        grid.add(new Label("Especialização:"), 0, 2);
        grid.add(txtEspecializacao, 1, 2);
        grid.add(new Label("Instituição:"), 0, 3);
        grid.add(cbInstituicao, 1, 3);

        Button btnSalvar = new Button("Confirmar");
        btnSalvar.setStyle("-fx-background-color: #2C4A7C; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");

        VBox layoutModal = new VBox(20, grid, btnSalvar);
        layoutModal.setPadding(new Insets(20));

        btnSalvar.setOnAction(e -> {
            try {
                String nome = txtNome.getText().trim();
                String cpf = txtCpf.getText().trim();
                String especializacao = txtEspecializacao.getText().trim();
                Instituicao instSelecionada = cbInstituicao.getValue();

                // Validações
                if (nome.isEmpty()) throw new ValidacaoException("O campo 'Nome' é obrigatório.");
                if (!cpf.matches("\\d{11}")) throw new ValidacaoException("O CPF deve conter exatamente 11 dígitos numéricos.");
                if (especializacao.isEmpty()) throw new ValidacaoException("O campo 'Especialização' é obrigatório.");
                if (instSelecionada == null) throw new ValidacaoException("A seleção de uma Instituição é obrigatória.");

                Pedagogo novoPedagogo = new Pedagogo(nome, cpf, especializacao, instSelecionada);
                if (pedagogoExistente == null) {
                    controller.adicionar(novoPedagogo);
                } else {
                    controller.atualizar(pedagogoExistente.getCpf(), novoPedagogo);
                }

                ((Stage) btnSalvar.getScene().getWindow()).close();
            } catch (ValidacaoException ex) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro de Validação", ex.getMessage());
            }
        });

        abrirModalFormulario(pedagogoExistente == null ? "Cadastrar Pedagogo" : "Editar Pedagogo", layoutModal);
    }

    @Override
    protected void excluirItem(Pedagogo pedagogo) {
        controller.excluir(pedagogo.getCpf());
    }
}
