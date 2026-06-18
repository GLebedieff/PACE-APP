package com.example.paceapp.model;

import java.io.*;
import java.util.ArrayList;

public class ArquivoPedagogo implements Persistencia<Pedagogo> {
    private static final String CAMINHO_ARQUIVO = "pedagogos.dat";

    @Override
    public void salvarLista(ArrayList<Pedagogo> lista){
        try {
            File arq = new File(CAMINHO_ARQUIVO);
            if(!arq.exists()) {
                arq.createNewFile();
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arq))){
                oos.writeObject(lista);
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar a lista de pedagogos: " + e.getMessage());
        }
    }

    @Override
    public ArrayList<Pedagogo> lerLista(){
        ArrayList<Pedagogo> lista = new ArrayList<>();
        File arq = new File(CAMINHO_ARQUIVO);
        if (arq.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream((new FileInputStream(arq)))){
                lista = (ArrayList<Pedagogo>) ois.readObject();
            } catch (IOException | ClassNotFoundException e){
                System.err.println("Erro ao ler lista de pedagogos: " + e.getMessage());
            }
        }
        return lista;
    }

    //Create
    @Override
    public void adicionar(Pedagogo novo){
        ArrayList<Pedagogo> lista = lerLista();
        lista.add(novo);
        salvarLista(lista);
    }

    //Update
    @Override
    public void atualizar(String cpfAntigo, Pedagogo atualizado){
        ArrayList<Pedagogo> lista = lerLista();
        for (int i = 0; i < lista.size(); i++){
            if (lista.get(i).getCpf().equals(cpfAntigo)){
                lista.set(i, atualizado);
                break;
            }
        }
        salvarLista(lista);
    }

    //Delete
    @Override
    public void excluir(String cpf){
        ArrayList<Pedagogo> lista = lerLista();
        lista.removeIf(pedagogo -> pedagogo.getCpf().equals(cpf));
        salvarLista(lista);
    }
}
