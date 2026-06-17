package com.example.paceapp.model;

import java.io.*;
import java.util.ArrayList;

public class ArquivoInstituicao implements Persistencia<Instituicao>{
    private static final String CAMINHO_ARQUIVO = "instituicoes.dat";

    @Override
    public void salvarLista(ArrayList<Instituicao> lista){
        try {
            File arq = new File(CAMINHO_ARQUIVO);
            if(!arq.exists()) {
                arq.createNewFile();
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arq))){
                oos.writeObject(lista);
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar a lista de instituições: " + e.getMessage());
        }
    }

    @Override
    public ArrayList<Instituicao> lerLista(){
        ArrayList<Instituicao> lista = new ArrayList<>();
        File arq = new File(CAMINHO_ARQUIVO);
        if (arq.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream((new FileInputStream(arq)))){
                lista = (ArrayList<Instituicao>) ois.readObject();
            } catch (IOException | ClassNotFoundException e){
                System.err.println("Erro ao ler lista de instituições: " + e.getMessage());
            }
        }
        return lista;
    }

    //Create
    @Override
    public void adicionar(Instituicao nova){
        ArrayList<Instituicao> lista = lerLista();
        lista.add(nova);
        salvarLista(lista);
    }

    //Update
    @Override
    public void atualizar(String cnpjAntigo, Instituicao atualizada){
        ArrayList<Instituicao> lista = lerLista();
        for (int i = 0; i < lista.size(); i++){
            if (lista.get(i).getCnpj().equals(cnpjAntigo)){
                lista.set(i, atualizada);
                break;
            }
        }
        salvarLista(lista);
    }

    //Delete
    @Override
    public void excluir(String cnpj){
        ArrayList<Instituicao> lista = lerLista();
        lista.removeIf(instituicao -> instituicao.getCnpj().equals(cnpj));
        salvarLista(lista);
    }
}
