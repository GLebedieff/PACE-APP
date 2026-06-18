package com.example.paceapp.model;

import java.io.*;
import java.util.ArrayList;

public class ArquivoTurma implements Persistencia<Turma> {
    private static final String CAMINHO_ARQUIVO = "turmas.dat";

    @Override
    public void salvarLista(ArrayList<Turma> lista){
        try {
            File arq = new File(CAMINHO_ARQUIVO);
            if(!arq.exists()) {
                arq.createNewFile();
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arq))){
                oos.writeObject(lista);
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar a lista de turmas: " + e.getMessage());
        }
    }

    @Override
    public ArrayList<Turma> lerLista(){
        ArrayList<Turma> lista = new ArrayList<>();
        File arq = new File(CAMINHO_ARQUIVO);
        if (arq.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream((new FileInputStream(arq)))){
                lista = (ArrayList<Turma>) ois.readObject();
            } catch (IOException | ClassNotFoundException e){
                System.err.println("Erro ao ler lista de turmas: " + e.getMessage());
            }
        }
        return lista;
    }

    //Create
    @Override
    public void adicionar(Turma nova){
        ArrayList<Turma> lista = lerLista();
        lista.add(nova);
        salvarLista(lista);
    }

    //Update
    @Override
    public void atualizar(String nomeTurmaAntigo, Turma atualizada){
        ArrayList<Turma> lista = lerLista();
        for (int i = 0; i < lista.size(); i++){
            if (lista.get(i).getNomeTurma().equals(nomeTurmaAntigo)){
                lista.set(i, atualizada);
                break;
            }
        }
        salvarLista(lista);
    }

    //Delete
    @Override
    public void excluir(String nomeTurma){
        ArrayList<Turma> lista = lerLista();
        lista.removeIf(turma -> turma.getNomeTurma().equals(nomeTurma));
        salvarLista(lista);
    }

}
