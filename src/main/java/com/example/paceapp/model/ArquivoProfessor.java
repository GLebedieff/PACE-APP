package com.example.paceapp.model;

import java.io.*;
import java.util.ArrayList;

public class ArquivoProfessor implements Persistencia<Professor> {
    private static final String CAMINHO_ARQUIVO = "professores.dat"; // Constante. Está definindo o nome do arquivo físico

    @Override
    public void salvarLista(ArrayList<Professor> lista){ // Lista RAM (dinâmica) -> lista arq física ssd
        try{ // Ali ele recebe a lista de professores da RAM (parâmetro)
            File arq = new File(CAMINHO_ARQUIVO); // tratar erros do disco
            if(!arq.exists()){ // Se o arquivo não estiver fisicamente criado
                arq.createNewFile(); // cria um arquivo em branco
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arq))) {
                oos.writeObject(lista);
            }
        }catch (IOException e){
            System.err.println("Erro ao salvar lista de professores:" + e.getMessage());
        }
    }
    @Override
    public ArrayList<Professor> lerLista(){
        ArrayList<Professor> lista = new ArrayList<>();
        File arq = new File(CAMINHO_ARQUIVO);
        if(arq.exists()){
            try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arq))){
                lista = (ArrayList<Professor>) ois.readObject();
            }catch (IOException | ClassNotFoundException e){
                System.err.println("Erro ao ler lista de professores:" + e.getMessage());
            }
        }
        return lista;
    }
    @Override
    public void adicionar(Professor novo){ //Quando um novo cadastro é feito e preciso adicioná-lo ao arquivo do disco
        ArrayList<Professor> lista = lerLista(); // Para ler a lista atual no disco
        lista.add(novo);
        salvarLista(lista);
    }
    @Override
    public void atualizar(String cpfAntigo, Professor atualizado){ // Prof atualizado -> obj q recebe os novos dados
        ArrayList<Professor> lista = lerLista();
        for(int i = 0; i < lista.size(); i++){ // Percorrendo a lista de registros de Professor
            if(lista.get(i).getCpf().equals(cpfAntigo)){ // Quando encontrar o professor c o cpf "equivalente" ao "antigo"
                lista.set(i, atualizado);// substitui pelo atualizado (objeto)
                break;
            }
        }
        salvarLista(lista);
    }
    @Override
    public void excluir(String cpf){
        ArrayList<Professor> lista = lerLista();
        lista.removeIf(prof -> prof.getCpf().equals(cpf)); // excluir o registro com o cpf certo
        salvarLista(lista); // Salvar a lista novamente (agora atualizada com a exclusão)
    }
}
