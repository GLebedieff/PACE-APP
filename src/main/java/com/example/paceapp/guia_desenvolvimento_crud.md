# Guia de Desenvolvimento: CRUD Genérico com MVC, JavaFX Puro e Persistência em Arquivo

Este guia apresenta um passo a passo atualizado para você desenvolver os CRUDs de **Usuário** e **Instituição** utilizando uma **arquitetura genérica e reutilizável baseada no padrão MVC (Model-View-Controller)**. 

Essa arquitetura resolve todas as suas considerações:
1. **Atualização Completa**: O método `atualizar` recebe o objeto modificado por inteiro e substitui o registro antigo correspondente.
2. **Controller e View Genéricos**: Reduz a repetição de código ao criar uma `BaseCrudView<T>` e um `CrudController<T>` parametrizados.
3. **Desacoplamento MVC**: O `Controller` atua intermediando a `View` (interface) e o `Model` (dados e persistência).
4. **Tabela com Botões de Ação na Linha**: A tabela exibe as colunas de dados e uma coluna especial de **Ações** contendo botões **Editar** e **Excluir** para cada registro individual.
5. **Formulário de Cadastro Modal**: Um botão central "+ Novo Cadastro" abre uma tela de formulário popup (janela Modal), mantendo a listagem principal limpa.

---

## 📂 Nova Estrutura de Pastas e Arquivos

```
com.example.paceapp
│
├── module-info.java (Permissões de reflexão para o TableView)
│
├── model/
│   ├── Pessoa.java (Entidade básica)
│   ├── Usuario.java (Entidade usuário)
│   ├── Instituicao.java (Entidade instituição)
│   ├── Persistencia.java (Interface genérica para persistência)
│   ├── ArquivoUsuario.java (Persistência em arquivo para Usuário)
│   ├── ArquivoInstituicao.java (Persistência em arquivo para Instituição)
│   └── ValidacaoException.java (Exceção customizada)
│
├── controller/
│   └── CrudController.java (Controller genérico para operações de CRUD)
│
└── view/
    ├── MainView.java (Ajustar para carregar as views na área de conteúdo)
    ├── BaseCrudView.java (Superclasse genérica da interface visual)
    ├── UsuarioView.java (View específica de Usuário herdando da genérica)
    └── InstituicaoView.java (View específica de Instituição herdando da genérica)
```

---

## 🛠️ Passo 1: Ajustar o `module-info.java`

Certifique-se de que o pacote `model` está aberto para o `javafx.base` em [module-info.java](file:///c:/Users/gigil/IdeaProjects/PACE-APP/src/main/java/module-info.java) para que o `TableView` consiga mapear as propriedades dos seus objetos.

```java
module com.example.paceapp {
    requires javafx.controls;
    
    opens com.example.paceapp.model to javafx.base;
    
    exports com.example.paceapp;
    exports com.example.paceapp.model;
    exports com.example.paceapp.view;
    exports com.example.paceapp.controller;
}
```

---

## 💾 Passo 2: Camada de Modelo (Model)

### 2.1 Entidades Básicas
Ajuste [Pessoa.java](file:///c:/Users/gigil/IdeaProjects/PACE-APP/src/main/java/com/example/paceapp/model/Pessoa.java) e [Usuario.java](file:///c:/Users/gigil/IdeaProjects/PACE-APP/src/main/java/com/example/paceapp/model/Usuario.java) para implementarem `java.io.Serializable`. Crie também `Instituicao.java` e `ValidacaoException.java`.

#### **Instituicao.java** (Novo arquivo em `model/`)
```java
package com.example.paceapp.model;

public class Instituicao extends Pessoa {
    private static final long serialVersionUID = 1L;

    private String cnpj;
    private String dataFundacao; // DD/MM/AAAA

    public Instituicao(String nome, String cnpj, String dataFundacao) {
        super(nome);
        this.cnpj = cnpj;
        this.dataFundacao = dataFundacao;
    }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getDataFundacao() { return dataFundacao; }
    public void setDataFundacao(String dataFundacao) { this.dataFundacao = dataFundacao; }
}
```

#### **ValidacaoException.java** (Novo arquivo em `model/`)
```java
package com.example.paceapp.model;

public class ValidacaoException extends Exception {
    public ValidacaoException(String mensagem) {
        super(mensagem);
    }
}
```

---

### 2.2 Camada de Persistência Genérica
Para que possamos reutilizar o Controller e a View, precisamos de uma interface comum para as classes de manipulação de arquivo.

#### **Persistencia.java** (Novo arquivo em `model/`)
```java
package com.example.paceapp.model;

import java.util.ArrayList;

public interface Persistencia<T> {
    ArrayList<T> lerLista();
    void salvarLista(ArrayList<T> lista);
    void adicionar(T item);
    void atualizar(String chaveAntiga, T itemAtualizado);
    void excluir(String chave);
}
```

#### **ArquivoUsuario.java** (Novo/Ajustado em `model/`)
Ajuste o gerenciador de persistência para implementar `Persistencia<Usuario>`. Repare que o método `atualizar` localiza o usuário antigo pelo e-mail e **substitui o objeto inteiro** na lista, permitindo atualizar todos os atributos de uma vez.

```java
package com.example.paceapp.model;

import java.io.*;
import java.util.ArrayList;

public class ArquivoUsuario implements Persistencia<Usuario> {
    private static final String CAMINHO_ARQUIVO = "usuarios.dat";

    @Override
    public void salvarLista(ArrayList<Usuario> lista) {
        try {
            File arq = new File(CAMINHO_ARQUIVO);
            if (!arq.exists()) arq.createNewFile();
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arq))) {
                oos.writeObject(lista);
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar lista de usuários: " + e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public ArrayList<Usuario> lerLista() {
        ArrayList<Usuario> lista = new ArrayList<>();
        File arq = new File(CAMINHO_ARQUIVO);
        if (arq.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arq))) {
                lista = (ArrayList<Usuario>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erro ao ler lista de usuários: " + e.getMessage());
            }
        }
        return lista;
    }

    @Override
    public void adicionar(Usuario novo) {
        ArrayList<Usuario> lista = lerLista();
        lista.add(novo);
        salvarLista(lista);
    }

    @Override
    public void atualizar(String emailAntigo, Usuario atualizado) {
        ArrayList<Usuario> lista = lerLista();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getEmail().equalsIgnoreCase(emailAntigo)) {
                lista.set(i, atualizado); // Substitui todos os dados do objeto
                break;
            }
        }
        salvarLista(lista);
    }

    @Override
    public void excluir(String email) {
        ArrayList<Usuario> lista = lerLista();
        lista.removeIf(usuario -> usuario.getEmail().equalsIgnoreCase(email));
        salvarLista(lista);
    }
}
```

#### **ArquivoInstituicao.java** (Novo/Ajustado em `model/`)
Implementação semelhante para `Persistencia<Instituicao>`.

```java
package com.example.paceapp.model;

import java.io.*;
import java.util.ArrayList;

public class ArquivoInstituicao implements Persistencia<Instituicao> {
    private static final String CAMINHO_ARQUIVO = "instituicoes.dat";

    @Override
    public void salvarLista(ArrayList<Instituicao> lista) {
        try {
            File arq = new File(CAMINHO_ARQUIVO);
            if (!arq.exists()) arq.createNewFile();
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arq))) {
                oos.writeObject(lista);
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar lista de instituições: " + e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public ArrayList<Instituicao> lerLista() {
        ArrayList<Instituicao> lista = new ArrayList<>();
        File arq = new File(CAMINHO_ARQUIVO);
        if (arq.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arq))) {
                lista = (ArrayList<Instituicao>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erro ao ler lista de instituições: " + e.getMessage());
            }
        }
        return lista;
    }

    @Override
    public void adicionar(Instituicao nova) {
        ArrayList<Instituicao> lista = lerLista();
        lista.add(nova);
        salvarLista(lista);
    }

    @Override
    public void atualizar(String cnpjAntigo, Instituicao atualizada) {
        ArrayList<Instituicao> lista = lerLista();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getCnpj().equals(cnpjAntigo)) {
                lista.set(i, atualizada); // Substitui todos os dados do objeto
                break;
            }
        }
        salvarLista(lista);
    }

    @Override
    public void excluir(String cnpj) {
        ArrayList<Instituicao> lista = lerLista();
        lista.removeIf(inst -> inst.getCnpj().equals(cnpj));
        salvarLista(lista);
    }
}
```

---

## 🎮 Passo 3: Criar o Controller Genérico

O `CrudController` implementa a lógica das operações CRUD que a View irá chamar. Ao utilizar Java Generics (`<T>`), o mesmo controller funciona perfeitamente para qualquer classe de modelo que utilize a interface `Persistencia`.

#### **CrudController.java** (Novo arquivo em `controller/`)
```java
package com.example.paceapp.controller;

import com.example.paceapp.model.Persistencia;
import java.util.ArrayList;

public class CrudController<T> {
    private final Persistencia<T> persistencia;

    public CrudController(Persistencia<T> persistencia) {
        this.persistencia = persistencia;
    }

    public ArrayList<T> listarTodos() {
        return persistencia.lerLista();
    }

    public void adicionar(T item) {
        persistencia.adicionar(item);
    }

    public void atualizar(String chaveAntiga, T itemAtualizado) {
        persistencia.atualizar(chaveAntiga, itemAtualizado);
    }

    public void excluir(String chave) {
        persistencia.excluir(chave);
    }
}
```

---

## 🖥️ Passo 4: Implementar as Views com JavaFX

### 4.1 A View Genérica
Essa classe cria a estrutura visual padrão para todas as telas de CRUD:
- Um título no topo e um botão de cadastro popup.
- Uma tabela que lista os dados e inclui dinamicamente botões **Editar** e **Excluir** na própria linha do registro.

#### **BaseCrudView.java** (Novo arquivo em `view/`)
```java
package com.example.paceapp.view;

import com.example.paceapp.controller.CrudController;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseCrudView<T> {
    protected final CrudController<T> controller;
    protected final String tituloTela;
    protected VBox rootPane;
    protected TableView<T> tabela;

    public BaseCrudView(String tituloTela, CrudController<T> controller) {
        this.tituloTela = tituloTela;
        this.controller = controller;
        this.rootPane = new VBox(15);
        this.rootPane.setPadding(new Insets(20));
        this.rootPane.setStyle("-fx-background-color: white;");
        this.construirInterface();
    }

    public VBox getPane() {
        atualizarTabela();
        return this.rootPane;
    }

    private void construirInterface() {
        // Cabeçalho com Título e Botão de Cadastro
        HBox cabecalho = new HBox();
        cabecalho.setAlignment(Pos.CENTER_LEFT);
        cabecalho.setSpacing(20);

        Label titulo = new Label(tituloTela);
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2C4A7C;");

        Button btnNovo = new Button("+ Novo Cadastro");
        btnNovo.setStyle("-fx-background-color: #2C4A7C; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        btnNovo.setOnAction(e -> abrirFormulario(null));

        cabecalho.getChildren().addAll(titulo, btnNovo);

        // Configuração da Tabela
        tabela = new TableView<>();
        tabela.setPrefHeight(400);

        // Carrega colunas de dados fornecidas pelas subclasses
        List<TableColumn<T, ?>> colunasDados = obterColunasDados();
        tabela.getColumns().addAll(colunasDados);

        // Coluna de Ações (Editar e Excluir)
        TableColumn<T, Void> colAcoes = new TableColumn<>("Ações");
        colAcoes.setPrefWidth(200);
        colAcoes.setCellFactory(param -> new TableCell<T, Void>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnExcluir = new Button("Excluir");
            private final HBox container = new HBox(8, btnEditar, btnExcluir);

            {
                btnEditar.setStyle("-fx-background-color: #F5C842; -fx-text-fill: #2C4A7C; -fx-font-weight: bold; -fx-cursor: hand;");
                btnExcluir.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
                container.setAlignment(Pos.CENTER);

                // Clique em Editar -> Abre formulário com dados existentes
                btnEditar.setOnAction(e -> {
                    T item = getTableView().getItems().get(getIndex());
                    abrirFormulario(item);
                });

                // Clique em Excluir -> Pede confirmação e remove
                btnExcluir.setOnAction(e -> {
                    T item = getTableView().getItems().get(getIndex());
                    Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION, "Confirmar exclusão?", ButtonType.YES, ButtonType.NO);
                    confirmacao.showAndWait().ifPresent(resposta -> {
                        if (resposta == ButtonType.YES) {
                            excluirItem(item);
                            atualizarTabela();
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(container);
                }
            }
        });

        tabela.getColumns().add(colAcoes);
        rootPane.getChildren().addAll(cabecalho, tabela);
    }

    public void atualizarTabela() {
        ArrayList<T> lista = controller.listarTodos();
        tabela.setItems(FXCollections.observableArrayList(lista));
    }

    // Janela modal (Popup) para inserir/editar dados
    protected void abrirModalFormulario(String titulo, VBox layoutFormulario) {
        Stage stageModal = new Stage();
        stageModal.setTitle(titulo);
        stageModal.initModality(Modality.APPLICATION_MODAL);
        
        Scene cena = new Scene(layoutFormulario, 400, 320);
        stageModal.setScene(cena);
        stageModal.showAndWait();
        atualizarTabela(); // Recarrega a tabela ao fechar o modal
    }

    protected void mostrarAlerta(Alert.AlertType tipo, String titulo, String conteudo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(conteudo);
        alerta.showAndWait();
    }

    // Ganchos que as Views filhas devem prover
    protected abstract List<TableColumn<T, ?>> obterColunasDados();
    protected abstract void abrirFormulario(T itemExistente);
    protected abstract void excluirItem(T item);
}
```

---

### 4.2 Views Específicas
As subclasses herdam de `BaseCrudView` e apenas especificam quais colunas de dados carregar, como montar os formulários no modal e como chamar o controller.

#### **UsuarioView.java** (Novo arquivo em `view/`)
```java
package com.example.paceapp.view;

import com.example.paceapp.controller.CrudController;
import com.example.paceapp.model.ArquivoUsuario;
import com.example.paceapp.model.Usuario;
import com.example.paceapp.model.ValidacaoException;
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
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEmail.setPrefWidth(150);

        TableColumn<Usuario, String> colTelefone = new TableColumn<>("Telefone");
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colTelefone.setPrefWidth(100);

        colunas.add(colNome);
        colunas.add(colEmail);
        colunas.add(colTelefone);

        return colunas;
    }

    @Override
    protected void abrirFormulario(Usuario usuarioExistente) {
        // Criar campos
        TextField txtNome = new TextField();
        TextField txtEmail = new TextField();
        PasswordField txtSenha = new PasswordField();
        TextField txtTelefone = new TextField();

        // Se estiver editando, pré-carrega os dados e bloqueia a alteração do ID/Email
        if (usuarioExistente != null) {
            txtNome.setText(usuarioExistente.getNome());
            txtEmail.setText(usuarioExistente.getEmail());
            txtSenha.setText(usuarioExistente.getSenha());
            txtTelefone.setText(usuarioExistente.getTelefone());
            txtEmail.setDisable(true); 
        }

        // Layout do Formulário no Modal
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

                // Validações obrigatórias
                if (nome.isEmpty()) throw new ValidacaoException("O campo 'Nome' é obrigatório.");
                if (email.isEmpty() || !email.contains("@")) throw new ValidacaoException("Email inválido.");
                if (senha.isEmpty()) throw new ValidacaoException("A senha é obrigatória.");
                if (!telefone.matches("\\d+")) throw new ValidacaoException("Telefone deve conter apenas números.");

                Usuario novo = new Usuario(nome, email, senha, telefone);

                if (usuarioExistente == null) {
                    controller.adicionar(novo);
                } else {
                    controller.atualizar(usuarioExistente.getEmail(), novo); // Passa o e-mail antigo como chave
                }

                // Fechar a janela modal
                ((Stage) btnSalvar.getScene().getWindow()).close();
            } catch (ValidacaoException ex) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro de Validação", ex.getMessage());
            }
        });

        abrirModalFormulario(usuarioExistente == null ? "Cadastrar Usuário" : "Editar Usuário", layoutModal);
    }

    @Override
    protected void excluirItem(Usuario usuario) {
        controller.excluir(usuario.getEmail());
    }
}
```

#### **InstituicaoView.java** (Novo arquivo em `view/`)
```java
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

        TableColumn<Instituicao, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colNome.setPrefWidth(120);

        TableColumn<Instituicao, String> colCnpj = new TableColumn<>("CNPJ");
        colCnpj.setCellValueFactory(new PropertyValueFactory<>("cnpj"));
        colCnpj.setPrefWidth(130);

        TableColumn<Instituicao, String> colData = new TableColumn<>("Fundação");
        colData.setCellValueFactory(new PropertyValueFactory<>("dataFundacao"));
        colData.setPrefWidth(100);

        colunas.add(colNome);
        colunas.add(colCnpj);
        colunas.add(colData);

        return colunas;
    }

    @Override
    protected void abrirFormulario(Instituicao instituicaoExistente) {
        TextField txtNome = new TextField();
        TextField txtCnpj = new TextField();
        TextField txtDataFundacao = new TextField();
        txtDataFundacao.setPromptText("DD/MM/AAAA");

        if (instituicaoExistente != null) {
            txtNome.setText(instituicaoExistente.getNome());
            txtCnpj.setText(instituicaoExistente.getCnpj());
            txtDataFundacao.setText(instituicaoExistente.getDataFundacao());
            txtCnpj.setDisable(true);
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Nome:"), 0, 0);
        grid.add(txtNome, 1, 0);
        grid.add(new Label("CNPJ (14 dígitos):"), 0, 1);
        grid.add(txtCnpj, 1, 1);
        grid.add(new Label("Data Fundação:"), 0, 2);
        grid.add(txtDataFundacao, 1, 2);

        Button btnSalvar = new Button("Confirmar");
        btnSalvar.setStyle("-fx-background-color: #2C4A7C; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");

        VBox layoutModal = new VBox(20, grid, btnSalvar);
        layoutModal.setPadding(new Insets(20));

        btnSalvar.setOnAction(e -> {
            try {
                String nome = txtNome.getText().trim();
                String cnpj = txtCnpj.getText().trim();
                String dataStr = txtDataFundacao.getText().trim();

                if (nome.isEmpty()) throw new ValidacaoException("O campo 'Nome' é obrigatório.");
                if (!cnpj.matches("\\d{14}")) throw new ValidacaoException("O CNPJ deve conter exatamente 14 dígitos numéricos.");

                // Validação de Data (Formato dd/MM/yyyy)
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                try {
                    LocalDate.parse(dataStr, formatter);
                } catch (DateTimeParseException ex) {
                    throw new ValidacaoException("A data deve estar no formato DD/MM/AAAA.");
                }

                Instituicao nova = new Instituicao(nome, cnpj, dataStr);

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
```

---

## 🔗 Passo 5: Integrar no `MainView.java`

Para exibir suas novas views na tela principal ao clicar nos botões do menu lateral, abra o arquivo [MainView.java](file:///c:/Users/gigil/IdeaProjects/PACE-APP/src/main/java/com/example/paceapp/view/MainView.java).

Encontre as ações dos botões `btnInstituicao` e `btnUsuario` (nas linhas 74 e 75) e conecte-os com as instâncias das novas classes:

```diff
-        btnInstituicao.setOnAction(e -> areaConteudo.getChildren().setAll(new Label("CRUD Instituição")));
-        btnUsuario.setOnAction(e -> areaConteudo.getChildren().setAll(new Label("CRUD Usuário")));
+        btnInstituicao.setOnAction(e -> areaConteudo.getChildren().setAll(new InstituicaoView().getPane()));
+        btnUsuario.setOnAction(e -> areaConteudo.getChildren().setAll(new UsuarioView().getPane()));
```
